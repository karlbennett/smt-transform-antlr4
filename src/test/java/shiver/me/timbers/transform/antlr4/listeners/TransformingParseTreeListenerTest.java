package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.Recognizer;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.InPlaceModifiableString;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_NAME_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_NAME_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_TYPE_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_NAME_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_NAME_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_TYPE_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockErrorNode;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockErrorNodeWithDefaultToken;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockParserRuleContext;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockParserRuleContextWithDefaultToken;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockRecognizer;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockTerminalNode;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockTerminalNodeWithDefaultToken;

public class TransformingParseTreeListenerTest {

    private static final String TEST_STRING = "A test string with lots of text.";
    private static final int CALLED_ONCE = 1;
    private static final int CALLED_TWICE = 2;
    private static final int CALLED_FOUR_TIMES = 4;

    private Recognizer recognizer;
    private Transformations<TokenTransformation> transformations;
    private Transformations<TokenTransformation> parentTransformations;
    private InPlaceModifiableString inPlaceModifiableString;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        recognizer = mockRecognizer();

        final TokenTransformation transformation = mock(TokenTransformation.class);

        transformations = mock(Transformations.class);
        when(transformations.get(anyInt())).thenReturn(transformation);
        when(transformations.get(anyString())).thenReturn(transformation);

        parentTransformations = mock(Transformations.class);
        when(parentTransformations.get(anyInt())).thenReturn(transformation);
        when(parentTransformations.get(anyString())).thenReturn(transformation);

        inPlaceModifiableString = mock(InPlaceModifiableString.class);
    }

    @Test
    public void testCreateWithMinimalDependencies() {

        new TransformingParseTreeListener(recognizer, transformations, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithMinimalDependenciesAndNullRecognizer() {

        new TransformingParseTreeListener(null, transformations, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithMinimalDependenciesAndNullTransformations() {

        new TransformingParseTreeListener(recognizer, null, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithMinimalDependenciesAndNullTransformableString() {

        new TransformingParseTreeListener(recognizer, transformations, null);
    }

    @Test
    public void testCreateWithAllDependencies() {

        createListener();
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullRecognizer() {

        new TransformingParseTreeListener(null, transformations, parentTransformations, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullTransformations() {

        new TransformingParseTreeListener(recognizer, null, parentTransformations, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullParentTransformations() {

        new TransformingParseTreeListener(recognizer, transformations, null, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullTransformableString() {

        new TransformingParseTreeListener(recognizer, transformations, parentTransformations, null);
    }

    @Test
    public void testVisitTerminal() {

        createListener().visitTerminal(mockTerminalNodeWithDefaultToken());

        verifyTransforamations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);

        verifyParentTransforamations(CALLED_ONCE, TEST_RULE_NAME_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitTerminalTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitTerminal(mockTerminalNodeWithDefaultToken());
        listener.visitTerminal(mockTerminalNodeWithDefaultToken());

        verifyTransforamations(CALLED_TWICE, TEST_TOKEN_NAME_ONE);

        verifyParentTransforamations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformableString(CALLED_FOUR_TIMES);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitTerminalTwiceWithDifferentTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitTerminal(mockTerminalNodeWithDefaultToken());
        listener.visitTerminal(mockTerminalNode(TEST_TOKEN_NAME_TWO, TEST_TOKEN_TYPE_TWO));

        verifyTransforamations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransforamations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyParentTransforamations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformableString(CALLED_FOUR_TIMES);

        verifyNoMoreDependencyInteractions();
    }

    @Test(expected = NullPointerException.class)
    public void testVisitTerminalWithNullNode() {

        createListener().visitTerminal(null);
    }

    @Test
    public void testVisitErrorNode() {

        createListener().visitErrorNode(mockErrorNodeWithDefaultToken());

        verifyTransforamations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);

        verifyTransformableString(CALLED_ONCE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitErrorNodeTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitErrorNode(mockErrorNodeWithDefaultToken());
        listener.visitErrorNode(mockErrorNodeWithDefaultToken());

        verifyTransforamations(CALLED_TWICE, TEST_TOKEN_NAME_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitErrorNodeTwiceWithDifferentTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitErrorNode(mockErrorNodeWithDefaultToken());
        listener.visitErrorNode(mockErrorNode(TEST_TOKEN_NAME_TWO, TEST_TOKEN_TYPE_TWO));

        verifyTransforamations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransforamations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test(expected = NullPointerException.class)
    public void testVisitErrorNodeWithNullNode() {

        createListener().visitErrorNode(null);
    }

    @Test
    public void testEnterEveryRule() {

        createListener().enterEveryRule(mockParserRuleContextWithDefaultToken());

        verifyTransforamations(CALLED_ONCE, TEST_RULE_NAME_ONE);

        verifyTransformableString(CALLED_ONCE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(mockParserRuleContextWithDefaultToken());
        listener.enterEveryRule(mockParserRuleContextWithDefaultToken());

        verifyTransforamations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithDifferentTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(mockParserRuleContextWithDefaultToken());
        listener.enterEveryRule(mockParserRuleContext(TEST_TOKEN_NAME_TWO, TEST_TOKEN_TYPE_TWO));

        verifyTransforamations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithDifferentRules() {

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(mockParserRuleContextWithDefaultToken());
        listener.enterEveryRule(mockParserRuleContext(TEST_RULE_TYPE_TWO, TEST_TOKEN_NAME_TWO, TEST_TOKEN_TYPE_TWO));

        verifyTransforamations(CALLED_ONCE, TEST_RULE_NAME_ONE);
        verifyTransforamations(CALLED_ONCE, TEST_RULE_NAME_TWO);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test(expected = NullPointerException.class)
    public void testEnterEveryRuleWithNullContext() {

        createListener().enterEveryRule(null);
    }

    @Test
    public void testExitEveryRule() {

        createListener().exitEveryRule(mockParserRuleContextWithDefaultToken());

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testExitEveryRuleTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.exitEveryRule(mockParserRuleContextWithDefaultToken());
        listener.exitEveryRule(mockParserRuleContextWithDefaultToken());

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testExitEveryRuleTwiceWithDifferentTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.exitEveryRule(mockParserRuleContextWithDefaultToken());
        listener.exitEveryRule(mockParserRuleContext(TEST_TOKEN_NAME_TWO, TEST_TOKEN_TYPE_TWO));

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testExitEveryRuleWithNullContext() {

        createListener().exitEveryRule(null);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testToString() {

        when(inPlaceModifiableString.toString()).thenReturn(TEST_STRING);

        assertEquals("the listener toString should match the inPlaceModifiableString.toString.", TEST_STRING,
                createListener().toString());
    }

    private TransformingParseTreeListener createListener() {

        return new TransformingParseTreeListener(recognizer, transformations, parentTransformations,
                inPlaceModifiableString);
    }

    private void verifyTransforamations(int times, String value) {

        verify(transformations, times(times)).get(value);
    }

    private void verifyParentTransforamations(int times, String value) {

        verify(parentTransformations, times(times)).get(value);
    }

    private void verifyTransformableString(int times) {

        verify(inPlaceModifiableString, times(times)).setSubstring(anyString(), anyInt(), anyInt());
    }

    private void verifyNoMoreDependencyInteractions() {

        verifyNoMoreInteractions(transformations);
        verifyNoMoreInteractions(parentTransformations);
        verifyNoMoreInteractions(inPlaceModifiableString);
    }
}
