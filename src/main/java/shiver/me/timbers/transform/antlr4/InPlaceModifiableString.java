package shiver.me.timbers.transform.antlr4;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * A character sequence that can have
 *
 * @author Karl Bennett
 */
public class InPlaceModifiableString implements CharSequence {

    private final StringBuilder transformedMainString;

    private String originalSubString;
    private int currentStartIndex;
    private int oldOffset;
    private int currentOffset;
    private int actualStartIndex;
    private int actualStopIndex;

    public InPlaceModifiableString(String string) {

        assertIsNotNull(argumentIsNullMessage("string"), string);

        this.transformedMainString = new StringBuilder(string);

        // Set the initial current start index to -1 so that updatePresets(int,int,String) is called for the first word.
        this.currentStartIndex = -1;
    }

    /**
     * Set the new string for the substring within the supplied indices.
     */
    public void setSubstring(String string, int startIndex, int stopIndex) {

        if (isNewWord(getCurrentStartIndex(), startIndex)) {

            updatePresets(startIndex, stopIndex);

        } else {

            updateActualIndices(getOldOffset(), getCurrentOffset(), startIndex, stopIndex);
        }

        insertSubStingInToMainString(getActualStartIndex(), getActualStopIndex(), string);

        updateCurrentOffset(getOriginalSubString().length(), string.length());
    }

    private void updatePresets(int startIndex, int stopIndex) {

        setCurrentStartIndex(startIndex);

        updateOldOffset(getCurrentOffset());
        updateCurrentOffset(0, 0);

        updateActualIndices(getOldOffset(), getCurrentOffset(), startIndex, stopIndex);

        updateOriginalSubString(getActualStartIndex(), getActualStopIndex());
    }

    private void updateActualIndices(int oldOffset, int currentOffset, int startIndex, int stopIndex) {

        updateActualStartIndex(oldOffset, startIndex);
        updateActualStopIndex(oldOffset, currentOffset, stopIndex);
    }

    boolean isNewWord(int currentIndex, int startIndex) {

        return currentIndex < startIndex;
    }

    void updateOldOffset(int currentOffset) {

        oldOffset += currentOffset;
    }

    void updateActualStartIndex(final int oldOffset, final int startIndex) {

        actualStartIndex = oldOffset + startIndex;
    }

    void updateActualStopIndex(int oldOffset, int currentOffset, int stopIndex) {

        actualStopIndex = oldOffset + currentOffset + stopIndex;
    }

    void insertSubStingInToMainString(int actualStartIndex, int actualStopIndex, String transformedSubString) {

        transformedMainString.replace(actualStartIndex, actualStopIndex + 1, transformedSubString);
    }

    void updateCurrentOffset(int currentLength, int transformedLength) {

        currentOffset = transformedLength - currentLength;
    }

    void updateOriginalSubString(int startIndex, int stopIndex) {

        this.originalSubString = transformedMainString.substring(startIndex, stopIndex + 1);
    }

    private int getCurrentStartIndex() {

        return currentStartIndex;
    }

    String getOriginalSubString() {

        return originalSubString;
    }

    private void setCurrentStartIndex(int currentStartIndex) {

        this.currentStartIndex = currentStartIndex;
    }

    private int getOldOffset() {

        return oldOffset;
    }

    int getCurrentOffset() {

        return currentOffset;
    }

    int getActualStartIndex() {

        return actualStartIndex;
    }

    int getActualStopIndex() {

        return actualStopIndex;
    }

    @Override
    public int length() {

        return transformedMainString.length();
    }

    @Override
    public char charAt(int index) {

        return transformedMainString.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {

        return transformedMainString.subSequence(start, end);
    }

    @Override
    public String toString() {

        return transformedMainString.toString();
    }
}
