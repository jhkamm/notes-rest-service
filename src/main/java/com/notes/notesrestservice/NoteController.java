package com.notes.notesrestservice;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(NoteController.class);

    class Credentials {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Autowired
    private NoteRepository noteRepository;

    private Credentials decodeAuthHeader(String authorization) {
        // Authorization: Basic base64credentials
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        Credentials creds = new Credentials();
        creds.setEmail(values[0]);
        creds.setPassword(values[1]);
        return creds;
    }

    // Read methods
    @GetMapping(path="/all")
    public ResponseEntity<Iterable<Note>> getAllUsers(@RequestHeader("authorization") String auth) {
        return ResponseEntity.ok(noteRepository.findByOwner(decodeAuthHeader(auth).getEmail()));
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<Note> getNoteById(@RequestHeader("authorization") String auth, @PathVariable("id") Long id) {
        String userEmail = decodeAuthHeader(auth).getEmail();
        Optional<Note> note = noteRepository.findById(id);
        if (!note.isPresent() | !note.get().getOwner().equals(decodeAuthHeader(auth).getEmail())) {
            log.info("Note does not belong to user issuing request.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note.get());
    }

    // Create method
    @PostMapping(path="/add")
    public ResponseEntity<Object> addNewNote (@RequestHeader("authorization") String auth, @RequestBody Note newNote) {
        if (newNote.getTitle().length() == 0 | newNote.getTitle().length() > 50) {
            // Invalid title
            return ResponseEntity.badRequest().body("Cannot create note. Note title must not be empty and must be less than 50 characters.");
        }
        if (newNote.getNote().length() > 1000) {
            // Invalid note
            return ResponseEntity.badRequest().body("Cannot create note. Note text must be less than 1000 characters.");
        }
        newNote.setOwner(decodeAuthHeader(auth).getEmail());
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
    public ResponseEntity<Object> updateNote(@RequestHeader("authorization") String auth, @RequestBody Note newNote, @PathVariable("id") Long id) {
        if (newNote.getTitle().length() == 0 | newNote.getTitle().length() > 50) {
            // Invalid title
            return ResponseEntity.badRequest().body("Cannot create note. Note title must not be empty and must be less than 50 characters.");
        }
        if (newNote.getNote().length() > 1000) {
            // Invalid note
            return ResponseEntity.badRequest().body("Cannot create note. Note text must be less than 1000 characters.");
        }
        return noteRepository.findById(id).map(note -> {
            if (!note.getOwner().equals(decodeAuthHeader(auth).getEmail())) {
                log.info("Note does not belong to user issuing request.");
                return ResponseEntity.notFound().build();
            }
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
    public ResponseEntity<Object> deleteNote(@RequestHeader("authorization") String auth, @PathVariable("id") Long id) {
        Optional<Note> note = noteRepository.findById(id);
        if (!note.isPresent() | !note.get().getOwner().equals(decodeAuthHeader(auth).getEmail())) {
            log.info("Note does not belong to user issuing request.");
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
