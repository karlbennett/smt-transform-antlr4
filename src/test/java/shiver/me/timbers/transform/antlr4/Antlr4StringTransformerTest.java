package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;
import shiver.me.timbers.transform.Transformations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.NullTransformer.TEXT_PLAIN;

public class Antlr4StringTransformerTest {

    private static final String TEST_STRING = "test string";

    private static final int TEST_TOKEN_TYPE = 0;
    private static final String TEST_TOKEN_NAME = "test_token";

    private static final int TEST_RULE_TYPE = 0;
    private static final String TEST_RULE_NAME = "test_rule";

    @Test
    public void testCreateWithMimeTypeAndParserBuilder() {

        new Antlr4StringTransformer<Recognizer>(TEXT_PLAIN, mockParserBuilder());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullMimeTypeAndParserBuilder() {

        new Antlr4StringTransformer<Recognizer>(null, mockParserBuilder());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithMimeTypeAndNullParserBuilder() {

        new Antlr4StringTransformer<Recognizer>(TEXT_PLAIN, null);
    }

    @Test
    public void testTransform() {

        final Transformations<TokenTransformation> transformations = mockTransformations();

        final ParserBuilder<Recognizer> parserBuilder = mockParserBuilder(transformations);

        new Antlr4StringTransformer<Recognizer>(TEXT_PLAIN, parserBuilder).transform(TEST_STRING, transformations);

        verify(transformations, times(1)).get(TEST_TOKEN_NAME);
        verify(transformations, times(1)).get(TEST_RULE_NAME);
        verifyNoMoreInteractions(transformations);

        verify(parserBuilder, times(1)).buildParser(TEST_STRING, transformations);
        verify(parserBuilder, times(1)).parse(any(Recognizer.class));
        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = AssertionError.class)
    public void testTransformWithNullStream() {

        new Antlr4StringTransformer<Recognizer>(TEXT_PLAIN, mockParserBuilder()).transform(null, mockTransformations());
    }

    @Test(expected = AssertionError.class)
    public void testTransformWithNullTransformations() {

        new Antlr4StringTransformer<Recognizer>(TEXT_PLAIN, mockParserBuilder()).transform(TEST_STRING, null);
    }

    private static ParserBuilder<Recognizer> mockParserBuilder() {

        return mockParserBuilder(mockTransformations());
    }

    private static ParserBuilder<Recognizer> mockParserBuilder(
            Transformations<TokenTransformation> tokenTransformations) {

        final RuleContext context = mockRuleContext();

        final Token token = mockToken();

        final TerminalNode terminalNode = mockTerminalNode(context, token);

        final Recognizer recognizer = mockRecognizer();

        @SuppressWarnings("unchecked")
        final ParserBuilder<Recognizer> parserBuilder = mock(ParserBuilder.class);
        when(parserBuilder.buildParser(TEST_STRING, tokenTransformations)).thenReturn(recognizer);
        when(parserBuilder.parse(recognizer)).thenReturn(terminalNode);

        return parserBuilder;
    }

    private static RuleContext mockRuleContext() {

        final RuleContext context = mock(RuleContext.class);
        when(context.getRuleIndex()).thenReturn(TEST_RULE_TYPE);

        return context;
    }

    private static Token mockToken() {

        final Token token = mock(Token.class);
        when(token.getType()).thenReturn(TEST_TOKEN_TYPE);
        when(token.getText()).thenReturn(TEST_TOKEN_NAME);

        return token;
    }

    private static TerminalNode mockTerminalNode(RuleContext context, Token token) {

        final TerminalNode terminalNode = mock(TerminalNode.class);
        when(terminalNode.getParent()).thenReturn(terminalNode);
        when(terminalNode.getPayload()).thenReturn(context);
        when(terminalNode.getSymbol()).thenReturn(token);

        return terminalNode;
    }

    private static Recognizer mockRecognizer() {

        final Recognizer recognizer = mock(Recognizer.class);
        when(recognizer.getTokenNames()).thenReturn(new String[]{TEST_TOKEN_NAME});
        when(recognizer.getRuleNames()).thenReturn(new String[]{TEST_RULE_NAME});

        return recognizer;
    }

    private static Transformations<TokenTransformation> mockTransformations() {

        final TokenTransformation transformation = mock(TokenTransformation.class);
        when(transformation.getName()).thenReturn(TEST_TOKEN_NAME);
        when(transformation.apply(any(RuleContext.class), any(Token.class), eq(TEST_TOKEN_NAME)))
                .thenReturn(TEST_TOKEN_NAME);

        @SuppressWarnings("unchecked")
        final Transformations<TokenTransformation> transformations = mock(Transformations.class);
        when(transformations.get(anyString())).thenReturn(transformation);

        return transformations;
    }
}
