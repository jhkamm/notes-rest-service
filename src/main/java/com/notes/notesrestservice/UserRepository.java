package com.notes.notesrestservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notes.notesrestservice.User;

interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(final String email);
}