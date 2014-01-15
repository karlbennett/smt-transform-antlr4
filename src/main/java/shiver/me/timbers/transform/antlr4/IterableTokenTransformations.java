package shiver.me.timbers.transform.antlr4;

import shiver.me.timbers.transform.iterable.IterableTransformations;

import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.NULL_TOKEN_TRANSFORMATION;

public class IterableTokenTransformations extends IterableTransformations<TokenTransformation> {

    public IterableTokenTransformations() {
        super(NULL_TOKEN_TRANSFORMATION);
    }

    public IterableTokenTransformations(Iterable<TokenTransformation> transformations) {
        super(transformations, NULL_TOKEN_TRANSFORMATION);
    }
}
