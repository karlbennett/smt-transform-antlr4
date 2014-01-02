package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;
import shiver.me.timbers.transform.Transformations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class Antlr4TransformerTest {

    private static final String TEST_STRING = "test string";

    private static final int TEST_TOKEN_TYPE = 0;
    private static final String TEST_TOKEN_NAME = "test_token";

    private static final int TEST_RULE_TYPE = 0;
    private static final String TEST_RULE_NAME = "test_rule";

    @Test
    public void testCreateWithParserBuilder() {

        new Antlr4StreamTransformer<Recognizer>(mockParserBuilder());
    }

    @Test
    public void testCreateWithParserBuilderAndParentRuleTransformations() {

        new Antlr4StreamTransformer<Recognizer>(mockParserBuilder(), mockTransformations());
    }

    @Test
    public void testTransform() {

        final Transformations<TokenTransformation> transformations = mockTransformations();

        final ParserBuilder<Recognizer> parserBuilder = mockParserBuilder(transformations);

        new Antlr4StreamTransformer<Recognizer>(parserBuilder, transformations)
                .transform(toStream(TEST_STRING), transformations);

        verify(transformations, times(1)).get(TEST_TOKEN_NAME);
        verify(transformations, times(1)).get(TEST_RULE_NAME);
        verifyNoMoreInteractions(transformations);

        verify(parserBuilder, times(1)).buildParser(TEST_STRING, transformations);
        verify(parserBuilder, times(1)).parse(any(Recognizer.class));
        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = RuntimeException.class)
    public void testTransformWithInvalidStream() {

        new Antlr4StreamTransformer<Recognizer>(mockParserBuilder())
                .transform(mock(InputStream.class), mockTransformations());
    }

    @Test(expected = NullPointerException.class)
    public void testTransformWithNullStream() {

        new Antlr4StreamTransformer<Recognizer>(mockParserBuilder()).transform(null, mockTransformations());
    }

    @Test(expected = AssertionError.class)
    public void testTransformWithNullTransformations() {

        new Antlr4StreamTransformer<Recognizer>(mockParserBuilder()).transform(toStream(TEST_STRING), null);
    }

    private static ParserBuilder<Recognizer> mockParserBuilder() {

        return mockParserBuilder(mockTransformations());
    }

    private static ParserBuilder<Recognizer> mockParserBuilder(
            Transformations<TokenTransformation> tokenTransformations) {

        final RuleContext context = mock(RuleContext.class);
        when(context.getRuleIndex()).thenReturn(TEST_RULE_TYPE);

        final Token token = mock(Token.class);
        when(token.getType()).thenReturn(TEST_TOKEN_TYPE);
        when(token.getText()).thenReturn(TEST_TOKEN_NAME);

        final TerminalNode parseTree = mock(TerminalNode.class);
        when(parseTree.getParent()).thenReturn(parseTree);
        when(parseTree.getPayload()).thenReturn(context);
        when(parseTree.getSymbol()).thenReturn(token);

        final Recognizer recognizer = mock(Recognizer.class);
        when(recognizer.getTokenNames()).thenReturn(new String[]{TEST_TOKEN_NAME});
        when(recognizer.getRuleNames()).thenReturn(new String[]{TEST_RULE_NAME});

        @SuppressWarnings("unchecked")
        final ParserBuilder<Recognizer> parserBuilder = mock(ParserBuilder.class);
        when(parserBuilder.buildParser(TEST_STRING, tokenTransformations)).thenReturn(recognizer);
        when(parserBuilder.parse(recognizer)).thenReturn(parseTree);

        return parserBuilder;
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

    private static InputStream toStream(String string) {

        try {

            return new ByteArrayInputStream(string.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException(e);
        }
    }
}
