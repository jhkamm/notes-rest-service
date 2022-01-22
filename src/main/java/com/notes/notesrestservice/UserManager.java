package com.notes.notesrestservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManager {
    private static final Logger log = LoggerFactory.getLogger(UserManager.class);

    private EmailValidator emailValidator = EmailValidator.getInstance();

    private UserRepository userRepository;

    public User createUser(User user) throws IllegalArgumentException {
        if (!emailValidator.isValid(user.getEmail())) {
            throw new IllegalArgumentException("User email must be a valid email address.");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User email is already present in the system.");
        }
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("User password must be at least 8 characters.");
        }
        return userRepository.save(user);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
