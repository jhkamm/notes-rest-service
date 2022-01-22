package com.notes.notesrestservice;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        if (repository.findAll().isEmpty()) {
            return args -> {
                User user1 = new User();
                user1.setEmail("user1@example.com");
                user1.setPassword("password");
                Timestamp timeNow = new Timestamp(System.currentTimeMillis());
                user1.setCreateTime(timeNow);
                user1.setLastUpdated(timeNow);
                User user2 = new User();
                user2.setEmail("user2@example.com");
                user2.setPassword("password2");
                timeNow = new Timestamp(System.currentTimeMillis());
                user2.setCreateTime(timeNow);
                user2.setLastUpdated(timeNow);
                log.info("Preloading " + repository.save(user1));
                log.info("Preloading " + repository.save(user2));
            };
        }
        return args -> {
            log.info("User repository already contains users. Will not preload");
        };
    }
}