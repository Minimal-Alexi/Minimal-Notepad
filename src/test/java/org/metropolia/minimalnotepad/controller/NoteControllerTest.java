package org.metropolia.minimalnotepad.controller;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metropolia.minimalnotepad.dto.ErrorResponse;
import org.metropolia.minimalnotepad.dto.SearchRequest;
import org.metropolia.minimalnotepad.model.Group;
import org.metropolia.minimalnotepad.model.Language;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.*;
import org.metropolia.minimalnotepad.service.UserGroupParticipationService;
import org.metropolia.minimalnotepad.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NoteControllerTest {
    private Language language;

    @Autowired
    private NoteController noteController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserGroupParticipationService userGroupParticipationService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setup() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
    }

    @BeforeEach
    public void setUp() {
        noteRepository.deleteAll();
        userRepository.deleteAll();
        languageRepository.deleteAll();
        language = new Language();
        language.setName("en");
        language.setCountry("US");
        languageRepository.saveAndFlush(language);
    }
    @Transactional
    @Test
    public void testGetAllNotesFromUserValidUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser@example.com");
        user.setLanguage(language);
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("Test Note");
        note.setText("This is a test note.");
        note.setUser(user);
        note.setCategoriesList(new ArrayList<>());
        Hibernate.initialize(note.getCategoriesList());
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
    @Transactional
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
        note.setCategoriesList(new ArrayList<>());
        Hibernate.initialize(note.getCategoriesList());
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
    @Transactional
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
        note.setCategoriesList(new ArrayList<>());
        Hibernate.initialize(note.getCategoriesList());
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

    @Test
    public void testUpdateNoteValid() {
        User user = new User();
        user.setUsername("testuser7");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser@example.com");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("Original Title");
        note.setText("Original text.");
        note.setUser(user);
        noteRepository.save(note);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        Note updatedNote = new Note();
        updatedNote.setUser(user);
        updatedNote.setTitle("Updated Title");
        updatedNote.setText("Updated text.");



        ResponseEntity<?> responseEntity = noteController.updateNote(authHeader, note.getId(), updatedNote);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof Note);
        Note returnedNote = (Note) body;
        assertEquals("Updated Title", returnedNote.getTitle());
        assertEquals("Updated text.", returnedNote.getText());
    }

    @Test
    public void testUpdateNoteUserNotFound() {
        String token = jwtUtils.generateToken("nonexistent");
        String authHeader = "Bearer " + token;

        Note updatedNote = new Note();
        updatedNote.setTitle("New Title");
        updatedNote.setText("New text.");

        ResponseEntity<?> responseEntity = noteController.updateNote(authHeader, 1L, updatedNote);
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
    public void testUpdateNoteNoteNotFound() {
        User user = new User();
        user.setUsername("testuser2");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser2@example.com");
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername());
        String authHeader = "Bearer " + token;

        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Title");
        updatedNote.setText("Updated text.");

        long nonExistentNoteId = 999L;
        ResponseEntity<?> responseEntity = noteController.updateNote(authHeader, nonExistentNoteId, updatedNote);
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
    public void testUpdateNoteInvalidHeader() {
        long dummyNoteId = 1L;
        String invalidHeader = "InvalidToken";
        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Title");
        updatedNote.setText("Updated text.");

        ResponseEntity<?> responseEntity = noteController.updateNote(invalidHeader, dummyNoteId, updatedNote);
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
    public void testGetSearchedNotesSuccess(){
        Note note1 = new Note(), note2 = new Note(), note3 = new Note();
        note1.setTitle("titleCool");
        note2.setTitle("I love min");
        note3.setTitle("I love milk");
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);
        String query1 = "titleCool", query2 = "I love mi";
        ResponseEntity<?> responseEntity = noteController.searchNote("non-applicable",new SearchRequest(query1,notes));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Object body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ArrayList<?>);
        ArrayList<Note> returnedNotes = (ArrayList<Note>) body;
        assertEquals(returnedNotes.size(), 1);

        responseEntity = noteController.searchNote("non-applicable",new SearchRequest(query2,notes));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body instanceof ArrayList<?>);
        returnedNotes = (ArrayList<Note>) body;
        assertEquals(returnedNotes.size(), 2);
    }
    @Test
    public void testGetSearchedNotesNoneFound(){
        Note note1 = new Note(), note2 = new Note(), note3 = new Note();
        note1.setTitle("titleCool");
        note2.setTitle("I love min");
        note3.setTitle("I love milk");
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);

        ResponseEntity<?> responseEntity = noteController.searchNote("non-applicable",new SearchRequest("non-applicable",notes));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    @Test
    public void testGetSearchedNotesBadQuery(){
        ResponseEntity<?> responseEntity = noteController.searchNote("non-applicable",new SearchRequest(null,null));
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllNotesFromUserGroups() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("testuser@example.com");
        user = userRepository.save(user);

        Group group = new Group();
        group.setName("Test Group");
        group.setUser(user);
        groupRepository.save(group);

        Note userNote = new Note();
        userNote.setTitle("User Note");
        userNote.setText("This is a user note.");
        userNote.setUser(user);
        userNote.setGroup(group);
        noteRepository.save(userNote);

        User testUser = new User();
        testUser.setUsername("testuser2");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setEmail("testuser2@example.com");
        testUser = userRepository.save(testUser);

        Group newGroup = new Group();
        newGroup.setName("Test Group 2");
        newGroup.setUser(testUser);
        groupRepository.save(newGroup);

        Note groupNote = new Note();
        groupNote.setTitle("Group Note");
        groupNote.setText("This is a group note.");
        groupNote.setUser(testUser);
        groupNote.setGroup(newGroup);
        noteRepository.save(groupNote);

        userGroupParticipationService.joinGroup(user.getId(), newGroup.getId());

        String token = jwtUtils.generateToken(user.getUsername());

        mockMvc.perform(get("/api/note/my-groups")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("User Note"))
                .andExpect(jsonPath("$[1].title").value("Group Note"));
    }
}
