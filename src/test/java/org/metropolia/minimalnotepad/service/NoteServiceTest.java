package org.metropolia.minimalnotepad.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
import org.metropolia.minimalnotepad.model.Category;
import org.metropolia.minimalnotepad.model.Note;
import org.metropolia.minimalnotepad.model.User;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        Note note1 = new Note();
        note1.setId(1);
        note1.setTitle("title1");
        note1.setUser(userMock);

        when(noteRepository.getNoteById(note1.getId())).thenReturn(note1);

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

        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);

        when(noteRepository.getNoteById(note.getId())).thenReturn(note);

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

        assertThrows(ResourceDoesntExistException.class , () -> {
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

        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);

        when(noteRepository.getNoteById(note.getId())).thenReturn(note);

        noteService.createNote(userMock, note);
        verify(noteRepository, times(1)).save(note);

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

        assertThrows(UserDoesntOwnResourceException.class,() ->noteService.createNote(note.getUser(), note));
    }
    @Test
    public void testDeleteNoteSuccess() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");

        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(userMock);

        noteService.deleteNote(userMock, note);
        verify(noteRepository, times(1)).delete(note);
    }
    @Test
    public void testDeleteNoteNoAuth(){
        Note note = new Note();
        note.setId(1);
        note.setTitle("title1");
        note.setUser(null);
        assertThrows(UserDoesntOwnResourceException.class,() -> noteService.deleteNote(null,note));
    }
    @Test
    public void testDeleteNoteFailure(){
        User userMock = new User();
        userMock.setId(1);
        userMock.setUsername("user1");
        userMock.setPassword("password1");
        userMock.setEmail("user1@email.com");
        assertThrows(ResourceDoesntExistException.class,() -> noteService.deleteNote(userMock,null));
    }
    @Test
   public void testFilterNotesSuccess(){
        Category category1 = new Category(), category2 = new Category();
        category1.setId(1);
        category1.setName("category1");
        category2.setId(2);
        category2.setName("category2");
        Note note1 = new Note();
        Note note2 = new Note();
        Note note3 = new Note();
        note1.setId(1);
        note1.setTitle("title1");
        note2.setId(2);
        note2.setTitle("title2");
        note3.setId(3);
        note3.setTitle("title3");

        ArrayList<Category> note1Categories = new ArrayList<>(),note2Categories = new ArrayList<>(),note3Categories = new ArrayList<>();
        note1Categories.add(category1);
        note1Categories.add(category2);
        note2Categories.add(category2);

        note1.setCategoriesList(note1Categories);
        note2.setCategoriesList(note2Categories);
        note3.setCategoriesList(note3Categories);

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);

        ArrayList<Note> category1Notes = noteService.filterNotes(notes,category1);
        assertTrue(category1Notes.get(0).getCategoriesList().contains(category1));
        assertEquals("title1", category1Notes.get(0).getTitle());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            category1Notes.get(1);
        });

        ArrayList<Note> category2Notes = noteService.filterNotes(notes,category2);
        assertTrue(category2Notes.get(0).getCategoriesList().contains(category2));
        assertEquals("title1", category2Notes.get(0).getTitle());
        assertTrue(category2Notes.get(1).getCategoriesList().contains(category2));
        assertEquals("title2", category2Notes.get(1).getTitle());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            category2Notes.get(2);
        });

        ArrayList<Note> noCategoryNotes = noteService.filterNotes(notes,null);
        assertTrue(noCategoryNotes.get(0).getCategoriesList().isEmpty());
        assertEquals("title3", noCategoryNotes.get(0).getTitle());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            noCategoryNotes.get(1);
        });

    }
    @Test
    public void testFilterNotesNoneFound(){
        Category category1 = new Category(), category2 = new Category();
        category1.setId(1);
        category1.setName("category1");
        category2.setId(2);
        category2.setName("category2");
        Note note1 = new Note();
        Note note2 = new Note();
        note1.setId(1);
        note1.setTitle("title1");
        note2.setId(2);
        note2.setTitle("title2");

        ArrayList<Category> note1Categories = new ArrayList<>(),note2Categories = new ArrayList<>();
        note1Categories.add(category1);
        note2Categories.add(category2);

        note1.setCategoriesList(note1Categories);
        note2.setCategoriesList(note2Categories);

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);

        ArrayList<Note> none = noteService.filterNotes(notes,null);
        assertTrue(none.isEmpty());
    }

}
