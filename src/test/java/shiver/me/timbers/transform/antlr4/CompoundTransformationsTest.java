package shiver.me.timbers.transform.antlr4;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.Before;
import org.junit.Test;
import shiver.me.timbers.transform.Transformations;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.antlr4.TestUtils.NAMES;
import static shiver.me.timbers.transform.antlr4.TestUtils.assertNoIterations;
import static shiver.me.timbers.transform.antlr4.TestUtils.assertNullTransformation;
import static shiver.me.timbers.transform.antlr4.TestUtils.assertTransformationsHaveCorrectNamesForIndices;
import static shiver.me.timbers.transform.antlr4.TestUtils.assertTransformationsHaveCorrectNamesForNames;
import static shiver.me.timbers.transform.antlr4.TestUtils.mockIterable;

public class CompoundTransformationsTest {

    private TokenApplier applier;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        applier = mock(TokenApplier.class);
    }

    @Test
    public void testCreate() {

        new CompoundTransformations(mockIterable(NAMES), mock(TokenApplier.class));
    }

    @Test
    @SuppressWarnings("UnusedDeclaration")
    public void testCreateWithEmptyIterable() {

        assertNoIterations(new CompoundTransformations(Collections.<String>emptySet(), applier));
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullIterable() {

        new CompoundTransformations(null, mock(TokenApplier.class));
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullApplier() {

        new CompoundTransformations(mockIterable(NAMES), null);
    }

    @Test
    public void testGetWithIndex() {

        assertTransformationsHaveCorrectNamesForIndices(new CompoundTransformations(mockIterable(NAMES), applier));
    }

    @Test
    public void testGetWithInvalidIndex() {

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applier);

        assertNullTransformation(transformations, -1);
        assertNullTransformation(transformations, NAMES.size());
    }

    @Test
    public void testGetWithName() {

        assertTransformationsHaveCorrectNamesForNames(new CompoundTransformations(mockIterable(NAMES), applier));
    }

    @Test
    public void testGetWithInvalidName() {

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applier);

        assertNullTransformation(transformations, "not a transformation");
    }

    @Test
    public void testGetWithNullName() {

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applier);

        assertNullTransformation(transformations, null);
    }

    @Test
    public void testIteratorIsNotNull() {

        assertNotNull("an iterator should be returned",
                new CompoundTransformations(mockIterable(NAMES), applier).iterator());
    }

    @Test
    public void testApplier() {

        final String TEST_APPLY_STRING = "this is the apply string.";

        when(applier.apply(any(RuleContext.class), any(Token.class), anyString())).thenReturn(TEST_APPLY_STRING);

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applier);

        for (TokenTransformation transformation : transformations) {

            assertEquals("all transformations should produce the same apply result.", TEST_APPLY_STRING,
                    transformation.apply(mock(RuleContext.class), mock(Token.class), "input string"));
        }
    }
}
