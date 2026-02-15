package rkr.simplekeyboard.inputmethod.latin.suggestions;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class AssetDictionarySuggestionEngine implements SuggestionEngine {
    private static final String TAG = AssetDictionarySuggestionEngine.class.getSimpleName();

    private final Context mContext;
    private final String mAssetPath;
    private volatile List<String> mDictionary;

    public AssetDictionarySuggestionEngine(final Context context, final String assetPath) {
        mContext = context;
        mAssetPath = assetPath;
    }

    @Override
    public List<String> getSuggestions(final String typedWord, final int maxSuggestions) {
        if (TextUtils.isEmpty(typedWord) || maxSuggestions <= 0) {
            return Collections.emptyList();
        }

        final String typedWordLowerCase = typedWord.toLowerCase(Locale.ROOT);
        final List<String> dictionary = getDictionary();
        if (dictionary.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> suggestions = new ArrayList<>(maxSuggestions);
        for (final String dictionaryWord : dictionary) {
            if (!dictionaryWord.startsWith(typedWordLowerCase)
                    || dictionaryWord.equals(typedWordLowerCase)) {
                continue;
            }
            suggestions.add(dictionaryWord);
            if (suggestions.size() >= maxSuggestions) {
                break;
            }
        }
        return suggestions;
    }

    private List<String> getDictionary() {
        List<String> dictionary = mDictionary;
        if (dictionary != null) {
            return dictionary;
        }
        synchronized (this) {
            dictionary = mDictionary;
            if (dictionary == null) {
                dictionary = loadDictionary();
                mDictionary = dictionary;
            }
        }
        return dictionary;
    }

    private List<String> loadDictionary() {
        final List<String> words = new ArrayList<>();
        try (InputStream inputStream = mContext.getAssets().open(mAssetPath);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String normalizedWord = line.trim().toLowerCase(Locale.ROOT);
                if (!normalizedWord.isEmpty()) {
                    words.add(normalizedWord);
                }
            }
        } catch (final IOException exception) {
            Log.e(TAG, "Unable to load dictionary asset: " + mAssetPath, exception);
            return Collections.emptyList();
        }
        return words;
    }
}
