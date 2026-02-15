package rkr.simplekeyboard.inputmethod.latin.suggestions;

public final class CurrentWordInfo {
    public static final CurrentWordInfo EMPTY = new CurrentWordInfo("", 0);

    public final String mWord;
    public final int mCharLength;

    CurrentWordInfo(final String word, final int charLength) {
        mWord = word;
        mCharLength = charLength;
    }

    public boolean hasWord() {
        return !mWord.isEmpty() && mCharLength > 0;
    }
}
