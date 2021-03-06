package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeListener;
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
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_ERROR_NODE_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_ERROR_NODE_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_CONTEXT_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_CONTEXT_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_NAME_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_NAME_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_TYPE_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_RULE_TYPE_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TERMINAL_NODE_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TERMINAL_NODE_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_NAME_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_NAME_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_ONE;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.TEST_TOKEN_TWO;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockErrorNode;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockParserRuleContext;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockParserRuleContextWithDefaultToken;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockRecognizer;
import static shiver.me.timbers.transform.antlr4.listeners.TestUtils.mockTerminalNode;

public class TransformingParseTreeListenerTest {

    private static final String TEST_STRING = "A test string with lots of text.";
    private static final int CALLED_ONCE = 1;
    private static final int CALLED_TWICE = 2;
    private static final int CALLED_THRICE = 3;
    private static final int CALLED_FOUR_TIMES = 4;

    private Recognizer recognizer;
    private TokenTransformation transformation;
    private Transformations<TokenTransformation> transformations;
    private InPlaceModifiableString inPlaceModifiableString;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        recognizer = mockRecognizer();

        transformation = mock(TokenTransformation.class);

        transformations = mock(Transformations.class);
        when(transformations.get(anyInt())).thenReturn(transformation);
        when(transformations.get(anyString())).thenReturn(transformation);

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

