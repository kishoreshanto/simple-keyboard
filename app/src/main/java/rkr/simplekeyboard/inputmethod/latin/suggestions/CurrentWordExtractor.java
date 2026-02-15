package rkr.simplekeyboard.inputmethod.latin.suggestions;

import rkr.simplekeyboard.inputmethod.latin.common.Constants;

public final class CurrentWordExtractor {
    private static final int CODE_RIGHT_SINGLE_QUOTE = 0x2019;

    private CurrentWordExtractor() {
    }

    public static CurrentWordInfo extractCurrentWord(final String textBeforeCursor,
            final String textAfterCursor) {
        if (textBeforeCursor == null || textBeforeCursor.isEmpty()) {
            return CurrentWordInfo.EMPTY;
        }
        if (startsInsideWord(textAfterCursor)) {
            return CurrentWordInfo.EMPTY;
        }

        int index = textBeforeCursor.length();
        while (index > 0) {
            final int codePoint = Character.codePointBefore(textBeforeCursor, index);
            if (!isWordCodePoint(codePoint)) {
                break;
            }
            index -= Character.charCount(codePoint);
        }

        if (index == textBeforeCursor.length()) {
            return CurrentWordInfo.EMPTY;
        }

        final String word = textBeforeCursor.substring(index);
        return new CurrentWordInfo(word, word.length());
    }

    private static boolean startsInsideWord(final String textAfterCursor) {
        if (textAfterCursor == null || textAfterCursor.isEmpty()) {
            return false;
        }
        final int codePointAfterCursor = Character.codePointAt(textAfterCursor, 0);
        return isWordCodePoint(codePointAfterCursor);
    }

    private static boolean isWordCodePoint(final int codePoint) {
        return Character.isLetter(codePoint)
                || codePoint == Constants.CODE_SINGLE_QUOTE
                || codePoint == CODE_RIGHT_SINGLE_QUOTE;
    }
}
