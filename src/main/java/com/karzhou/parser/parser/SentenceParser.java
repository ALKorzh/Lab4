package com.karzhou.parser.parser;

import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.composite.ComponentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SentenceParser implements TextParser {
    private static final Logger LOGGER = LogManager.getLogger(SentenceParser.class);
    private static final String SPLIT_ON_LEXEME_REGEXP = "\\s+(?=[\\w-(])";
    private LexemeParser lexemeParser = new LexemeParser();

    @Override
    public TextComponent parse(String sentence) {
        TextComponent textComponent = new TextComponent(ComponentType.SENTENCE);

        List<String> allStr = new ArrayList<>(Arrays.asList(sentence.split(SPLIT_ON_LEXEME_REGEXP)));
        allStr.removeIf(o -> o.equals(""));

        for(String s : allStr){
            textComponent.addComponent(lexemeParser.parse(s));
        }

        LOGGER.info("Sentence was parsed on lexemes");

        return textComponent;
    }

}
