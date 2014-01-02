package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiver.me.timbers.transform.IndividualTransformations;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.StreamTransformer;
import shiver.me.timbers.transform.antlr4.listeners.TransformingParseTreeListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;
import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.NULL_TOKEN_TRANSFORMATION;

/**
 * This is an ANTLR4 specific {@code StreamTransformer} that can be used transform the code within the supplied input stream
 * with the use of the parser built from the dependent {@code ParserBuilder}.
 */
public class Antlr4StreamTransformer<P extends Recognizer> implements StreamTransformer<TokenTransformation> {

    private static final int STREAM_COPY_BUFFER_SIZE = 1024 * 4; // This value was taken from commons-io.

    private final Logger log = LoggerFactory.getLogger(Antlr4StreamTransformer.class);

    private final ParserBuilder<P> parserBuilder;

    private final Transformations<TokenTransformation> parentRuleTransformations;

    public Antlr4StreamTransformer(ParserBuilder<P> parserBuilder) {

        this(parserBuilder, new IndividualTransformations<TokenTransformation>(NULL_TOKEN_TRANSFORMATION));
    }

    /**
     * The {@code parentRuleTransformations} should contain any transformations that should
     * be run for the parent rule of a terminal token.
     */
    public Antlr4StreamTransformer(ParserBuilder<P> parserBuilder,
                                   Transformations<TokenTransformation> parentRuleTransformations) {

        assertIsNotNull(argumentIsNullMessage("parentRuleTransformations"), parentRuleTransformations);

        log.debug("{} created.", Antlr4StreamTransformer.class.getSimpleName());

        this.parserBuilder = parserBuilder;
        this.parentRuleTransformations = parentRuleTransformations;
    }

    @Override
    public String transform(InputStream stream, final Transformations<TokenTransformation> transformations) {

        log.debug("Reading input stream into string.");
        final String source = toString(stream);

        log.debug("Building parser.");
        final P parser = parserBuilder.buildParser(source, transformations);

        log.debug("Parsing.");
        final ParseTree result = parserBuilder.parse(parser);

        final ParseTreeListener listener = new TransformingParseTreeListener(parser, transformations,
                parentRuleTransformations, new InPlaceModifiableString(source));

        log.debug("Begin walking the parse tree.");
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, result);

        return listener.toString();
    }

    private static String toString(InputStream stream) {

        final Reader reader = new InputStreamReader(stream);

        final StringWriter writer = copyToStringWriter(reader);

        return writer.toString();
    }

    private static StringWriter copyToStringWriter(Reader reader) {

        final StringWriter writer = new StringWriter();

        final char[] buffer = new char[STREAM_COPY_BUFFER_SIZE];

        try {

            for (int charsRead = 0; charsRead != -1; charsRead = reader.read(buffer)) {

                writer.write(buffer, 0, charsRead);
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return writer;
    }
}
