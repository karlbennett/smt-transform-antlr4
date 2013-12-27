package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.transform.NullTransformation;
import shiver.me.timbers.transform.Transformation;
import shiver.me.timbers.transform.Transformations;

import java.util.BitSet;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Karl Bennett
 */
public class TransformationAwareErrorListenerTest {

    private static final int TOKEN_TYPE = 0;
    private static final String TOKEN_NAME = "test token";

    private static final int LINE = 1;
    private static final int CHAR_POSITION_IN_LINE = 100;
    private static final String MESSAGE = "Test error message.";
    private static final int START_INDEX = 10;
    private static final int STOP_INDEX = 20;
    private static final boolean EXACT = true;
    private static final int PREDICTION = 42;

    private ANTLRErrorListener listener;
    private Parser parser;
    private Token token;
    private RecognitionException exception;
    private DFA dfa;
    private BitSet bitSet;
    private ATNConfigSet configs;

    @Before
    public void setUp() {

        listener = mock(ANTLRErrorListener.class);

        parser = mock(Parser.class);
        when(parser.getTokenNames()).thenReturn(new String[]{TOKEN_NAME});

        token = mock(Token.class);

        exception = mock(RecognitionException.class);
        dfa = mock(DFA.class);
        bitSet = mock(BitSet.class);
        configs = mock(ATNConfigSet.class);
    }

