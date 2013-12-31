package shiver.me.timbers.transform.antlr4;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.transform.antlr4.StaticNameListBuilder.staticName;

public class StaticNameListBuilderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testCreate() {

        new StaticNameListBuilder(mock(Iterable.class));
    }

    @Test(expected = AssertionError.class)
    @SuppressWarnings("unchecked")
    public void testCreateWithNullIterable() {

        new StaticNameListBuilder(null);
    }

    @Test
    public void testBuild() {

        final Collection<Class> classes = Arrays.<Class>asList(StaticNameClassOne.class, StaticNameClassTwo.class,
                StaticNameClassThree.class);

        final List<String> names = new StaticNameListBuilder(classes).build();

        assertThat("the correct number of names are returned.", names, hasSize(classes.size()));
        assertThat("the correct name list should be created.", names,
                contains(StaticNameClassOne.NAME, StaticNameClassTwo.NAME, StaticNameClassThree.NAME));
    }

    @Test
    public void testStaticName() {

        assertEquals("the static name should be correct.", StaticNameClassOne.NAME,
                staticName(StaticNameClassOne.class));
        assertEquals("the static name should be correct.", StaticNameClassTwo.NAME,
                staticName(StaticNameClassTwo.class));
        assertEquals("the static name should be correct.", StaticNameClassThree.NAME,
                staticName(StaticNameClassThree.class));
    }

    @Test(expected = RuntimeException.class)
    public void testStaticNameWithTypeWithNoStaticName() {

        staticName(CompositeTokenTransformation.class);
    }

    @Test(expected = RuntimeException.class)
    public void testStaticNameWithTypeWithPrivateStaticName() throws NoSuchFieldException {

        staticName(PrivateStaticName.class.getDeclaredField("NAME"));
    }

    @Test(expected = NullPointerException.class)
    public void testStaticNameWithNullType() {

        staticName((Class) null);
    }

    @Test(expected = NullPointerException.class)
    public void testStaticNameWithNullField() {

        staticName((Field) null);
    }

    private static class StaticNameClassOne {

        public static final String NAME = "name_one";

    }

    private static class StaticNameClassTwo {

        public static final String NAME = "name_two";

    }

    private static class StaticNameClassThree {

        public static final String NAME = "name_three";

    }

    private static class PrivateStaticName {

        @SuppressWarnings("UnusedDeclaration")
        private static final String NAME = "private_static_name";
    }
}
