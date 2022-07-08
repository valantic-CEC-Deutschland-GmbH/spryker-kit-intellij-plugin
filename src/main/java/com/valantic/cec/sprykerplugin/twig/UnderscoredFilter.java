package com.valantic.cec.sprykerplugin.twig;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnderscoredFilter implements Filter {
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
        boolean isFirstChar = true;
        StringBuilder builder = new StringBuilder();
        for (char c : helpString.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (!isFirstChar) {
                    builder.append('_');
                }
                builder.append(Character.toLowerCase(c));
            }
            else {
                builder.append(c);
            }
            if (isFirstChar) {
                isFirstChar = false;
            }
        }
        return builder.toString();
    }

    @Override
    public List<String> getArgumentNames() {
        ArrayList<String> listOfArgs = new ArrayList<>();

        listOfArgs.add("stringToConvertToUnderscored");

        return listOfArgs;
    }
}
