package rkr.simplekeyboard.inputmethod.latin.suggestions;

import java.util.List;

public interface SuggestionEngine {
    List<String> getSuggestions(String typedWord, int maxSuggestions);
}
