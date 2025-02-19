package org.metropolia.minimalnotepad.controller;


import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
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
    public ResponseEntity<?> getAllNotesFromUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }
            List<Note> noteList = noteService.getNoteListsByUser(user);
            return ResponseEntity.ok(noteList);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }
    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteFromUser(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long noteId) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }
            Note note = noteService.getNoteById(user, noteId);
            return ResponseEntity.ok(note);
        }catch (Exception e) {
            if(e instanceof ResourceDoesntExistException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> createNote(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Note note) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }
            note.setUser(user);
            System.out.println(note.getColour());
            noteService.createNote(user,note);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }
    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long noteId) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }
            Note note = noteService.getNoteById(user, noteId);
            noteService.deleteNote(user,note);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(note);
        }catch (Exception e) {
            if(e instanceof ResourceDoesntExistException) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, e.getMessage()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }
    private String getTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is invalid");
        }
        return authorizationHeader.substring(7);
    }
    private User getUserFromToken(String token) {
        String username = jwtUtils.extractUsername(token);
        return userService.getUserByUsername(username);
    }
}
