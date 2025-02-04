package org.metropolia.minimalnotepad.service;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testGetNotesListByUsersSuccess()
    {
        User userMock1 = new User(), userMock2 = new User();
        userMock1.setId(1);
        userMock1.setUsername("user1");
        userMock1.setPassword("password1");
        userMock1.setEmail("user1@email.com");

        userMock2.setId(2);
        userMock2.setUsername("user2");
        userMock2.setPassword("password2");
        userMock2.setEmail("user2@email.com");

        userRepository.save(userMock1);
        userRepository.save(userMock2);

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
        noteRepository.save(note1);
        noteRepository.save(note2);
        noteRepository.save(note3);

        List<Note> result = noteService.getNoteListsByUser(userMock1), resultEmpty = noteService.getNoteListsByUser(userMock2);
        assertNotNull(result);
        assertNotNull(resultEmpty);

        assertEquals(3, result.size());
        assertTrue(resultEmpty.isEmpty());
    }
    @Test
    public void testGetNoteByIdByUserSuccess()
    {


    }
}
