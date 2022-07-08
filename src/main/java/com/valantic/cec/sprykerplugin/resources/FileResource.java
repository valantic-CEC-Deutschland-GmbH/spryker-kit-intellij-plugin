package com.valantic.cec.sprykerplugin.resources;

import java.io.*;

public class FileResource {
    public static String readFileContentFromResources(String resourceFileName) {
        InputStream resource = FileResource.class.getClassLoader().getResourceAsStream(resourceFileName);

        if (resource == null) {
            try {
                resource = new FileInputStream("../main/resources/" + resourceFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("file is not found!");
            }
        }

        StringBuilder fileContent = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {
            InputStreamReader reader = new InputStreamReader(resource);
            bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileContent.toString();
    }
}
