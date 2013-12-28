package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

import static shiver.me.timbers.checks.Checks.isNotNull;

/**
 * This class holds the current transformed string for any given {@code Token}. If the token hasn't been
 * seen yet it will just return the string from the {@link Token#getText()} method.
 */
public class TokenStrings {

    private final Map<String, String> tokenStrings = new HashMap<String, String>();

    /**
     * @return the string for the supplied token if it has been set, otherwise the just returns the string from the
     *         supplied tokens {@link Token#getText()} method.
     */
    public String get(Token token) {

        final String tokenString = tokenStrings.get(token.toString());

        return isNotNull(tokenString) ? tokenString : token.getText();
    }

    /**
     * Set the string for the supplied {@code Token}
     */
    public void set(Token token, String string) {

        tokenStrings.put(token.toString(), string);
    }
}
