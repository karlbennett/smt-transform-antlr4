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

    private TokenApplyer applyer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {

        applyer = mock(TokenApplyer.class);
    }

    @Test
    public void testCreate() {

        new CompoundTransformations(mockIterable(NAMES), mock(TokenApplyer.class));
    }

    @Test
    @SuppressWarnings("UnusedDeclaration")
    public void testCreateWithEmptyIterable() {

        assertNoIterations(new CompoundTransformations(Collections.<String>emptySet(), applyer));
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullIterable() {

        new CompoundTransformations(null, mock(TokenApplyer.class));
    }

    @Test(expected = AssertionError.class)
    public void testCreateWithNullApplyer() {

        new CompoundTransformations(mockIterable(NAMES), null);
    }

    @Test
    public void testGetWithIndex() {

        assertTransformationsHaveCorrectNamesForIndices(new CompoundTransformations(mockIterable(NAMES), applyer));
    }

    @Test
    public void testGetWithInvalidIndex() {

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applyer);

        assertNullTransformation(transformations, -1);
        assertNullTransformation(transformations, NAMES.size());
    }

    @Test
    public void testGetWithName() {

        assertTransformationsHaveCorrectNamesForNames(new CompoundTransformations(mockIterable(NAMES), applyer));
    }

    @Test
    public void testGetWithInvalidName() {

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applyer);

        assertNullTransformation(transformations, "not a transformation");
    }

    @Test
    public void testGetWithNullName() {

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applyer);

        assertNullTransformation(transformations, null);
    }

    @Test
    public void testIteratorIsNotNull() {

        assertNotNull("an iterator should be returned",
                new CompoundTransformations(mockIterable(NAMES), applyer).iterator());
    }

    @Test
    public void testApplyer() {

        final String TEST_APPLY_STRING = "this is the apply string.";

        when(applyer.apply(any(RuleContext.class), any(Token.class), anyString())).thenReturn(TEST_APPLY_STRING);

        Transformations<TokenTransformation> transformations = new CompoundTransformations(mockIterable(NAMES), applyer);

        for (TokenTransformation transformation : transformations) {

            assertEquals("all transformations should produce the same apply result.", TEST_APPLY_STRING,
                    transformation.apply(mock(RuleContext.class), mock(Token.class), "input string"));
        }
    }
}
