package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiver.me.timbers.transform.IndividualTransformations;
import shiver.me.timbers.transform.TransformableString;
import shiver.me.timbers.transform.Transformation;
import shiver.me.timbers.transform.Transformations;

import java.util.Collections;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This parse tree listener will apply any supplied transformations to related tokens exposed in the listener methods.
 */
public class TransformingParseTreeListener implements ParseTreeListener {

    private static final Transformations EMPTY_TRANSFORMATIONS = new IndividualTransformations(
            Collections.<Transformation>emptySet());

    private final Logger log = LoggerFactory.getLogger(TransformingParseTreeListener.class);

    private final Recognizer recognizer;
    private final Transformations transformations;
    private Transformations parentRuleTransformations;
    private final TransformableString transformableString;

    public TransformingParseTreeListener(Recognizer recognizer, Transformations transformations,
                                         TransformableString transformableString) {

        this(recognizer, transformations, EMPTY_TRANSFORMATIONS, transformableString);
    }

    /**
     * The {@code parentRuleTransformations} argument in this constructor should contain any transformations that should
     * be run for the parent rule of a terminal token. That is when a token is passed to
     * {@link #visitTerminal(TerminalNode)} it's parent rules name will be passed to the
     * {@link Transformations#get(String)} method of the {@code parentRuleTransformations} and the resulting
     * transformation will be applied to that token.
     */
    public TransformingParseTreeListener(Recognizer recognizer, Transformations transformations,
                                         Transformations parentRuleTransformations,
                                         TransformableString transformableString) {

        log.debug("{} created.", TransformingParseTreeListener.class.getSimpleName());

        assertIsNotNull(argumentIsNullMessage("recognizer"), recognizer);
        assertIsNotNull(argumentIsNullMessage("transformations"), transformations);
        assertIsNotNull(argumentIsNullMessage("parentRuleTransformations"),
                parentRuleTransformations);
        assertIsNotNull(argumentIsNullMessage("transformableString"), transformableString);

        this.transformableString = transformableString;
        this.transformations = transformations;
        this.parentRuleTransformations = parentRuleTransformations;
        this.recognizer = recognizer;
    }

    @Override
    public void visitTerminal(@NotNull TerminalNode node) {

        final RuleContext context = (RuleContext) node.getParent().getPayload();

        final Token token = node.getSymbol();

        log.debug("\"{}\" terminal node visited.", token.getText());

        transformToken(transformations, token);

        transformRule(parentRuleTransformations, context, token);
    }

    @Override
    public void visitErrorNode(@NotNull ErrorNode node) {

        final Token token = node.getSymbol();

        log.debug("\"{}\" error node visited.", token.getText());

        transformToken(transformations, token);
    }

    @Override
    public void enterEveryRule(@NotNull ParserRuleContext context) {

        final Token token = context.getStart();

        log.debug("Rule visited for \"{}\".", token.getText());

        transformRule(transformations, context, token);
    }

    @Override
    public void exitEveryRule(@NotNull ParserRuleContext context) {
    }

    private void transformRule(Transformations transformations, RuleContext context, Token token) {

        final String ruleName = getRuleName(context.getRuleIndex());

        final Transformation transformation = transformations.get(ruleName);

        log.debug("\"{}\" transformation found for rule \"{}\".", transformation.getName(), ruleName);

        transformableString.transformSubstring(transformation, token.getStartIndex(), token.getStopIndex());
    }

    private String getRuleName(int rule) {

        return recognizer.getRuleNames()[rule];
    }

    private void transformToken(Transformations transformations, Token token) {

        if (isValidTokenType(token)) {

            final String tokenName = getTokenName(token.getType());

            final Transformation transformation = transformations.get(tokenName);

            log.debug("\"{}\" transformation found for token \"{}\".", transformation.getName(), tokenName);

            transformableString.transformSubstring(transformation, token.getStartIndex(), token.getStopIndex());
        }
    }

    private boolean isValidTokenType(Token token) {

        return 0 <= token.getType();
    }

    private String getTokenName(int type) {

        return recognizer.getTokenNames()[type];
    }

    @Override
    public String toString() {

        return transformableString.toString();
    }
}
