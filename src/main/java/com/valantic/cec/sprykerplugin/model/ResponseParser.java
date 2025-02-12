package com.valantic.cec.sprykerplugin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseParser {
    public static void main(String[] args) {
        String response = "class names:\n" +
            "________________________________________________________________________________\n" +
            "\\Pyz\\Zed\\Navigation\\Business\\Navigation\\NavigationBusinessFactory\n" +
            "\\Pyz\\Zed\\Navigation\\Business\\Navigation\\NavigationReader\n" +
            "________________________________________________________________________________\n" +
            "\n" +
            "source code:\n" +
            "________________________________________________________________________________\n" +
            "\n" +
            "namespace Pyz\\Zed\\Navigation\\Business\\Navigation;\n" +
            "\n" +
            "use Spryker\\Zed\\Navigation\\Business\\Navigation\\NavigationBusinessFactory as SprykerNavigationBusinessFactory;\n" +
            "\n" +
            "class NavigationBusinessFactory extends SprykerNavigationBusinessFactory\n" +
            "{\n" +
            "    /**\n" +
            "     * @return \\Pyz\\Zed\\Navigation\\Business\\Navigation\\NavigationReader\n" +
            "     */\n" +
            "    public function createNavigationReader()\n" +
            "    {\n" +
            "        return new NavigationReader(\n" +
            "            $this->getRepository(),\n" +
            "            $this->getGlossaryFacade(),\n" +
            "            $this->createNavigationNodeReaderPlugin()\n" +
            "        );\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "namespace Pyz\\Zed\\Navigation\\Business\\Navigation;\n" +
            "\n" +
            "use Spryker\\Zed\\Navigation\\Business\\Navigation\\NavigationReader as SprykerNavigationReader;\n" +
            "\n" +
            "class NavigationReader extends SprykerNavigationReader\n" +
            "{\n" +
            "    // Add custom methods or override existing ones as needed\n" +
            "\n" +
            "    /**\n" +
            "     * @inheritdoc\n" +
            "     */\n" +
            "    protected function getLocalizedNavigationNodeData(array $navigationNodeData)\n" +
            "    {\n" +
            "        // Custom implementation for retrieving localized navigation node data\n" +
            "\n" +
            "        // Example:\n" +
            "        $locale = $this->getCurrentLocale();\n" +
            "        if (isset($navigationNodeData[$locale])) {\n" +
            "            return $navigationNodeData[$locale];\n" +
            "        }\n" +
            "\n" +
            "        return [];\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @inheritdoc\n" +
            "     */\n" +
            "    protected function getNavigationNodeUrl(array $navigationNode)\n" +
            "    {\n" +
            "        // Custom implementation for retrieving the URL of a navigation node\n" +
            "\n" +
            "        // Example:\n" +
            "        $url = parent::getNavigationNodeUrl($navigationNode);\n" +
            "\n" +
            "        // Modify the URL or add custom logic here\n" +
            "\n" +
            "        return $url;\n" +
            "}\n" +
        "}\n";

        List<String> classNames = extractClassNames(response);
        List<String> sourceCodes = extractSourceCode(response);

        System.out.println("Class Names:");
        for (String className : classNames) {
            System.out.println(className);
        }

        System.out.println("\nSource Codes:");
        for (String sourceCode : sourceCodes) {
            System.out.println(sourceCode);
        }
    }

    private static List<String> extractClassNames(String response) {
        List<String> classNames = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\\\[A-Za-z\\\\]+");
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            classNames.add(matcher.group());
        }

        return classNames;
    }

    private static List<String> extractSourceCode(String response) {
        List<String> sourceCodes = new ArrayList<>();

        Pattern pattern = Pattern.compile("namespace [A-Za-z\\\\]+;.+(?=\\n\\n)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            sourceCodes.add(matcher.group());
        }

        return sourceCodes;
    }
}
