package org.metropolia.minimalnotepad.controller;


import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.NoteService;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;
    private final JwtUtils jwtUtils;
    public NoteController(UserService userService, NoteService noteService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.noteService = noteService;
        this.jwtUtils = jwtUtils;
    }
    @GetMapping("/")
    public ResponseEntity<?> getAllNotesFromUser(@RequestHeader String token) {
        User user = getUserFromToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
        }
        List<Note> noteList =  noteService.getNoteListsByUser(user);
        return ResponseEntity.ok(noteList);
    }
    private User getUserFromToken(String token) {
        String username = jwtUtils.extractUsername(token);
        return userService.getUserByUsername(username);
    }
}
