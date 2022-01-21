package com.example.accessingdatamysql;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/notes")
public class MainController {
    @Autowired
    private NoteRepository noteRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser (@RequestParam String title, @RequestParam String note) {
        if (title.length == 0 | title.length > 50) {
            // Bad title, handle error on title
            return "Cannot create note. Note title must not be empty and must be less than 50 characters.";
        }
        if (note.length > 1000) {
            return "Cannot create note. Note text must be less than 1000 characters.";
        }
        Note n = new Note();
        n.setTitle(title);
        n.setNote(note);
        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        n.setCreateTime(timeNow);
        n.setLastUpdated(timeNow);
        noteRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return noteRepository.findAll();
    }
}
