package com.valantic.cec.sprykerplugin.services;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.valantic.cec.sprykerplugin.services.datatransfer.DocBlockItem;

import java.util.ArrayList;

/**
 * Derived from PhpClassRendererInterface from
 * Spryker plugin https://github.com/tobi812/idea-php-spryker-plugin
 */
public interface PhpClassStringGeneratorInterface {
    String getFactoryMethodString(PhpClass phpClass);

    String getDocBlockString(ArrayList<DocBlockItem> docBlockItems);
}
