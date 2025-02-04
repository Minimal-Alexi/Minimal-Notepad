package org.metropolia.minimalnotepad.service;

import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    public void testGetNotesListByUsersSuccess() {
        User userMock1 = new User(), userMock2 = new User();
        userMock1.setId(1);
        userMock1.setUsername("user1");
        userMock1.setPassword("password1");
        userMock1.setEmail("user1@email.com");

        userMock2.setId(2);
        userMock2.setUsername("user2");
        userMock2.setPassword("password2");
        userMock2.setEmail("user2@email.com");

        Note note1 = new Note(),note2 = new Note(),note3 = new Note();
        note1.setId(1);
        note1.setTitle("title1");
        note1.setUser(userMock1);
        note2.setId(2);
        note2.setTitle("title2");
        note2.setUser(userMock1);
        note3.setId(3);
        note3.setTitle("title3");
        note3.setUser(userMock1);

        when(noteRepository.getNotesByUserId(userMock1.getId())).thenReturn(Arrays.asList(note1, note2, note3));
        when(noteRepository.getNotesByUserId(userMock2.getId())).thenReturn(new ArrayList<>());

        List<Note> result = noteService.getNoteListsByUser(userMock1), resultEmpty = noteService.getNoteListsByUser(userMock2);
        assertNotNull(result);
        assertNotNull(resultEmpty);

        assertEquals(3, result.size());
        assertTrue(resultEmpty.isEmpty());
    }
    @Test
    public void testGetNoteByIdSuccess() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        userRepository.save(userMock);

        Note note1 = new Note();
        note1.setId(1);
        note1.setTitle("title1");
        note1.setUser(userMock);

        noteRepository.save(note1);

        Note resultNote = noteService.getNoteById(userMock, note1.getId());
        assertNotNull(resultNote);
        assertEquals("title1", resultNote.getTitle());
        assertEquals(userMock, resultNote.getUser());
    }
    @Test
    public void testGetNotesByIdNoAuth() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        userRepository.save(userMock);

        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);
        noteRepository.save(note);

        assertThrows(UserDoesntOwnResourceException.class, () -> {
            noteService.getNoteById(null, note.getId());
        });
    }
    @Test
    public void testGetNotesByIdFailure() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        userRepository.save(userMock);

        assertThrows(UserDoesntOwnResourceException.class , () -> {
            noteService.getNoteById(userMock, 1);
        });
    }
    @Test
    public void testCreateNoteSuccess() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        userRepository.save(userMock);

        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);

        assertTrue(noteService.createNote(userMock, note));
        Note result = noteService.getNoteById(userMock, note.getId());
        assertNotNull(result);
        assertEquals("title1", result.getTitle());
        assertEquals(userMock, result.getUser());
    }
    @Test
    public void testCreateNoteFailure(){
        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(null);

        assertFalse(noteService.createNote(note.getUser(), note));
    }
    @Test
    public void testDeleteNoteSuccess() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        userRepository.save(userMock);

        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);
        noteRepository.save(note);

        assertTrue(noteService.deleteNote(userMock, note));
    }
    @Test
    public void testDeleteNoteNoAuth(){
        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(null);
        assertFalse(noteService.deleteNote(null,note));
    }
    @Test
    public void testDeleteNoteFailure(){
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);
        assertFalse(noteService.deleteNote(null, note));
    }

}
