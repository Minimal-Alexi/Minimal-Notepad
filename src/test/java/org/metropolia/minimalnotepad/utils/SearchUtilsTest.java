package org.metropolia.minimalnotepad.utils;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SearchUtilsTest {
    SearchUtils searchUtils = new SearchUtils();
    @Test
    public void testSearchFunctionSuccess(){
        ArrayList<String> searchList = new ArrayList<>();
        searchList.add("My Summer Car");
        searchList.add("Summer Car");
        searchList.add("I love bananas");
        searchList.add("Coca cola espuma");
        String searchTerm1 = "Summer Car", searchTerm2 = "Coca cola";

        ArrayList<Integer> foundResults = searchUtils.searchText(searchList, searchTerm1);
        assertNotNull(foundResults);
        assertEquals("My Summer Car", searchList.get(foundResults.get(0)));
        assertEquals("Summer Car", searchList.get(foundResults.get(1)));
        assertThrows(IndexOutOfBoundsException.class, () ->
        {
            foundResults.get(2);
        });

        ArrayList<Integer> foundResults2 = searchUtils.searchText(searchList, searchTerm2);
        assertNotNull(foundResults2);
        assertEquals("Coca cola espuma", searchList.get(foundResults2.get(0)));
        assertThrows(IndexOutOfBoundsException.class, () ->
        {
            foundResults2.get(2);
        });
    }
    @Test
    public void testSearchFunctionFailure(){
        ArrayList<String> searchList = new ArrayList<>();
        searchList.add("My Summer Car");
        searchList.add("Summer Car");
        searchList.add("I love bananas");

        ArrayList<Integer> foundResults = searchUtils.searchText(searchList, "Coca cola espuma");
        assertNotNull(foundResults);
        assertTrue(foundResults.isEmpty());
    }
}
