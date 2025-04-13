package org.metropolia.minimalnotepad.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * The type Search utils.
 */
@Component
public class SearchUtils {
    /**
     * Instantiates a new Search utils.
     */
    public SearchUtils() {

    }

    /**
     * Search text array list.
     *
     * @param listOfStringsToSearch the list of strings to search
     * @param searchText            the search text
     * @return the array list
     */
    public ArrayList<Integer> searchText(ArrayList<String> listOfStringsToSearch, String searchText) {
        ArrayList<Integer> stringPositions = new ArrayList<>();
        searchText = searchText.toLowerCase();
        for (int i = 0; i < listOfStringsToSearch.size(); i++) {
            if (listOfStringsToSearch.get(i).toLowerCase().contains(searchText) || fuzzySearch(listOfStringsToSearch.get(i).toLowerCase(), searchText)) {
                stringPositions.add(i);
            }
        }
        return stringPositions;
    }
    private boolean fuzzySearch(String text1, String text2) {
        int[][] distMatrix = new int[text1.length() + 1][text2.length() + 1];
        final int THRESHOLD = (text1.length() + text2.length() / 2) * 20 / 100;

        for (int i = 0; i <= text1.length(); i++) {
            distMatrix[i][0] = i;
        }

        for (int i = 0; i <= text2.length(); i++) {
            distMatrix[0][i] = i;
        }

        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                int cost = (text1.charAt(i - 1) == text2.charAt(j - 1)) ? 0 : 1;
                distMatrix[i][j] = Math.min(Math.min(distMatrix[i - 1][j] + 1, distMatrix[i][j - 1] + 1), distMatrix[i - 1][j - 1] + cost);
            }
        }

        int distance = distMatrix[text1.length()][text2.length()];

        if (distance > THRESHOLD) {
            return false;
        }
        return true;
    }
}
