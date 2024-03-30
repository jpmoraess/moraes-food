package br.com.moraesit.restaurant.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"br.com.moraesit.restaurant.service.infra", "br.com.moraesit.commons.persistence"})
@EnableJpaRepositories(basePackages = {"br.com.moraesit.restaurant.service.infra", "br.com.moraesit.commons.persistence"})
@SpringBootApplication(scanBasePackages = {"br.com.moraesit.restaurant.service", "br.com.moraesit.commons"})
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}
