package shiver.me.timbers.transform.antlr4;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.EIGHT;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.EIGHT_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.EIGHT_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.FIVE;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.FIVE_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.FIVE_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.FOUR;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.FOUR_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.FOUR_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.NINE;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.NINE_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.NINE_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.NUMBER_TRANSFORMATION_MAP;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.ODD_EVEN_TRANSFORMATION_MAP;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.ONE;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.ONE_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.ONE_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.SEVEN;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.SEVEN_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.SEVEN_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.SIX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.SIX_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.SIX_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TEN;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TEN_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TEN_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TEST_TEXT;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.THREE;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.THREE_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.THREE_STOP_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TRANSFORMED_SEVEN_TEXT;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TRANSFORMED_TEXT;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TWO;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TWO_START_INDEX;
import static shiver.me.timbers.transform.antlr4.InPlaceModifiableStringTestUtils.TWO_STOP_INDEX;

public class InPlaceModifiableStringTest {

    private InPlaceModifiableString inPlaceModifiableString;

    @Before
    public void setUp() {

        inPlaceModifiableString = new InPlaceModifiableString(TEST_TEXT);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNull() {

        new InPlaceModifiableString(null);
    }

    @Test
    public void testIsNewWordWithOldIndex() {

        assertFalse("initial index of 0 and new index of 0 should produce false.",
                inPlaceModifiableString.isNewWord(0, 0));
    }

    @Test
    public void testIsNewWordWithNewIndex() {

        assertTrue("initial index of 0 and index of 1 should produce true.", inPlaceModifiableString.isNewWord(0, 1));
    }

    @Test
    public void testIsNewWordWithNegativeIndex() {

        assertFalse("initial index of 0 and negative index should produce false.",
                inPlaceModifiableString.isNewWord(0, -1));
        assertFalse("initial index of 0 and negative index should produce false.",
                inPlaceModifiableString.isNewWord(0, -2));
        assertFalse("initial index of 0 and negative index should produce false.",
                inPlaceModifiableString.isNewWord(0, -3));
        assertFalse("initial index of 0 and negative index should produce false.",
                inPlaceModifiableString.isNewWord(0, -5));
    }

    @Test
    public void testUpdateCurrentOffsetForLongerWord() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH + 5);

