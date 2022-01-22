package com.notes.notesrestservice;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Autowired
    private UserRepository userRepository;
    private UserManager userManager;

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        if (repository.findAll().isEmpty()) {
            return args -> {
                try {
                    userManager = new UserManager();
                    userManager.setUserRepository(userRepository);
                    User user1 = new User();
                    user1.setEmail("user1@example.com");
                    user1.setPassword("password1");
                    Timestamp timeNow = new Timestamp(System.currentTimeMillis());
                    user1.setCreateTime(timeNow);
                    user1.setLastUpdated(timeNow);
                    User user2 = new User();
                    user2.setEmail("user2@example.com");
                    user2.setPassword("password2");
                    timeNow = new Timestamp(System.currentTimeMillis());
                    user2.setCreateTime(timeNow);
                    user2.setLastUpdated(timeNow);
                    log.info("Preloading " + userManager.createUser(user1));
                    log.info("Preloading " + userManager.createUser(user2));
                } catch (IllegalArgumentException e) {
                    log.warn("Failed preloading user data due to IllegalArgumentException " + e.getMessage());
                }
            };
        }
        return args -> {
            log.info("User repository already contains users. Will not preload");
        };
    }
}