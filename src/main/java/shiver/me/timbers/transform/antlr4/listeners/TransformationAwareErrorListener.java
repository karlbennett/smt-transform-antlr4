package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shiver.me.timbers.transform.NullTransformation;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.util.BitSet;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This console error listener can be used to wrap any other error listener, it will then suppress any syntax errors
 * that relate to tokens that have related transformations. This is usually used to suppress the syntax errors reported
 * for comments.
 *
 * @author Karl Bennett
 */
public class TransformationAwareErrorListener implements ANTLRErrorListener {

    private final Logger log = LoggerFactory.getLogger(TransformationAwareErrorListener.class);

    private final ANTLRErrorListener listener;
    private final Transformations<TokenTransformation> transformations;

    public TransformationAwareErrorListener(ANTLRErrorListener listener,
                                            Transformations<TokenTransformation> transformations) {

        assertIsNotNull(argumentIsNullMessage("listener"), listener);
        assertIsNotNull(argumentIsNullMessage("transformations"), transformations);

        log.debug("{} created.", TransformationAwareErrorListener.class.getSimpleName());

        this.listener = listener;
        this.transformations = transformations;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {

        final Token token = (Token) offendingSymbol;

        final int type = token.getType();

        if (0 <= type) {

            final String tokenName = recognizer.getTokenNames()[type];

            if (canTransform(tokenName)) {

                log.debug("\"{}\" token can be transformed so error suppressed.", tokenName);

                return;
            }
        }

        listener.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
    }

    private boolean canTransform(String tokenName) {

        return !(transformations.get(tokenName) instanceof NullTransformation);
    }

    @Override
    public void reportAmbiguity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                boolean exact, @NotNull BitSet ambigAlts, @NotNull ATNConfigSet configs) {

        listener.reportAmbiguity(recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs);
    }

    @Override
    public void reportAttemptingFullContext(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                            @Nullable BitSet conflictingAlts, @NotNull ATNConfigSet configs) {

        listener.reportAttemptingFullContext(recognizer, dfa, startIndex, stopIndex, conflictingAlts, configs);
    }

    @Override
    public void reportContextSensitivity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                         int prediction, @NotNull ATNConfigSet configs) {

        listener.reportContextSensitivity(recognizer, dfa, startIndex, stopIndex, prediction, configs);
    }
}
