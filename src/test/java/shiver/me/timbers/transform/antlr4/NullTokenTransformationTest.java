package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.NULL_TOKEN_TRANSFORMATION;

public class NullTokenTransformationTest {

    @Test
    public void testGetName() {

        assertEquals("the null Transformation name should be correct.", NullTokenTransformation.class.getSimpleName(),
                NULL_TOKEN_TRANSFORMATION.getName());
    }

    @Test
    public void testApply() {

        final String TEST_STRING = "test string";

        assertEquals("the null Transformation apply should do nothing.", TEST_STRING,
                NULL_TOKEN_TRANSFORMATION.apply(mock(RuleContext.class), mock(Token.class), TEST_STRING));
    }

    @Test
    public void testEquals() {

        assertTrue("the null Transformation equals should work.",
                NULL_TOKEN_TRANSFORMATION.equals(new NullTokenTransformation()));
    }

    @Test
    public void testEqualsWithNonNullTransformation() {

        assertFalse("the null Transformation equals should work with a non null Transformation.",
                NULL_TOKEN_TRANSFORMATION.equals(mock(TokenTransformation.class)));
    }

    @Test
    public void testEqualsWithNonTransformation() {

        assertFalse("the null Transformation equals should work with a non null Transformation.",
                NULL_TOKEN_TRANSFORMATION.equals(new Object()));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void testEqualsWithNull() {

        assertFalse("the null Transformation equals should work with a non null Transformation.",
                NULL_TOKEN_TRANSFORMATION.equals(null));
    }

    @Test
    public void testHashCode() {

        assertThat("the null Transformation hascode should return a non zero value.",
                NULL_TOKEN_TRANSFORMATION.hashCode(), not(0));
    }
}
