package com.notes.notesrestservice;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(path="/notes")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping(path="/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable("id") Integer id) {
        Optional<Note> note = noteRepository.findById(id);
        if (!note.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note.get());
    }

    @PostMapping(path="/add") // Map ONLY POST Requests
    public ResponseEntity<String> addNewNote (@RequestParam String title, @RequestParam String note) {
        if (title.length() == 0 | title.length() > 50) {
            // Bad title, handle error on title
            return ResponseEntity.badRequest().body("Cannot create note. Note title must not be empty and must be less than 50 characters.");
        }
        if (note.length() > 1000) {
            return ResponseEntity.badRequest().body("Cannot create note. Note text must be less than 1000 characters.");
        }
        Note n = new Note();
        n.setTitle(title);
        n.setNote(note);
        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        n.setCreateTime(timeNow);
        n.setLastUpdated(timeNow);
        noteRepository.save(n);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
          .path("/{id}")
          .buildAndExpand(n.getId())
          .toUri();

        return ResponseEntity.created(uri).body("Note saved");
    }

    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Note>> getAllUsers() {
        // This returns a JSON or XML with the users
        return ResponseEntity.ok(noteRepository.findAll());
    }
}
