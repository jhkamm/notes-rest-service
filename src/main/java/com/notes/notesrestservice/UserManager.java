package com.notes.notesrestservice;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManager {
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) throws IllegalArgumentException {
        if (!emailValidator.isValid(user.getEmail())) {
            throw new IllegalArgumentException("User email must be a valid email address.");
        }
        if (userRepository.findByEmail(user.getEmail()) == null) {
            throw new IllegalArgumentException("User email is already present in the system.");
        }
        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("User password must be at least 8 characters.");
        }
        return userRepository.save(user);
    }
}
