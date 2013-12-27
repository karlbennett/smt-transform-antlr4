package shiver.me.timbers.transform.antlr4.listeners;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoggingErrorListenerTest {

    private static final int START_INDEX = 1;
    private static final int STOP_INDEX = 2;

    private Logger logger;

    @Before
    public void setUp() {

        logger = mock(Logger.class);
    }

    @Test
    public void testCreate() {

        new LoggingErrorListener();
    }

    @Test
    public void testCreateWithLogger() {

        new LoggingErrorListener(logger);
    }

    @Test
    public void testSyntaxError() {

        final String TEST_MESSAGE = "test message";

        final int LINE = 1;
        final int CHAR_INDEX = 2;

        new LoggingErrorListener(logger).syntaxError(null, null, LINE, CHAR_INDEX, TEST_MESSAGE, null);

        verify(logger, times(1)).warn(anyString(), eq(LINE), eq(CHAR_INDEX), eq(TEST_MESSAGE));
    }

    @Test
    public void testReportAmbiguity() {

        new LoggingErrorListener(logger).reportAmbiguity(null, null, START_INDEX, STOP_INDEX, false, null, null);

        verify(logger, times(1)).warn(anyString(), eq(START_INDEX), eq(STOP_INDEX));
    }

    @Test
    public void testReportAttemptingFullContext() {

        new LoggingErrorListener(logger).reportAttemptingFullContext(null, null, START_INDEX, STOP_INDEX, null, null);

        verify(logger, times(1)).warn(anyString(), eq(START_INDEX), eq(STOP_INDEX));
    }

    @Test
    public void testReportContextSensitivity() {

        new LoggingErrorListener(logger).reportContextSensitivity(null, null, START_INDEX, STOP_INDEX, 0, null);

        verify(logger, times(1)).warn(anyString(), eq(START_INDEX), eq(STOP_INDEX));
    }
}
