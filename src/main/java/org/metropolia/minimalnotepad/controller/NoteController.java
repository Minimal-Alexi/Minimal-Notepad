package org.metropolia.minimalnotepad.controller;


import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.NoteFilter;
import org.metropolia.minimalnotepad.dto.SearchRequest;
import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.service.GroupService;
import org.metropolia.minimalnotepad.service.NoteService;
import org.metropolia.minimalnotepad.service.UserService;
import org.metropolia.minimalnotepad.utils.JwtUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;
    private final GroupService groupService;
    private final JwtUtils jwtUtils;
    public NoteController(UserService userService, NoteService noteService, GroupService groupService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.noteService = noteService;
        this.groupService = groupService;
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
    @GetMapping("/my-groups")
    public ResponseEntity<?> getAllNotesFromUserGroups(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User not found"));
            }

            List<Group> userGroups = groupService.getUserGroups(user);
            if (userGroups == null || userGroups.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "User is not an owner or a member of any groups."));
            }

            List<Note> notesFromGroups = noteService.getNotesFromGroups(userGroups);
            System.out.println(notesFromGroups);
            return ResponseEntity.ok(notesFromGroups);
        }catch (Exception e) {
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
            noteService.createNote(user,note);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, e.getMessage()));
        }
    }

    @PatchMapping("/{noteId}")
    public ResponseEntity<?> updateNote(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable long noteId,
            @RequestBody Note updatedNote) {
        try {
            String token = getTokenFromHeader(authorizationHeader);

            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "User not found"));
            }

            Note savedNote = noteService.updateNote(user, noteId, updatedNote);
            return ResponseEntity.ok(savedNote);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(401, "Authorization header is invalid"));
        } catch (ResourceDoesntExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "An unexpected error occurred"));
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

    @PostMapping("/filter")
    public ResponseEntity<?> filterNote(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody NoteFilter filterDTO) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "User not found"));
            }

            if (filterDTO == null || filterDTO.getNotes() == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(400, "Invalid request body"));
            }

            ArrayList<Note> filteredNotes = noteService.filterNotes(filterDTO.getNotes(), filterDTO.getCategory());
            return ResponseEntity.ok(filteredNotes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "An error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchNote(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SearchRequest searchRequest) {
        try{
            ArrayList<Note> unfilteredNotes = searchRequest.getNotes();
            String query = searchRequest.getQuery();
            if(query == null || unfilteredNotes.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, "Nothing to search"));
            }
            ArrayList<Note> filteredNotes = noteService.findNotes(unfilteredNotes, query);
            if(filteredNotes.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, "No results found"));
            }
            return ResponseEntity.ok(filteredNotes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(500, "An unexpected error occurred"));
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
