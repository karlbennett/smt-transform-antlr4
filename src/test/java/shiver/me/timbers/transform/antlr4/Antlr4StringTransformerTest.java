package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Parser;
import org.junit.Test;
import shiver.me.timbers.transform.Transformations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.NullTransformer.TEXT_PLAIN;

public class Antlr4StringTransformerTest {

    private static final String TEST_STRING = "test string";

    @Test
    public void testCreateWithMimeTypeAndParserBuilder() {

        new Antlr4StringTransformer<Parser>(TEXT_PLAIN, mockParserBuilder());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullMimeTypeAndParserBuilder() {

        new Antlr4StringTransformer<Parser>(null, mockParserBuilder());
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithMimeTypeAndNullParserBuilder() {

        new Antlr4StringTransformer<Parser>(TEXT_PLAIN, null);
    }

    @Test
    public void testTransform() {

        final Parser parser = mock(Parser.class);
        final Transformations<TokenTransformation> transformations = mockTransformations();

        final ParserBuilder<Parser> parserBuilder = mockParserBuilder(parser, transformations);

        new Antlr4StringTransformer<Parser>(TEXT_PLAIN, parserBuilder).transform(TEST_STRING, transformations);

        verifyZeroInteractions(transformations);

        verify(parserBuilder, times(1)).buildParser(TEST_STRING, transformations);
        verify(parserBuilder, times(1)).parse(parser);
        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = AssertionError.class)
    public void testTransformWithNullStream() {

        new Antlr4StringTransformer<Parser>(TEXT_PLAIN, mockParserBuilder()).transform(null, mockTransformations());
    }

    @Test(expected = AssertionError.class)
    public void testTransformWithNullTransformations() {

        new Antlr4StringTransformer<Parser>(TEXT_PLAIN, mockParserBuilder()).transform(TEST_STRING, null);
    }

    private static ParserBuilder<Parser> mockParserBuilder() {

        return mockParserBuilder(mock(Parser.class), mockTransformations());
    }

    private static ParserBuilder<Parser> mockParserBuilder(Parser parser,
                                                           Transformations<TokenTransformation> tokenTransformations) {

        @SuppressWarnings("unchecked")
        final ParserBuilder<Parser> parserBuilder = mock(ParserBuilder.class);
        when(parserBuilder.buildParser(TEST_STRING, tokenTransformations)).thenReturn(parser);

        return parserBuilder;
    }

    @SuppressWarnings("unchecked")
    private static Transformations<TokenTransformation> mockTransformations() {

        return mock(Transformations.class);
    }
}
