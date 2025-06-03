package com.karzhou.parser.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.stream.Stream;

public class TextReader {
    private static final Logger LOGGER = LogManager.getLogger(TextReader.class);
    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    public static final String INPUT_DATA_PATH = "data/input.txt";

    public static String readAllText(String path) {
        StringBuilder allText = new StringBuilder();

        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null) {
                LOGGER.fatal("File not found in resources: " + path);
                throw new RuntimeException("File not found in resources: " + path);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                 Stream<String> linesStream = reader.lines()) {

                linesStream.filter(s -> !s.isEmpty()).forEach(allText::append);
                LOGGER.info("File was correctly read");
            }
        } catch (IOException e) {
            LOGGER.fatal("Problems with reading file from resources: " + path, e);
            throw new RuntimeException("Problems with reading file from resources: " + path, e);
        }

        return allText.toString();
    }
}
