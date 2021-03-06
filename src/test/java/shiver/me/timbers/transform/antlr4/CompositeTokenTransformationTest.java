package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CompositeTokenTransformationTest {

    private static final String NAME = "test_name";

    private static final String TEST_TEXT = "some test text.";

    private TokenApplier applier;

    @Before
    public void setUp() {

        applier = mock(TokenApplier.class);
    }

    @Test
    public void testCreate() {

        new CompositeTokenTransformation(NAME, applier);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullApplier() {

        new CompositeTokenTransformation(NAME, null);
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullName() {

        new CompositeTokenTransformation(null, applier);
    }

    @Test
    public void testGetName() {

        assertEquals("the name should be set correctly.", NAME, new CompositeTokenTransformation(NAME, applier).getName());
    }

    @Test
    public void testApply() {

        final RuleContext context = mock(RuleContext.class);
        final Token token = mock(Token.class);

        new CompositeTokenTransformation(NAME, applier).apply(context, token, TEST_TEXT);

        verify(applier, times(1)).apply(context, token, TEST_TEXT);
    }

    @Test
    public void testApplyWithNullRuleContext() {

        new CompositeTokenTransformation(NAME, applier).apply(null, mock(Token.class), TEST_TEXT);

        verify(applier, times(1)).apply(isNull(RuleContext.class), any(Token.class), eq(TEST_TEXT));
    }

    @Test
    public void testApplyWithNullToken() {

        new CompositeTokenTransformation(NAME, applier).apply(mock(RuleContext.class), null, TEST_TEXT);

        verify(applier, times(1)).apply(any(RuleContext.class), isNull(Token.class), eq(TEST_TEXT));
    }

    @Test
    public void testApplyWithNullString() {

        new CompositeTokenTransformation(NAME, applier).apply(mock(RuleContext.class), mock(Token.class), null);

        verify(applier, times(1)).apply(any(RuleContext.class), any(Token.class), isNull(String.class));
    }

    @Test(expected = Exception.class)
    public void testApplyWithException() {

        when(applier.apply(any(RuleContext.class), any(Token.class), TEST_TEXT)).thenThrow(new Exception());

        new CompositeTokenTransformation(NAME, applier).apply(mock(RuleContext.class), mock(Token.class), TEST_TEXT);
    }
}
