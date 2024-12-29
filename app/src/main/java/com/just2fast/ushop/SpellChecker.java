package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SpellChecker {
    private final ArrayList<String>dictionary = new ArrayList<>();

    Gson gson = new Gson();
    private final int MAX_LEVENSHTEIN_DISTANCE = 2; // Maximum allowed Levenshtein distance for suggestions

    public SpellChecker(Activity parent) {

        SharedPreferences sp1225 = parent.getSharedPreferences("Category List", Context.MODE_PRIVATE);
        String catString = sp1225.getString("Category List","null");
        FetchCategoryModel fetchCategoryModel = gson.fromJson(catString,FetchCategoryModel.class);

        dictionary.addAll(fetchCategoryModel.fashionCat);
        dictionary.addAll(fetchCategoryModel.groceryCat);
        dictionary.add("men");
        dictionary.add("man");
        dictionary.add("for");
        dictionary.add("women");
        dictionary.add("woman");
        dictionary.add("girl");
        dictionary.add("boy");
        dictionary.add("kid");
        dictionary.add("grey");
        dictionary.add("red");
        dictionary.add("green");


    }

   /* private void loadDictionary(List<String> correctWords) {
            for (String word : correctWords) {
                Log.d("word",word.toLowerCase());
                dictionary.add(word.toLowerCase());
            }
    }*/

    public boolean isWordCorrect(String word) {
        return dictionary.contains(word.toLowerCase());
    }

    public List<String> getSuggestions(String word) {
        List<String> suggestions = new ArrayList<>();
        for (String correctWord : dictionary) {
            int distance = computeLevenshteinDistance(word.toLowerCase(), correctWord);
            if (distance <= MAX_LEVENSHTEIN_DISTANCE) {
                suggestions.add(correctWord);
            }
        }
        return suggestions;
    }

    private int computeLevenshteinDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }
}