        assertEquals("offset should increase", 5, inPlaceModifiableString.getCurrentOffset());
    }

    @Test
    public void testUpdateCurrentOffsetForMultipleLongerWords() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH + 5);
        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH + 4);
        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH + 3);

        assertEquals("offset should increase", 3, inPlaceModifiableString.getCurrentOffset());
    }

    @Test
    public void testUpdateCurrentOffsetForSameWord() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH);

        assertEquals("offset should stay the same", 0, inPlaceModifiableString.getCurrentOffset());
    }

    @Test
    public void testUpdateCurrentOffsetForMultipleSameWords() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH);
        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH);
        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH);

        assertEquals("offset should stay the same", 0, inPlaceModifiableString.getCurrentOffset());
    }

    @Test
    public void testUpdateCurrentOffsetForShorterWord() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH - 3);

        assertEquals("offset should decrease", -3, inPlaceModifiableString.getCurrentOffset());
    }

    @Test
    public void testUpdateCurrentOffsetForMultipleShorterWords() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH - 1);
        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH - 2);
        inPlaceModifiableString.updateCurrentOffset(LENGTH, LENGTH - 3);

        assertEquals("offset should decrease", -3, inPlaceModifiableString.getCurrentOffset());
    }

    @Test
    public void testUpdateOriginalSubstring() {

        inPlaceModifiableString.updateOriginalSubString(ONE_START_INDEX, ONE_STOP_INDEX);

        assertEquals("the original substring should be set correctly.", ONE,
                inPlaceModifiableString.getOriginalSubString());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testUpdateOriginalSubstringWithInverseIndices() {

        inPlaceModifiableString.updateOriginalSubString(ONE_STOP_INDEX, ONE_START_INDEX);
    }

    @Test
    public void testUpdateActualStartIndexWithNoOffset() {

        inPlaceModifiableString.updateActualStartIndex(0, ONE_START_INDEX);

        assertEquals("with no offset the current and actual start indices should be equal.",
                ONE_START_INDEX, inPlaceModifiableString.getActualStartIndex());
    }

    @Test
    public void testUpdateActualStartIndexWithPositiveOffset() {

        final int OLD_OFFSET = 5;

        inPlaceModifiableString.updateActualStartIndex(OLD_OFFSET, TWO_START_INDEX);

        assertEquals("with a positive offset the actual start index should be equal to the current index plus the " +
                "old offset.", TWO_START_INDEX + OLD_OFFSET, inPlaceModifiableString.getActualStartIndex());
    }

    @Test
    public void testUpdateActualStartIndexWithNegativeOffset() {

        final int OLD_OFFSET = -5;

        inPlaceModifiableString.updateActualStartIndex(OLD_OFFSET, TWO_START_INDEX);

        assertEquals("with a negative offset the actual start index should be equal to the current index plus the " +
                "old offset.", TWO_START_INDEX + OLD_OFFSET, inPlaceModifiableString.getActualStartIndex());
    }

    @Test
    public void testUpdateActualStartIndexWithNegativeOffsetAndZeroCurrentIndex() {

        inPlaceModifiableString.updateActualStartIndex(-5, 0);

        assertThat("a negative offset and a current index of 0 should not a negative actual index.",
                inPlaceModifiableString.getActualStartIndex(), lessThan(0));
    }

    @Test
    public void testUpdateActualStartIndexWithNegativeCurrentIndex() {

        inPlaceModifiableString.updateActualStartIndex(0, -5);

        assertThat("a negative current index should produce a negative actual index.",
                inPlaceModifiableString.getActualStartIndex(), lessThan(0));
    }

    @Test
    public void testUpdateActualStopIndexWithNoOffset() {

        final int LENGTH = ONE.length();

        inPlaceModifiableString.updateActualStopIndex(0, 0, LENGTH);

        assertEquals("with no offset the current and actual stop indices should be equal.",
                LENGTH, inPlaceModifiableString.getActualStopIndex());
    }

    @Test
    public void testUpdateActualStopIndexWithPositiveOffset() {

        final int LENGTH = ONE.length();
        final int OLD_OFFSET = 5;
        final int CURRENT_OFFSET = 5;

        inPlaceModifiableString.updateActualStopIndex(OLD_OFFSET, CURRENT_OFFSET, LENGTH);

        assertEquals("with a positive offset the actual stop index should be equal to the current index plus the " +
                "offset.", OLD_OFFSET + CURRENT_OFFSET + LENGTH, inPlaceModifiableString.getActualStopIndex());
    }

    @Test
    public void testUpdateActualStopIndexWithNegativeOffset() {

        final int LENGTH = ONE.length();
        final int OLD_OFFSET = -5;
        final int CURRENT_OFFSET = -5;

        inPlaceModifiableString.updateActualStopIndex(OLD_OFFSET, CURRENT_OFFSET, LENGTH);

        assertEquals("with a negative offset the actual stop index should be equal to the current index plus the " +
                "offset.", OLD_OFFSET + CURRENT_OFFSET + LENGTH, inPlaceModifiableString.getActualStopIndex());
    }

    @Test
    public void testUpdateActualStopIndexWithNegativeOffsetAndZeroCurrentIndex() {

        inPlaceModifiableString.updateActualStopIndex(-5, -5, 0);

        assertThat("a negative offset and a current index of 0 should produce a negative actual index.",
                inPlaceModifiableString.getActualStopIndex(), lessThan(0));
    }

    @Test
    public void testUpdateActualStopIndexWithNegativeCurrentIndex() {

        inPlaceModifiableString.updateActualStopIndex(0, 0, -5);

        assertThat("a negative current index should produce a negative actual index.",
                inPlaceModifiableString.getActualStopIndex(), lessThan(0));
    }

    @Test
    public void testApplyTransformationToMainStringWithNoOffset() {

        inPlaceModifiableString.insertSubStingInToMainString(ONE_START_INDEX, ONE_STOP_INDEX, ONE + "12345");

        assertEquals("one12345 two three four five six seven eight nine ten", inPlaceModifiableString.toString());
    }

    @Test
    public void testApplyTransformationToMainStringWithPositiveCurrentOffset() {

        inPlaceModifiableString.insertSubStingInToMainString(ONE_START_INDEX, ONE_STOP_INDEX, ONE + "123");
        inPlaceModifiableString.insertSubStingInToMainString(ONE_START_INDEX, ONE_STOP_INDEX + 3, ONE + "12345");

        assertEquals("one12345 two three four five six seven eight nine ten", inPlaceModifiableString.toString());
    }

    @Test
    public void testApplyTransformationToMainStringWithPositiveOldOffset() {

        inPlaceModifiableString.insertSubStingInToMainString(ONE_START_INDEX, ONE_STOP_INDEX, ONE + "12345");
        inPlaceModifiableString.insertSubStingInToMainString(TWO_START_INDEX + 5, TWO_STOP_INDEX + 5, TWO + "123");

        assertEquals("one12345 two123 three four five six seven eight nine ten", inPlaceModifiableString.toString());
    }

    @Test
    public void testApplyTransformationToMainStringWithPositiveOldOffsetAndNegativeCurrentOffset() {

        inPlaceModifiableString.insertSubStingInToMainString(ONE_START_INDEX, ONE_STOP_INDEX, ONE + "12345");
        inPlaceModifiableString.insertSubStingInToMainString(TWO_START_INDEX + 5, TWO_STOP_INDEX + 5, TWO + "123");
        inPlaceModifiableString.insertSubStingInToMainString(TWO_START_INDEX + 5, TWO_STOP_INDEX + 8, TWO + "1");

        assertEquals("one12345 two1 three four five six seven eight nine ten", inPlaceModifiableString.toString());
    }

    @Test
    public void testTransformSubstringWithTransformationsTwo() {

        applyTransformation(SEVEN_START_INDEX, SEVEN_STOP_INDEX, SEVEN);

        assertEquals("transformed string should only contain a modified seven.", TRANSFORMED_SEVEN_TEXT,
                inPlaceModifiableString.toString());
    }

    @Test
    public void testTransformSubstringWithTransformationsOnAllNumbers() {

        applyTransformation(ONE_START_INDEX, ONE_STOP_INDEX, ONE);
        applyTransformation(TWO_START_INDEX, TWO_STOP_INDEX, TWO);
        applyTransformation(THREE_START_INDEX, THREE_STOP_INDEX, THREE);
        applyTransformation(FOUR_START_INDEX, FOUR_STOP_INDEX, FOUR);
        applyTransformation(FIVE_START_INDEX, FIVE_STOP_INDEX, FIVE);
        applyTransformation(SIX_START_INDEX, SIX_STOP_INDEX, SIX);
        applyTransformation(SEVEN_START_INDEX, SEVEN_STOP_INDEX, SEVEN);
        applyTransformation(EIGHT_START_INDEX, EIGHT_STOP_INDEX, EIGHT);
        applyTransformation(NINE_START_INDEX, NINE_STOP_INDEX, NINE);
        applyTransformation(TEN_START_INDEX, TEN_STOP_INDEX, TEN);


        assertEquals("the transformed string should be correct.", TRANSFORMED_TEXT,
                inPlaceModifiableString.toString());
    }

    @Test
    public void testTransformSubstringWithTransformationsWithPreviousNumber() {

        inPlaceModifiableString.setSubstring("1", ONE_START_INDEX, ONE_STOP_INDEX);
        inPlaceModifiableString.setSubstring("2", TWO_START_INDEX, TWO_STOP_INDEX);
        inPlaceModifiableString.setSubstring("3", THREE_START_INDEX, THREE_STOP_INDEX);
        inPlaceModifiableString.setSubstring("this should not be applied", TWO_START_INDEX, TWO_STOP_INDEX);
        inPlaceModifiableString.setSubstring("4", FOUR_START_INDEX, FOUR_STOP_INDEX);

        assertEquals("the transformed string should be correct.", "1 2 3 4 five six seven eight nine ten",
                inPlaceModifiableString.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTransformSubstringWithNullTransformation() {

        inPlaceModifiableString.setSubstring(null, ONE_START_INDEX, ONE_START_INDEX);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTransformSubstringWithInverseIndices() {

        inPlaceModifiableString.setSubstring(wrapWithNumber(ONE), ONE_STOP_INDEX, ONE_START_INDEX);
    }

    @Test
    public void testLength() {

        assertEquals("the initial length should be the same as the wrapped string.", TEST_TEXT.length(),
                inPlaceModifiableString.length());
    }

    @Test
    public void testLengthAfterTransformation() {

        inPlaceModifiableString.setSubstring(wrapWithNumber(ONE), ONE_START_INDEX, ONE_STOP_INDEX);

        assertThat("the length should be different to the wrapped string after a transformation.",
                inPlaceModifiableString.length(), not(TEST_TEXT.length()));
    }

    @Test
    public void testCharAt() {

        final int INDEX = 20;

        assertEquals("char should be the same as the wrapped string.", TEST_TEXT.charAt(INDEX),
                inPlaceModifiableString.charAt(INDEX));
    }

    @Test
    public void testCharAtAfterTransformation() {

        inPlaceModifiableString.setSubstring(wrapWithNumber(ONE), ONE_START_INDEX, ONE_STOP_INDEX);

        final int INDEX = 20;

        assertThat("char should be different to the wrapped string after a transformation.",
                inPlaceModifiableString.charAt(INDEX), not(TEST_TEXT.charAt(INDEX)));
    }

    @Test
    public void testSubString() {

        final int START = 10;
        final int END = 20;

        assertEquals("sub-sequence should be the same as the wrapped string.", TEST_TEXT.subSequence(START, END),
                inPlaceModifiableString.subSequence(START, END));
    }

    @Test
    public void testSubStringAfterTransformation() {

        inPlaceModifiableString.setSubstring(wrapWithNumber(ONE), ONE_START_INDEX, ONE_STOP_INDEX);

        final int START = 10;
        final int END = 20;

        assertThat("sub-sequence should be different to the wrapped string after a transformation.",
                inPlaceModifiableString.subSequence(START, END), not(TEST_TEXT.subSequence(START, END)));
    }

    private void applyTransformation(int startIndex, int stopIndex, String substring) {

        final String numberWrappedString = wrapWithNumber(substring);
        inPlaceModifiableString.setSubstring(numberWrappedString, startIndex, stopIndex);

        final String oddEvenWrappedString = wrap(numberWrappedString, ODD_EVEN_TRANSFORMATION_MAP.get(substring));
        inPlaceModifiableString.setSubstring(oddEvenWrappedString, startIndex, stopIndex);

        final String digitWrappedString = wrap(oddEvenWrappedString, NUMBER_TRANSFORMATION_MAP.get(substring));
        inPlaceModifiableString.setSubstring(digitWrappedString, startIndex, stopIndex);
    }

    private static String wrapWithNumber(String string) {

        return wrap(string, "number");
    }

    private static String wrap(String string, String wrapping) {

        return '[' + wrapping + ']' + string + '[' + wrapping + ']';
    }
}
