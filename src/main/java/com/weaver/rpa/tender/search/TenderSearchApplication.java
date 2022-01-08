package com.weaver.rpa.tender.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages="com.weaver.rpa")
@EnableScheduling
public class TenderSearchApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TenderSearchApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TenderSearchApplication.class, args);
    }
}
