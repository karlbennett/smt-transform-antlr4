package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiver.me.timbers.transform.AbstractTransformer;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.listeners.TransformingParseTreeListener;
import shiver.me.timbers.transform.string.StringTransformer;

import javax.activation.MimeType;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This is an ANTLR4 specific {@code StreamTransformer} that can be used transform the code within the supplied input stream
 * with the use of the parser built from the dependent {@code ParserBuilder}.
 */
public class Antlr4StringTransformer<P extends Parser> extends AbstractTransformer<String, TokenTransformation>
        implements StringTransformer<TokenTransformation> {

    private final Logger log = LoggerFactory.getLogger(Antlr4StringTransformer.class);

    private final ParserBuilder<P> parserBuilder;

    public Antlr4StringTransformer(MimeType mimeType, ParserBuilder<P> parserBuilder) {
        super(mimeType);

        assertIsNotNull(argumentIsNullMessage("parserBuilder"), parserBuilder);

        log.debug("{} created.", Antlr4StringTransformer.class.getSimpleName());

        this.parserBuilder = parserBuilder;
    }

    @Override
    public String transform(String source, final Transformations<TokenTransformation> transformations) {

        log.debug("Building parser.");
        final P parser = parserBuilder.buildParser(source, transformations);

        final ParseTreeListener listener = new TransformingParseTreeListener(parser, transformations,
                new InPlaceModifiableString(source));
        parser.addParseListener(listener);

        log.debug("Parsing.");
        parserBuilder.parse(parser);

        return listener.toString();
    }
}
