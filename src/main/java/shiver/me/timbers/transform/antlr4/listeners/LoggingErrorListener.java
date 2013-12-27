package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

/**
 * This console error listener will log and errors passed to this listener at the warning level.
 *
 * @author Karl Bennett
 */
public class LoggingErrorListener implements ANTLRErrorListener {

    private final Logger logger;

    public LoggingErrorListener() {

        this(LoggerFactory.getLogger(LoggingErrorListener.class));
    }

    public LoggingErrorListener(Logger logger) {

        this.logger = logger;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {

        logger.warn("A syntax error was found at line {}:{} {}", line, charPositionInLine, msg);
    }

    @Override
    public void reportAmbiguity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                boolean exact, @NotNull BitSet ambigAlts, @NotNull ATNConfigSet configs) {

        logger.warn("An ambiguity was found at index ({},{})", startIndex, stopIndex);
    }

    @Override
    public void reportAttemptingFullContext(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                            @Nullable BitSet conflictingAlts, @NotNull ATNConfigSet configs) {

        logger.warn("Attempting full context at index ({},{})", startIndex, stopIndex);
    }

    @Override
    public void reportContextSensitivity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                         int prediction, @NotNull ATNConfigSet configs) {

        logger.warn("Context sensitivity at index ({},{})", startIndex, stopIndex);
    }
}
