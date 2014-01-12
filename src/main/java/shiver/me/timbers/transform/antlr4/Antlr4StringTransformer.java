package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.listeners.TransformingParseTreeListener;
import shiver.me.timbers.transform.string.StringTransformer;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This is an ANTLR4 specific {@code StreamTransformer} that can be used transform the code within the supplied input stream
 * with the use of the parser built from the dependent {@code ParserBuilder}.
 */
public class Antlr4StringTransformer<P extends Recognizer> implements StringTransformer<TokenTransformation> {

    private final Logger log = LoggerFactory.getLogger(Antlr4StringTransformer.class);

    private final ParserBuilder<P> parserBuilder;

    public Antlr4StringTransformer(ParserBuilder<P> parserBuilder) {

        assertIsNotNull(argumentIsNullMessage("parserBuilder"), parserBuilder);

        log.debug("{} created.", Antlr4StringTransformer.class.getSimpleName());

        this.parserBuilder = parserBuilder;
    }

    @Override
    public String transform(String source, final Transformations<TokenTransformation> transformations) {

        log.debug("Building parser.");
        final P parser = parserBuilder.buildParser(source, transformations);

        log.debug("Parsing.");
        final ParseTree result = parserBuilder.parse(parser);

        final ParseTreeListener listener = new TransformingParseTreeListener(parser, transformations,
                new InPlaceModifiableString(source));

        log.debug("Begin walking the parse tree.");
        final ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, result);

        return listener.toString();
    }
}
