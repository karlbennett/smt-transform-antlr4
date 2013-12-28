package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import shiver.me.timbers.transform.NamedTransformation;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This is a concrete {@code Transformation} that can have it's {@code name} and {@link TokenApplier} set as constructor
 * dependencies.
 */
public class CompositeTokenTransformation extends NamedTransformation implements TokenTransformation {

    private final TokenApplier applier;

    public CompositeTokenTransformation(String name, TokenApplier applier) {
        super(name);

        assertIsNotNull(argumentIsNullMessage("applier"), applier);

        this.applier = applier;
    }

    @Override
    public String apply(RuleContext context, Token token, String string) {

        return applier.apply(context, token, string);
    }
}
