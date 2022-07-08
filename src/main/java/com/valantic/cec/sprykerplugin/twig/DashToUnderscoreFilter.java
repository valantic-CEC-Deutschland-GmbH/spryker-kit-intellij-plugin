package com.valantic.cec.sprykerplugin.twig;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashToUnderscoreFilter implements Filter {
    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) throws PebbleException {
        if (input == null) {
            return null;
        }
        String output;
        if (input instanceof String ) {
            output = ((String) input).replaceAll("/", "_");
        }
        else {
            output = input.toString().replaceAll("/","_");
        }

        return output;
    }

    @Override
    public List<String> getArgumentNames() {
        ArrayList<String> listOfArgs = new ArrayList<>();

        listOfArgs.add("stringToReplaceDashesWithUnderscoreIn");

        return listOfArgs;
    }
}
