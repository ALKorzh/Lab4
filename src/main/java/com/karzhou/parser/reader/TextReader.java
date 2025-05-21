package com.karzhou.parser.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Objects;
import java.util.stream.Stream;

public class TextReader {
    private static final Logger LOGGER = LogManager.getLogger(TextReader.class);
    private static ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    public static final String INPUT_DATA_PATH = "data/input.txt";

    public static String readAllText(String path){
        StringBuilder allText = new StringBuilder();

        File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());

        if(!file.exists()){
            LOGGER.fatal("File doesn't exist");
            throw new RuntimeException("File doesn't exist");
        }

        try(InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Stream<String> linesStream = reader.lines()){

                linesStream.filter( s -> !s.isEmpty()).forEach(allText::append);
                LOGGER.info("File was correctly read");
            }

        catch (IOException e) {
            LOGGER.fatal("Problems with reading file, on the next path: " + path, e);
            throw new RuntimeException("Problems with reading file, on the next path: " + path, e);
        }

        return allText.toString();
    }

}
