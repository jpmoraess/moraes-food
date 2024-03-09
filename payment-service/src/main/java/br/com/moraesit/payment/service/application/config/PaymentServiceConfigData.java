package br.com.moraesit.payment.service.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment-service")
public class PaymentServiceConfigData {

    private String paymentRequestTopicName;
    private String paymentResponseTopicName;

    public String getPaymentRequestTopicName() {
        return paymentRequestTopicName;
    }

    public void setPaymentRequestTopicName(String paymentRequestTopicName) {
        this.paymentRequestTopicName = paymentRequestTopicName;
    }

    public String getPaymentResponseTopicName() {
        return paymentResponseTopicName;
    }

    public void setPaymentResponseTopicName(String paymentResponseTopicName) {
        this.paymentResponseTopicName = paymentResponseTopicName;
    }
}
