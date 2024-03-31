package br.com.moraesit.order.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EntityScan(basePackages = {"br.com.moraesit.order.service.infra", "br.com.moraesit.commons.persistence"})
@EnableJpaRepositories(basePackages = {"br.com.moraesit.order.service.infra", "br.com.moraesit.commons.persistence"})
@SpringBootApplication(scanBasePackages = {"br.com.moraesit.order.service", "br.com.moraesit.commons"})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
