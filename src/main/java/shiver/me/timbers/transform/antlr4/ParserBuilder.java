package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import shiver.me.timbers.transform.Transformations;

/**
 * This interface should be implemented with the complete instantiation and configuration of the language specific
 * parser.
 */
public interface ParserBuilder<P extends Parser> {

    /**
     * @param source               the source code that will be parsed.
     * @param errorTransformations any transformations that should be ignored if found to be errors.
     * @return a newly built language specific parser.
     */
    public P buildParser(String source, Transformations<TokenTransformation> errorTransformations);

    /**
     * @param parser the parser that will have it's top level language parse method called.
     * @return the parse tree produced by the top level language parser method.
     */
    public ParseTree parse(P parser);
}
