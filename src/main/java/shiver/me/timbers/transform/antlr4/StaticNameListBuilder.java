package shiver.me.timbers.transform.antlr4;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static shiver.me.timbers.asserts.Asserts.argumentIsNullMessage;
import static shiver.me.timbers.asserts.Asserts.assertIsNotNull;

/**
 * This class can be used to build a list of transformation names from a collection of transformation types.
 */
public class StaticNameListBuilder {

    private final Iterable<Class> types;

    public StaticNameListBuilder(Iterable<Class> types) {

        assertIsNotNull(argumentIsNullMessage("types"), types);

        this.types = types;
    }

    /**
     * @return the list of names for the supplied transformation types.
     */
    public List<String> build() {

        final List<String> names = new ArrayList<String>();

        for (Class type : types) {

            names.add(staticName(type));
        }

        return names;
    }

    static String staticName(java.lang.Class type) {

        try {

            final Field nameField = type.getField("NAME");

            return staticName(nameField);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);
        }
    }

    static String staticName(Field nameField) {

        try {

            return nameField.get(null).toString();

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }
}
