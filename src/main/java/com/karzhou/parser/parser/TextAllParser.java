package com.karzhou.parser.parser;

import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.composite.ComponentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextAllParser implements TextParser {
    private static final Logger LOGGER = LogManager.getLogger(TextAllParser.class);
    private static final String SPLIT_ON_PARAGRAPHS_REGEXP = "\\s{4}";
    private ParagraphParser paragraphParser = new ParagraphParser();

    @Override
    public TextComponent parse(String text) {
        TextComponent textComponent = new TextComponent(ComponentType.TEXT);

        List<String> allStr = new ArrayList<>(Arrays.asList(text.split(SPLIT_ON_PARAGRAPHS_REGEXP)));

        for(String s : allStr){
            textComponent.addComponent(paragraphParser.parse(s));
        }

        LOGGER.info("Text was parsed on paragraphs");
        return textComponent;
    }

}
