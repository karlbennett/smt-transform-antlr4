package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import shiver.me.timbers.transform.NullTransformation;

/**
 * This transformation should be returned when no matching transformation can be found.
 */
public class NullTokenTransformation extends NullTransformation implements TokenTransformation {

    public static final TokenTransformation NULL_TOKEN_TRANSFORMATION = new NullTokenTransformation();

    @Override
    public String apply(RuleContext context, Token token, String string) {

        return string;
    }
}