        new TransformingParseTreeListener(null, transformations, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullTransformations() {

        new TransformingParseTreeListener(recognizer, null, inPlaceModifiableString);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullTransformableString() {

        new TransformingParseTreeListener(recognizer, transformations, null);
    }

    @Test
    public void testVisitTerminal() {

        createListener().visitTerminal(TEST_TERMINAL_NODE_ONE);

        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);

        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitTerminalTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitTerminal(TEST_TERMINAL_NODE_ONE);
        listener.visitTerminal(TEST_TERMINAL_NODE_ONE);

        verifyTransformations(CALLED_TWICE, TEST_TOKEN_NAME_ONE);
        verifyTransformations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformation(CALLED_FOUR_TIMES, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_FOUR_TIMES);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitTerminalTwiceWithDifferentTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitTerminal(TEST_TERMINAL_NODE_ONE);
        listener.visitTerminal(mockTerminalNode(TEST_RULE_CONTEXT_ONE, TEST_TOKEN_TWO));

        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyTransformations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_TWO);

        verifyTransformableString(CALLED_FOUR_TIMES);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitTerminalTwiceWithDifferentRulesAndTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitTerminal(TEST_TERMINAL_NODE_ONE);
        listener.visitTerminal(TEST_TERMINAL_NODE_TWO);

        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_TWO);

        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_TWO, TEST_TOKEN_TWO);

        verifyTransformableString(CALLED_FOUR_TIMES);

        verifyNoMoreDependencyInteractions();
    }

    @Test(expected = NullPointerException.class)
    public void testVisitTerminalWithNullNode() {

        createListener().visitTerminal(null);
    }

    @Test
    public void testVisitErrorNode() {

        createListener().visitErrorNode(TEST_ERROR_NODE_ONE);

        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_ONCE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitErrorNodeTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitErrorNode(TEST_ERROR_NODE_ONE);
        listener.visitErrorNode(TEST_ERROR_NODE_ONE);

        verifyTransformations(CALLED_TWICE, TEST_TOKEN_NAME_ONE);

        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitErrorNodeTwiceWithDifferentTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitErrorNode(TEST_ERROR_NODE_ONE);
        listener.visitErrorNode(mockErrorNode(TEST_RULE_CONTEXT_ONE, TEST_TOKEN_TWO));

        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_TWO);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitErrorNodeTwiceWithDifferentRulesAndTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.visitErrorNode(TEST_ERROR_NODE_ONE);
        listener.visitErrorNode(TEST_ERROR_NODE_TWO);

        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_TWO, TEST_TOKEN_TWO);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test(expected = NullPointerException.class)
    public void testVisitErrorNodeWithNullNode() {

        createListener().visitErrorNode(null);
    }

    @Test
    public void testEnterEveryRule() {

        createListener().enterEveryRule(TEST_RULE_CONTEXT_ONE);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_ONCE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithTheSameToken() {

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);
        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);

        verifyTransformations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithDifferentTokens() {

        final ParserRuleContext mockContext = mockParserRuleContext(TEST_RULE_TYPE_ONE, TEST_TOKEN_TWO);

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);
        listener.enterEveryRule(mockContext);

        verifyTransformations(CALLED_TWICE, TEST_RULE_NAME_ONE);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_ONCE, mockContext, TEST_TOKEN_TWO);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithDifferentRules() {

        final ParserRuleContext mockContext = mockParserRuleContext(TEST_RULE_TYPE_TWO, TEST_TOKEN_ONE);

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);
        listener.enterEveryRule(mockContext);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_TWO);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_ONCE, mockContext, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testEnterEveryRuleTwiceWithDifferentRulesAndTokens() {

        final TransformingParseTreeListener listener = createListener();
        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);
        listener.enterEveryRule(TEST_RULE_CONTEXT_TWO);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_TWO);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_TWO, TEST_TOKEN_TWO);

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
        listener.exitEveryRule(mockParserRuleContext(TEST_RULE_TYPE_ONE, TEST_TOKEN_TWO));

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

    @Test
    public void testVisitVisitTerminalAfterEnterEveryRuleWithSameTokensAndRules() throws Exception {

        final ParseTreeListener listener = createListener();

        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);
        listener.visitTerminal(TEST_TERMINAL_NODE_ONE);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);

        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitVisitTerminalAfterEnterEveryRuleWithSameTokensAndDifferentRules() throws Exception {

        final ParseTreeListener listener = createListener();

        final ParserRuleContext context = mockParserRuleContext(TEST_RULE_TYPE_TWO, TEST_TOKEN_ONE);

        listener.enterEveryRule(context);
        listener.visitTerminal(TEST_TERMINAL_NODE_ONE);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_TWO);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_ONE);

        verifyTransformation(CALLED_ONCE, context, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);

        verifyTransformableString(CALLED_TWICE);

        verifyNoMoreDependencyInteractions();
    }

    @Test
    public void testVisitVisitTerminalAfterEnterEveryRuleWithDifferentTokenAndDifferentRules() throws Exception {

        final ParseTreeListener listener = createListener();

        listener.enterEveryRule(TEST_RULE_CONTEXT_ONE);
        listener.visitTerminal(TEST_TERMINAL_NODE_TWO);

        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_ONE);
        verifyTransformations(CALLED_ONCE, TEST_RULE_NAME_TWO);
        verifyTransformations(CALLED_ONCE, TEST_TOKEN_NAME_TWO);

        verifyTransformation(CALLED_ONCE, TEST_RULE_CONTEXT_ONE, TEST_TOKEN_ONE);
        verifyTransformation(CALLED_TWICE, TEST_RULE_CONTEXT_TWO, TEST_TOKEN_TWO);

        verifyTransformableString(CALLED_THRICE);

        verifyNoMoreDependencyInteractions();
    }

    private TransformingParseTreeListener createListener() {

        return new TransformingParseTreeListener(recognizer, transformations, inPlaceModifiableString);
    }

    private void verifyTransformation(int times, ParserRuleContext context, Token token) {

        verify(transformation, times(times)).apply(context, token, token.getText());
    }

    private void verifyTransformations(int times, String value) {

        verify(transformations, times(times)).get(value);
    }

    private void verifyTransformableString(int times) {

        verify(inPlaceModifiableString, times(times)).setSubstring(anyString(), anyInt(), anyInt());
    }

    private void verifyNoMoreDependencyInteractions() {

        verifyNoMoreInteractions(transformations);
        verifyNoMoreInteractions(inPlaceModifiableString);
    }
}