    @Test
    public void testCreate() {

        new TransformationAwareErrorListener(listener, mockTransformations());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullListener() {

        new TransformationAwareErrorListener(null, mockTransformations());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullCommentTypes() {

        new TransformationAwareErrorListener(listener, null);
    }

    @Test
    public void testSyntaxError() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, exception);

        verify(listener, times(1)).syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, exception);
    }

    @Test
    public void testSyntaxErrorWithRecognisedType() {

        when(token.getType()).thenReturn(TOKEN_TYPE);

        new TransformationAwareErrorListener(listener, mockTransformationsWithToken(parser, token))
                .syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, exception);

        verify(listener, times(0)).syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, exception);
    }

    @Test(expected = NullPointerException.class)
    public void testSyntaxErrorWithNullRecognizer() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .syntaxError(null, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, exception);
    }

    @Test(expected = NullPointerException.class)
    public void testSyntaxErrorWithNullToken() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .syntaxError(parser, null, LINE, CHAR_POSITION_IN_LINE, MESSAGE, exception);
    }

    @Test
    public void testSyntaxErrorWithNullMessage() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, null, exception);

        verify(listener, times(1)).syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, null, exception);
    }

    @Test
    public void testSyntaxErrorWithNullException() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, null);

        verify(listener, times(1)).syntaxError(parser, token, LINE, CHAR_POSITION_IN_LINE, MESSAGE, null);
    }

    @Test
    public void testReportAmbiguity() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAmbiguity(parser, dfa, START_INDEX, STOP_INDEX, EXACT, bitSet, configs);

        verify(listener, times(1)).reportAmbiguity(parser, dfa, START_INDEX, STOP_INDEX, EXACT, bitSet, configs);
    }

    @Test
    public void testReportAmbiguityWithNullRecognizer() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAmbiguity(null, dfa, START_INDEX, STOP_INDEX, EXACT, bitSet, configs);

        verify(listener, times(1)).reportAmbiguity(null, dfa, START_INDEX, STOP_INDEX, EXACT, bitSet, configs);
    }

    @Test
    public void testReportAmbiguityWithNullDfa() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAmbiguity(parser, null, START_INDEX, STOP_INDEX, EXACT, bitSet, configs);

        verify(listener, times(1)).reportAmbiguity(parser, null, START_INDEX, STOP_INDEX, EXACT, bitSet, configs);
    }

    @Test
    public void testReportAmbiguityWithNullBitSet() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAmbiguity(parser, dfa, START_INDEX, STOP_INDEX, EXACT, null, configs);

        verify(listener, times(1)).reportAmbiguity(parser, dfa, START_INDEX, STOP_INDEX, EXACT, null, configs);
    }

    @Test
    public void testReportAmbiguityWithNullConfigs() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAmbiguity(parser, dfa, START_INDEX, STOP_INDEX, EXACT, bitSet, null);

        verify(listener, times(1)).reportAmbiguity(parser, dfa, START_INDEX, STOP_INDEX, EXACT, bitSet, null);
    }

    @Test
    public void testReportAttemptingFullContext() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAttemptingFullContext(parser, dfa, START_INDEX, STOP_INDEX, bitSet, configs);

        verify(listener, times(1))
                .reportAttemptingFullContext(parser, dfa, START_INDEX, STOP_INDEX, bitSet, configs);
    }

    @Test
    public void testReportAttemptingFullContextWithNullRecognizer() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAttemptingFullContext(null, dfa, START_INDEX, STOP_INDEX, bitSet, configs);

        verify(listener, times(1))
                .reportAttemptingFullContext(null, dfa, START_INDEX, STOP_INDEX, bitSet, configs);
    }

    @Test
    public void testReportAttemptingFullContextWithNullDfa() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAttemptingFullContext(parser, null, START_INDEX, STOP_INDEX, bitSet, configs);

        verify(listener, times(1))
                .reportAttemptingFullContext(parser, null, START_INDEX, STOP_INDEX, bitSet, configs);
    }

    @Test
    public void testReportAttemptingFullContextWithNullBitSet() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAttemptingFullContext(parser, dfa, START_INDEX, STOP_INDEX, null, configs);

        verify(listener, times(1))
                .reportAttemptingFullContext(parser, dfa, START_INDEX, STOP_INDEX, null, configs);
    }

    @Test
    public void testReportAttemptingFullContextWithNullConfigs() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportAttemptingFullContext(parser, dfa, START_INDEX, STOP_INDEX, bitSet, null);

        verify(listener, times(1))
                .reportAttemptingFullContext(parser, dfa, START_INDEX, STOP_INDEX, bitSet, null);
    }

    @Test
    public void testReportContextSensitivity() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportContextSensitivity(parser, dfa, START_INDEX, STOP_INDEX, PREDICTION, configs);

        verify(listener, times(1))
                .reportContextSensitivity(parser, dfa, START_INDEX, STOP_INDEX, PREDICTION, configs);
    }

    @Test
    public void testReportContextSensitivityWithNullRecognizer() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportContextSensitivity(null, dfa, START_INDEX, STOP_INDEX, PREDICTION, configs);

        verify(listener, times(1))
                .reportContextSensitivity(null, dfa, START_INDEX, STOP_INDEX, PREDICTION, configs);
    }

    @Test
    public void testReportContextSensitivityWithNullDfa() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportContextSensitivity(parser, null, START_INDEX, STOP_INDEX, PREDICTION, configs);

        verify(listener, times(1))
                .reportContextSensitivity(parser, null, START_INDEX, STOP_INDEX, PREDICTION, configs);
    }

    @Test
    public void testReportContextSensitivityWithNullConfigs() {

        new TransformationAwareErrorListener(listener, mockTransformations())
                .reportContextSensitivity(parser, dfa, START_INDEX, STOP_INDEX, PREDICTION, null);

        verify(listener, times(1))
                .reportContextSensitivity(parser, dfa, START_INDEX, STOP_INDEX, PREDICTION, null);
    }

    private static Transformations mockTransformations() {

        final Transformations transformations = mock(Transformations.class);
        when(transformations.get(anyString())).thenReturn(new NullTransformation());

        return transformations;
    }

    private static Transformations mockTransformationsWithToken(Parser parser, Token token) {

        final Transformation transformation = mock(Transformation.class);

        final Transformations transformations = mock(Transformations.class);
        when(transformations.get(parser.getTokenNames()[token.getType()])).thenReturn(transformation);

        return transformations;
    }
}
