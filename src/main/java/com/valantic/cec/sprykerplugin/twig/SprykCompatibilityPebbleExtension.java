package com.valantic.cec.sprykerplugin.twig;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.HashMap;
import java.util.Map;

public class SprykCompatibilityPebbleExtension extends AbstractExtension {
    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = super.getFilters();

        if (filters == null) {
            filters = new HashMap<>();
        }

        filters.put("classNameShort", new ClassShortNameFilter());
        filters.put("dasherize", new DasherizeFilter());
        filters.put("ensureConsoleSuffix", new EnsureConsoleSuffixFilter());
        filters.put("camelBack", new CamelBackFilter());
        filters.put("underscored", new UnderscoredFilter());
        filters.put("underscoretoslash", new UnderscoreToSlashFilter());
        filters.put("lcfirst", new LowerCaseFirstFilter());
        filters.put("camelCased", new CamelCaseFilter());
        filters.put("dashToCamelCase", new DashToCamelCaseFilter());
        filters.put("dashToUnderscore", new DashToUnderscoreFilter());

        return filters;
    }

}
