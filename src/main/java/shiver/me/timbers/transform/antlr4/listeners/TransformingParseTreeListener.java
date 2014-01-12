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
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.InPlaceModifiableString;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.util.HashSet;
import java.util.Set;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This parse tree listener will apply any supplied transformations to related tokens exposed in the listener methods.
 */
public class TransformingParseTreeListener implements ParseTreeListener {

    public static final String EOF = "EOF";

    private final Logger log = LoggerFactory.getLogger(TransformingParseTreeListener.class);

    private final Recognizer recognizer;
    private final Transformations<TokenTransformation> transformations;
    private final InPlaceModifiableString inPlaceModifiableString;

    private TokenStrings tokenStrings;

    private final Set<String> ruleTokens;

    public TransformingParseTreeListener(Recognizer recognizer, Transformations<TokenTransformation> transformations,
                                         InPlaceModifiableString inPlaceModifiableString) {

        log.debug("{} created.", TransformingParseTreeListener.class.getSimpleName());

        assertIsNotNull(argumentIsNullMessage("recognizer"), recognizer);
        assertIsNotNull(argumentIsNullMessage("transformations"), transformations);
        assertIsNotNull(argumentIsNullMessage("inPlaceModifiableString"), inPlaceModifiableString);

        this.recognizer = recognizer;
        this.transformations = transformations;
        this.inPlaceModifiableString = inPlaceModifiableString;

        this.tokenStrings = new TokenStrings();

        this.ruleTokens = new HashSet<String>();
    }

    @Override
    public void visitTerminal(@NotNull TerminalNode node) {

        final RuleContext context = (RuleContext) node.getParent().getPayload();

        final Token token = node.getSymbol();

        final String tokenName = getTokenName(token.getType());

        final String ruleName = getRuleName(context.getRuleIndex());

        log.debug("Terminal node visited for \"{}\" token with rule \"{}\" and value \"{}\".",
                tokenName, ruleName, token.getText());

        transformString(transformations, tokenName, context, token);

        if (ruleNotApplied(token)) {

            transformString(transformations, ruleName, context, token);
        }
    }

    @Override
    public void visitErrorNode(@NotNull ErrorNode node) {

        final RuleContext context = (RuleContext) node.getParent().getPayload();

        final Token token = node.getSymbol();

        final String tokenName = getTokenName(token.getType());

        log.debug("Error node visited for \"{}\" token with value \"{}\".", tokenName, token.getText());

        transformString(transformations, tokenName, context, token);
    }

    @Override
    public void enterEveryRule(@NotNull ParserRuleContext context) {

        final Token token = context.getStart();

        final String ruleName = getRuleName(context.getRuleIndex());

        log.debug("\"{}\" rule visited for \"{}\".", ruleName, token.getText());

        transformString(transformations, ruleName, context, token);

        registerAppliedRule(token);
    }

    @Override
    public void exitEveryRule(@NotNull ParserRuleContext context) {
    }

    private String getTokenName(int type) {

        return Recognizer.EOF == type ? EOF : recognizer.getTokenNames()[type];
    }

    private String getRuleName(int rule) {

        return recognizer.getRuleNames()[rule];
    }

    private boolean ruleNotApplied(Token token) {

        return !ruleTokens.contains(token.getText());
    }

    private void registerAppliedRule(Token token) {

        ruleTokens.add(token.getText());
    }

    private void transformString(Transformations<TokenTransformation> transformations, String name, RuleContext context,
                                 Token token) {

        if (isValidTokenType(token)) {

            final TokenTransformation transformation = transformations.get(name);

            log.debug("Transformation \"{}\" found for token \"{}\".", transformation.getName(), token.getText());

            final String currentTokenString = tokenStrings.get(token);

            final String transformedString = transformation.apply(context, token, currentTokenString);

            inPlaceModifiableString.setSubstring(transformedString, token.getStartIndex(), token.getStopIndex());

            tokenStrings.set(token, transformedString);
        }
    }

    private boolean isValidTokenType(Token token) {

        return 0 <= token.getType();
    }

    @Override
    public String toString() {

        return inPlaceModifiableString.toString();
    }
}
