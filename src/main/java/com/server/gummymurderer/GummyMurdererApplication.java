package com.server.gummymurderer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class GummyMurdererApplication {

    public static void main(String[] args) {
        SpringApplication.run(GummyMurdererApplication.class, args);
    }

}
