package com.karzhou.parser.parser;

import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.composite.ComponentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParagraphParser implements TextParser {
    private static final Logger LOGGER = LogManager.getLogger(ParagraphParser.class);
    private static final String SPLIT_ON_SENTENCES_REGEXP = "(?<=[.{1}!?])\\s";
    private SentenceParser sentenceParser = new SentenceParser();

    @Override
    public TextComponent parse(String paragraph) {
        TextComponent textComponent = new TextComponent(ComponentType.PARAGRAPH);

        List<String> allStr = new ArrayList<>(Arrays.asList(paragraph.split(SPLIT_ON_SENTENCES_REGEXP)));

        for(String s : allStr){
            textComponent.addComponent(sentenceParser.parse(s));
        }

        LOGGER.info("Paragraph was parsed on sentences");

        return textComponent;
    }

}
