package br.com.moraesit.order.service.infra.rest.exception.handler;

import br.com.moraesit.commons.rest.exception.handler.ErrorDTO;
import br.com.moraesit.commons.rest.exception.handler.GlobalExceptionHandler;
import br.com.moraesit.order.service.domain.exception.OrderDomainException;
import br.com.moraesit.order.service.domain.exception.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(OrderGlobalExceptionHandler.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = OrderDomainException.class)
    public ErrorDTO handleException(OrderDomainException orderDomainException) {
        logger.error(orderDomainException.getMessage(), orderDomainException);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(orderDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = OrderNotFoundException.class)
    public ErrorDTO handleException(OrderNotFoundException orderNotFoundException) {
        logger.error(orderNotFoundException.getMessage(), orderNotFoundException);
        return ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(orderNotFoundException.getMessage())
                .build();
    }
}
