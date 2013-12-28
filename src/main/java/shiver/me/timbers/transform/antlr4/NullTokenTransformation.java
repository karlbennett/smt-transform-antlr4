package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;

/**
 * This transformation should be returned when no matching transformation can be found.
 */
public class NullTokenTransformation implements TokenTransformation {

    public static final TokenTransformation NULL_TOKEN_TRANSFORMATION = new NullTokenTransformation();

    @Override
    public String getName() {

        return NullTokenTransformation.class.getSimpleName();
    }

    @Override
    public String apply(RuleContext context, Token token, String string) {

        return string;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NullTokenTransformation that = (NullTokenTransformation) o;

        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {

        return getName().hashCode();
    }
}
