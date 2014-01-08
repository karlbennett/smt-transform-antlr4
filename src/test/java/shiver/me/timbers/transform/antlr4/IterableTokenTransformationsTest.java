package shiver.me.timbers.transform.antlr4;

import org.junit.Test;

import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.*;

public class IterableTokenTransformationsTest {

    @Test
    public void testCreate() {

        new IterableTokenTransformations();
    }

    @Test
    public void testCreateWithIterable() {

        new IterableTokenTransformations(TestUtils.<TokenTransformation>mockIterable(NULL_TOKEN_TRANSFORMATION));
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullIterable() {

        new IterableTokenTransformations(null);
    }
}
