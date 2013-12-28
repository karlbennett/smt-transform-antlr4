package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;

/**
 * This interface should be implemented with logic for modifying token strings.
 */
public interface TokenApplyer {

    /**
     * @return a string that has been derived from the supplied inputs.
     */
    public String apply(RuleContext context, Token token, String string);
}
