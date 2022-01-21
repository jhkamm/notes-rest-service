package com.notes.notesrestservice;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(path="/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    // Read methods
    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Note>> getAllUsers(@RequestHeader Map<String, String> header) {
        System.out.println("HEADERS: " + header);

        return ResponseEntity.ok(noteRepository.findAll());
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") Long id) {
        Optional<Note> note = noteRepository.findById(id);
        if (!note.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note.get());
    }

    // Create method
    @PostMapping(path="/add")
    public ResponseEntity<Object> addNewNote (@RequestBody Note newNote) {
        if (newNote.getTitle().length() == 0 | newNote.getTitle().length() > 50) {
            // Invalid title
            return ResponseEntity.badRequest().body("Cannot create note. Note title must not be empty and must be less than 50 characters.");
        }
        if (newNote.getNote().length() > 1000) {
            // Invalid note
            return ResponseEntity.badRequest().body("Cannot create note. Note text must be less than 1000 characters.");
        }
        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        newNote.setCreateTime(timeNow);
        newNote.setLastUpdated(timeNow);
        noteRepository.save(newNote);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(newNote.getId())
          .toUri();

        return ResponseEntity.created(uri).body(newNote);
    }

    // Update method
    @PutMapping(path="/{id}")
    public ResponseEntity<Object> updateNote(@RequestBody Note newNote, @PathVariable("id") Long id) {
        if (newNote.getTitle().length() == 0 | newNote.getTitle().length() > 50) {
            // Invalid title
            return ResponseEntity.badRequest().body("Cannot create note. Note title must not be empty and must be less than 50 characters.");
        }
        if (newNote.getNote().length() > 1000) {
            // Invalid note
            return ResponseEntity.badRequest().body("Cannot create note. Note text must be less than 1000 characters.");
        }
        return noteRepository.findById(id).map(note -> {
            note.setTitle(newNote.getTitle());
            note.setNote(newNote.getNote());
            note.setLastUpdated(new Timestamp(System.currentTimeMillis()));
            return ResponseEntity.ok((Object) noteRepository.save(note));
        }).orElseGet(() -> {
            newNote.setId(id);
            Timestamp timeNow = new Timestamp(System.currentTimeMillis());
            newNote.setCreateTime(timeNow);
            newNote.setLastUpdated(timeNow);
            return ResponseEntity.ok((Object) noteRepository.save(newNote));
        });
    }

    // Delete method
    @DeleteMapping(path="/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable("id") Long id) {
        Optional<Note> note = noteRepository.findById(id);
        if (!note.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
