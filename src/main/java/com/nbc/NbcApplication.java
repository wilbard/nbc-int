package com.nbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMBeanExport
@EntityScan(basePackages = {"com.nbc.model"})
@EnableJpaRepositories(basePackages = {"com.nbc.repository"})
@ComponentScan(basePackages = {"com.nbc.*"})
public class NbcApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NbcApplication.class, args);
    }
}
