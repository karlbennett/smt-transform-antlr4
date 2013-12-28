package shiver.me.timbers.transform.antlr4;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public final class InPlaceModifiableStringTestUtils {

    private InPlaceModifiableStringTestUtils() {
    }

    public static final String ONE = "one";
    public static final String TWO = "two";
    public static final String THREE = "three";
    public static final String FOUR = "four";
    public static final String FIVE = "five";
    public static final String SIX = "six";
    public static final String SEVEN = "seven";
    public static final String EIGHT = "eight";
    public static final String NINE = "nine";
    public static final String TEN = "ten";

    public static final String TEST_TEXT = ONE + " " + TWO + " " + THREE + " " + FOUR + " " + FIVE + " " + SIX + " " +
            SEVEN + " " + EIGHT + " " + NINE + " " + TEN;
    public static final String TRANSFORMED_SEVEN_TEXT = ONE + " " + TWO + " " + THREE + " " + FOUR + " " + FIVE + " " +
            SIX + " [7][odd][number]" + SEVEN + "[number][odd][7] " + EIGHT + " " + NINE + " " + TEN;

    public static final String TRANSFORMED_TEXT =
            "[1][odd][number]" + ONE + "[number][odd][1] " +
                    "[2][even][number]" + TWO + "[number][even][2] " +
                    "[3][odd][number]" + THREE + "[number][odd][3] " +
                    "[4][even][number]" + FOUR + "[number][even][4] " +
                    "[5][odd][number]" + FIVE + "[number][odd][5] " +
                    "[6][even][number]" + SIX + "[number][even][6] " +
                    "[7][odd][number]" + SEVEN + "[number][odd][7] " +
                    "[8][even][number]" + EIGHT + "[number][even][8] " +
                    "[9][odd][number]" + NINE + "[number][odd][9] " +
                    "[10][even][number]" + TEN + "[number][even][10]";

    public static final int ONE_START_INDEX = 0;
    public static final int ONE_STOP_INDEX = 2;

    public static final int TWO_START_INDEX = 4;
    public static final int TWO_STOP_INDEX = 6;

    public static final int THREE_START_INDEX = 8;
    public static final int THREE_STOP_INDEX = 12;

    public static final int FOUR_START_INDEX = 14;
    public static final int FOUR_STOP_INDEX = 17;

    public static final int FIVE_START_INDEX = 19;
    public static final int FIVE_STOP_INDEX = 22;

    public static final int SIX_START_INDEX = 24;
    public static final int SIX_STOP_INDEX = 26;

    public static final int SEVEN_START_INDEX = 28;
    public static final int SEVEN_STOP_INDEX = 32;

    public static final int EIGHT_START_INDEX = 34;
    public static final int EIGHT_STOP_INDEX = 38;

    public static final int NINE_START_INDEX = 40;
    public static final int NINE_STOP_INDEX = 43;

    public static final int TEN_START_INDEX = 45;
    public static final int TEN_STOP_INDEX = 47;

    public static final Map<String, String> NUMBER_TRANSFORMATION_MAP = unmodifiableMap(
            new HashMap<String, String>() {{
                put(ONE, "1");
                put(TWO, "2");
                put(THREE, "3");
                put(FOUR, "4");
                put(FIVE, "5");
                put(SIX, "6");
                put(SEVEN, "7");
                put(EIGHT, "8");
                put(NINE, "9");
                put(TEN, "10");
            }}
    );

    public static final Map<String, String> ODD_EVEN_TRANSFORMATION_MAP = unmodifiableMap(
            new HashMap<String, String>() {{
                put(ONE, "odd");
                put(TWO, "even");
                put(THREE, "odd");
                put(FOUR, "even");
                put(FIVE, "odd");
                put(SIX, "even");
                put(SEVEN, "odd");
                put(EIGHT, "even");
                put(NINE, "odd");
                put(TEN, "even");
            }}
    );
}
