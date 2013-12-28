package shiver.me.timbers.transform.antlr4;

import shiver.me.timbers.transform.Transformation;

/**
 * This transformation is used specifically for transforming the strings related to ANTLR4
 * {@link org.antlr.v4.runtime.Token}s.
 */
public interface TokenTransformation extends Transformation, TokenApplier {
}
