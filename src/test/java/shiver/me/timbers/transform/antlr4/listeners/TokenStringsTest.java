package shiver.me.timbers.transform.antlr4.listeners;

import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenStringsTest {

    private static final String TEST_TOKEN_STRING = "test token";
    private static final String TEST_MODIFIED_TOKEN_STRING = "test modified token";

    private Token token;

    @Before
    public void setUp() {

        token = mock(Token.class);
        when(token.getText()).thenReturn(TEST_TOKEN_STRING);
    }

    @Test
    public void testGetWithUnsetToken() {

        assertEquals("unset token should produce tokens string.", TEST_TOKEN_STRING, new TokenStrings().get(token));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullToken() {

        new TokenStrings().get(null);
    }

    @Test
    public void testSetWithModifiedString() {

        final TokenStrings tokenStrings = new TokenStrings();
        tokenStrings.set(token, TEST_MODIFIED_TOKEN_STRING);

        assertEquals("set token should produce modified string.", TEST_MODIFIED_TOKEN_STRING, tokenStrings.get(token));
    }

    @Test(expected = NullPointerException.class)
    public void testSetWithNullToken() {

        new TokenStrings().set(null, TEST_MODIFIED_TOKEN_STRING);
    }

    @Test
    public void testSetWithNullString() {

        final TokenStrings tokenStrings = new TokenStrings();
        tokenStrings.set(token, null);

        assertEquals("set token with null should produce tokens string.", TEST_TOKEN_STRING,
                new TokenStrings().get(token));
    }
}
