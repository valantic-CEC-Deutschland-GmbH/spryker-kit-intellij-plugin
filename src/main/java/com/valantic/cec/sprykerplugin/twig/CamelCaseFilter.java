package com.valantic.cec.sprykerplugin.twig;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CamelCaseFilter implements Filter {
    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException {
        if (input == null) {
            return null;
        }
        String helpString;
        if (input instanceof String ) {
            helpString = ((String) input);
        }
        else {
            helpString = input.toString();
        }
        StringBuilder builder = new StringBuilder();
        boolean nextCharToUpper = true; // starting with upper case (uppercase camel case)
        for (char c : helpString.toCharArray()) {
            if (c == '_') {
                nextCharToUpper = true;
            }
            else {
                builder.append(nextCharToUpper ? Character.toUpperCase(c): Character.toLowerCase(c));
                nextCharToUpper = false;
            }
        }
        return builder.toString();
    }

    @Override
    public List<String> getArgumentNames() {
        ArrayList<String> listOfArgs = new ArrayList<>();

        listOfArgs.add("stringSeparatedByUnderscoresToTransformToCamelCase");

        return listOfArgs;
    }
}
