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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Note controller.
 */
@RestController
@RequestMapping("/api/note")
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;
    private final GroupService groupService;
    private final JwtUtils jwtUtils;

    /**
     * Instantiates a new Note controller.
     *
     * @param userService  the user service
     * @param noteService  the note service
     * @param groupService the group service
     * @param jwtUtils     the jwt utils
     */
    public NoteController(UserService userService, NoteService noteService,
                          GroupService groupService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.noteService = noteService;
        this.groupService = groupService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Gets all notes from user.
     *
     * @param authorizationHeader the authorization header
     * @return the all notes from user
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllNotesFromUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }
            List<Note> noteList = noteService.getNoteListsByUser(user);
            return ResponseEntity.ok(noteList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    /**
     * Gets note from user.
     *
     * @param authorizationHeader the authorization header
     * @param noteId              the note id
     * @return the note from user
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNoteFromUser(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long noteId) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }
            Note note = noteService.getNoteById(user, noteId);
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            if (e instanceof ResourceDoesntExistException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    /**
     * Gets all notes from user groups.
     *
     * @param authorizationHeader the authorization header
     * @return the all notes from user groups
     */
    @GetMapping("/my-groups")
    public ResponseEntity<?> getAllNotesFromUserGroups(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            List<Group> userGroups = groupService.getUserGroups(user);
            if (userGroups == null || userGroups.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User is not an owner or a member of any groups."));
            }
            List<Note> notesFromGroups = noteService.getNotesFromGroups(userGroups, user);
            return ResponseEntity.ok(notesFromGroups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    /**
     * Create note response entity.
     *
     * @param authorizationHeader the authorization header
     * @param note                the note
     * @return the response entity
     */
    @PostMapping("/")
    public ResponseEntity<?> createNote(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Note note) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }
            note.setUser(user);
            noteService.createNote(user, note);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    /**
     * Update note response entity.
     *
     * @param authorizationHeader the authorization header
     * @param noteId              the note id
     * @param updatedNote         the updated note
     * @return the response entity
     */
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
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            Note savedNote = noteService.updateNote(user, noteId, updatedNote);
            return ResponseEntity.ok(savedNote);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Authorization header is invalid"));
        } catch (ResourceDoesntExistException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
        }
    }

    /**
     * Delete note response entity.
     *
     * @param authorizationHeader the authorization header
     * @param noteId              the note id
     * @return the response entity
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNote(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long noteId) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }
            Note note = noteService.getNoteById(user, noteId);
            noteService.deleteNote(user, note);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(note);
        } catch (Exception e) {
            if (e instanceof ResourceDoesntExistException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }

    /**
     * Filter note response entity.
     *
     * @param authorizationHeader the authorization header
     * @param filterDTO           the filter dto
     * @return the response entity
     */
    @PostMapping("/filter")
    public ResponseEntity<?> filterNote(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody NoteFilter filterDTO) {
        try {
            String token = getTokenFromHeader(authorizationHeader);
            User user = getUserFromToken(token);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found"));
            }

            if (filterDTO == null || filterDTO.getNotes() == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request body"));
            }

            ArrayList<Note> filteredNotes = noteService.filterNotes(filterDTO.getNotes(), filterDTO.getCategory());
            return ResponseEntity.ok(filteredNotes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred: " + e.getMessage()));
        }
    }

    /**
     * Search note response entity.
     *
     * @param authorizationHeader the authorization header
     * @param searchRequest       the search request
     * @return the response entity
     */
    @PostMapping("/search")
    public ResponseEntity<?> searchNote(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SearchRequest searchRequest) {
        try {
            ArrayList<Note> unfilteredNotes = searchRequest.getNotes();
            String query = searchRequest.getQuery();
            if (query == null || unfilteredNotes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Nothing to search"));
            }
            ArrayList<Note> filteredNotes = noteService.findNotes(unfilteredNotes, query);
            if (filteredNotes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "No results found"));
            }
            return ResponseEntity.ok(filteredNotes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
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
