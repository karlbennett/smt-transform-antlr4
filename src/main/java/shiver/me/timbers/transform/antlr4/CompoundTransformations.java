package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import shiver.me.timbers.transform.IterableTransformations;

import java.util.LinkedList;
import java.util.List;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;
import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.NULL_TOKEN_TRANSFORMATION;

/**
 * A collection of transformation names that will all match to the same {@link TokenApplier#apply} logic.
 */
public class CompoundTransformations extends IterableTransformations<TokenTransformation> {

    public CompoundTransformations(Iterable<String> names, TokenApplier applier) {
        super(createTransformations(names, applier), NULL_TOKEN_TRANSFORMATION);
    }

    private static Iterable<TokenTransformation> createTransformations(Iterable<String> names, final TokenApplier applier) {

        assertIsNotNull(argumentIsNullMessage("names"), names);
        assertIsNotNull(argumentIsNullMessage("applier"), applier);

        final List<TokenTransformation> transformations = new LinkedList<TokenTransformation>();

        for (final String name : names) {

            transformations.add(new TokenTransformation() {

                @Override
                public String getName() {

                    return name;
                }

                @Override
                public String apply(RuleContext context, Token token, String string) {

                    return applier.apply(context, token, string);
                }
            });
        }

        return transformations;
    }
}
