package org.metropolia.minimalnotepad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.metropolia.minimalnotepad.exception.ResourceDoesntExistException;
import org.metropolia.minimalnotepad.exception.UserDoesntOwnResourceException;
import org.metropolia.minimalnotepad.model.*;
import org.metropolia.minimalnotepad.repository.GroupRepository;
import org.metropolia.minimalnotepad.repository.NoteRepository;
import org.metropolia.minimalnotepad.utils.SearchUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private SearchUtils searchUtils = new SearchUtils();
    private Language defaultLanguage;
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private NoteService noteService;
    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        noteRepository.deleteAll();
        groupRepository.deleteAll();
        noteService = new NoteService(noteRepository, searchUtils, groupService);
        defaultLanguage = new Language();
        defaultLanguage.setId(1);
    }


    @Test
    public void testGetNotesListByUsersSuccess() {
        User userMock1 = new User(), userMock2 = new User();
        userMock1.setId(1);
        userMock1.setUsername("user1");
        userMock1.setPassword("password1");
        userMock1.setEmail("user1@email.com");
        userMock1.setLanguage(defaultLanguage);

        userMock2.setId(2);
        userMock2.setUsername("user2");
        userMock2.setPassword("password2");
        userMock2.setEmail("user2@email.com");
        userMock2.setLanguage(defaultLanguage);

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
        userMock.setLanguage(defaultLanguage);

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
        userMock.setLanguage(defaultLanguage);

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
        userMock.setLanguage(defaultLanguage);

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
        userMock.setLanguage(defaultLanguage);

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
    @Test
    public void testFindNotesSuccess(){
        Note note1 = new Note(), note2 = new Note(), note3 = new Note();
        note1.setTitle("titleCool");
        note2.setTitle("I love min");
        note3.setTitle("I love milk");
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);

        ArrayList<Note> foundNotes = noteService.findNotes(notes,"titleCool");

        assertNotNull(foundNotes);
        assertEquals(1, foundNotes.size());
        assertEquals("titleCool", foundNotes.get(0).getTitle());

        foundNotes = noteService.findNotes(notes,"I love mi");
        assertNotNull(foundNotes);
        assertEquals(2, foundNotes.size());
        assertEquals("I love min", foundNotes.get(0).getTitle());
        assertEquals("I love milk", foundNotes.get(1).getTitle());

    }
    @Test
    public void testFindNotesFailure(){
        Note note1 = new Note(), note2 = new Note(), note3 = new Note();
        note1.setTitle("titleCool");
        note2.setTitle("I love min");
        note3.setTitle("I love milk");
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
        notes.add(note3);
        ArrayList<Note> foundNotes = noteService.findNotes(notes,"non applicable");
        assertNotNull(foundNotes);
        assertTrue(foundNotes.isEmpty());
    }

    @Test
    public void testGetNotesFromGroups() {
        User user = new User();
        user.setId(1);
        user.setUsername("user1");
        user.setPassword("password1");
        user.setEmail("test@example.com");
        user.setLanguage(defaultLanguage);

        // Creating Group objects
        Group group1 = new Group();
        group1.setName("Group 1");
        group1.setUser(user);
        groupRepository.save(group1);
        Group group2 = new Group();
        group2.setName("Group 2");
        group2.setUser(user);
        groupRepository.save(group2);

        // Creating Note objects
        Note note1 = new Note();
        note1.setTitle("Note 1");
        note1.setGroup(group1);
        noteRepository.save(note1);
        Note note2 = new Note();
        note2.setTitle("Note 2");
        note2.setGroup(group2);
        noteRepository.save(note2);

        List<Note> notesList1 = new ArrayList<>();
        notesList1.add(note1);
        group1.setNotes(notesList1);

        List<Note> notesList2 = new ArrayList<>();
        notesList2.add(note2);
        group2.setNotes(notesList2);

        when(groupRepository.findAll()).thenReturn(Arrays.asList(group1, group2));

        // Creating a list of groups
        List<Group> groups = groupService.getAllGroups();

        // Calling the method under test
        List<Note> result = noteService.getNotesFromGroups(groups,user);

        // Assertions
        assertEquals(2, result.size());
        assertTrue(result.contains(note1));
        assertTrue(result.contains(note2));
    }
}
