package br.com.moraesit.order.service.saga;

import br.com.moraesit.commons.domain.valueobject.PaymentStatus;
import br.com.moraesit.commons.saga.SagaStatus;
import br.com.moraesit.order.service.OrderServiceApplication;
import br.com.moraesit.order.service.application.OrderPaymentSaga;
import br.com.moraesit.order.service.application.dto.message.PaymentResponse;
import br.com.moraesit.order.service.infra.persistence.outbox.payment.entity.PaymentOutboxEntity;
import br.com.moraesit.order.service.infra.persistence.outbox.payment.repository.PaymentOutboxJpaRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static br.com.moraesit.commons.saga.SagaConstants.ORDER_SAGA_NAME;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = OrderServiceApplication.class)
@Sql(value = {"classpath:sql/OrderPaymentSagaTestSetUp.sql"})
@Sql(value = {"classpath:sql/OrderPaymentSagaTestCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
public class OrderPaymentSagaTest {
    private static final Logger log = LoggerFactory.getLogger(OrderPaymentSagaTest.class);

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;
    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    private final UUID SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa");
    private final UUID ORDER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb17");
    private final UUID CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41");
    private final UUID PAYMENT_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("100");

    @Test
    void testDoublePayment() {
        orderPaymentSaga.process(getPaymentResponse());
        orderPaymentSaga.process(getPaymentResponse());
    }

    //@Test
    void testDoublePaymentWithThreads() throws InterruptedException {
        Thread t1 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
        Thread t2 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertPaymentOutbox();
    }

    //@Test
    void testDoublePaymentWithLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread-1");
            } finally {
                countDownLatch.countDown();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread-2");
            } finally {
                countDownLatch.countDown();
            }
        });

        t1.start();
        t2.start();

        countDownLatch.await();

        assertPaymentOutbox();
    }

    private void assertPaymentOutbox() {
        Optional<PaymentOutboxEntity> paymentOutboxEntity =
                paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(ORDER_SAGA_NAME, SAGA_ID,
                        List.of(SagaStatus.PROCESSING));
        assertTrue(paymentOutboxEntity.isPresent());
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(SAGA_ID.toString())
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentId(PAYMENT_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }

}
