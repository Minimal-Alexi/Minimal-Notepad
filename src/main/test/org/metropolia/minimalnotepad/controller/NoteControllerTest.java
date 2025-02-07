package org.metropolia.minimalnotepad.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NoteControllerTest {

    @Autowired
    private NoteController noteController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllNotesFromUserValidUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("Test Note");
        note.setText("This is a test note.");
        note.setUser(user);
        noteRepository.save(note);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        ResponseEntity<?> responseEntity = noteController.getAllNotesFromUser(authHeader);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof List<?>);
        List<?> noteList = (List<?>) body;
        assertEquals(1, noteList.size());

        Note returnedNote = (Note) noteList.get(0);
        assertEquals("Test Note", returnedNote.getTitle());
        assertEquals("This is a test note.", returnedNote.getText());
    }

    @Test
    public void testGetAllNotesFromUserInvalidHeader() {
        String invalidHeader = "InvalidHeader";
        ResponseEntity<?> responseEntity = noteController.getAllNotesFromUser(invalidHeader);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(401, error.getStatus());
        assertEquals("Authorization header is invalid", error.getMessage());
    }

    @Test
    public void testGetAllNotesFromUserUserNotFound() {
        String token = jwtUtils.generateToken("nonexistent");
        String authHeader = "Bearer " + token;

        ResponseEntity<?> responseEntity = noteController.getAllNotesFromUser(authHeader);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(404, error.getStatus());
        assertEquals("User not found", error.getMessage());
    }

    @Test
    public void testGetNoteFromUserValidNote() {
        User user = new User();
        user.setUsername("testuser2");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser2@example.com");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("Note Title");
        note.setText("Note Content");
        note.setUser(user);
        noteRepository.save(note);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        ResponseEntity<?> responseEntity = noteController.getNoteFromUser(authHeader, note.getId());
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof Note);
        Note returnedNote = (Note) body;
        assertEquals("Note Title", returnedNote.getTitle());
        assertEquals("Note Content", returnedNote.getText());
    }

    @Test
    public void testGetNoteFromUserNoteNotFound() {
        User user = new User();
        user.setUsername("testuser3");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser3@example.com");
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        long nonExistentNoteId = 999L;
        ResponseEntity<?> responseEntity = noteController.getNoteFromUser(authHeader, nonExistentNoteId);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(404, error.getStatus());
        assertEquals("This note doesn't exist", error.getMessage());
    }

    @Test
    public void testGetNoteFromUserInvalidHeader() {
        long dummyNoteId = 1L;
        String invalidHeader = "BadHeader";
        ResponseEntity<?> responseEntity = noteController.getNoteFromUser(invalidHeader, dummyNoteId);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(401, error.getStatus());
        assertEquals("Authorization header is invalid", error.getMessage());
    }

    @Test
    public void testCreateNoteValid() {

        User user = new User();
        user.setUsername("testuser4");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser4@example.com");
        userRepository.save(user);


        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;


        Note note = new Note();
        note.setTitle("New Note");
        note.setText("New note content");

        ResponseEntity<?> responseEntity = noteController.createNote(authHeader, note);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof Note);
        Note createdNote = (Note) body;
        assertNotNull(createdNote.getId());
        assertEquals("New Note", createdNote.getTitle());
        assertEquals("New note content", createdNote.getText());
        assertEquals(user.getUsername(), createdNote.getUser().getUsername());
    }

    @Test
    public void testCreateNoteInvalidHeader() {
        Note note = new Note();
        note.setTitle("Invalid Note");
        note.setText("Should not be created");
        String invalidHeader = "InvalidToken";

        ResponseEntity<?> responseEntity = noteController.createNote(invalidHeader, note);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(401, error.getStatus());
        assertEquals("Authorization header is invalid", error.getMessage());
    }

    @Test
    public void testCreateNoteUserNotFound() {
        String token = jwtUtils.generateToken("nonexistentUser");
        String authHeader = "Bearer " + token;

        Note note = new Note();
        note.setTitle("Note Title");
        note.setText("Note Content");

        ResponseEntity<?> responseEntity = noteController.createNote(authHeader, note);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(404, error.getStatus());
        assertEquals("User not found", error.getMessage());
    }

    @Test
    public void testDeleteNoteValid() {
        User user = new User();
        user.setUsername("testuser5");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser5@example.com");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("Note to Delete");
        note.setText("This note will be deleted.");
        note.setUser(user);
        noteRepository.save(note);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        ResponseEntity<?> responseEntity = noteController.deleteNote(authHeader, note.getId());
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        Optional<Note> deletedNote = noteRepository.findById(note.getId());
        assertFalse(deletedNote.isPresent());
    }

    @Test
    public void testDeleteNoteNoteNotFound() {
        User user = new User();
        user.setUsername("testuser6");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser6@example.com");
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        long nonExistentNoteId = 12345L;
        ResponseEntity<?> responseEntity = noteController.deleteNote(authHeader, nonExistentNoteId);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(404, error.getStatus());
        assertEquals("This note doesn't exist", error.getMessage());
    }

    @Test
    public void testDeleteNoteInvalidHeader() {
        long dummyNoteId = 1L;
        String invalidHeader = "BadToken";
        ResponseEntity<?> responseEntity = noteController.deleteNote(invalidHeader, dummyNoteId);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ErrorResponse);
        ErrorResponse error = (ErrorResponse) body;
        assertEquals(401, error.getStatus());
        assertEquals("Authorization header is invalid", error.getMessage());
    }
}
