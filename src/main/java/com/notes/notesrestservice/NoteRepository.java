package com.notes.notesrestservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwner(final String owner);
}
