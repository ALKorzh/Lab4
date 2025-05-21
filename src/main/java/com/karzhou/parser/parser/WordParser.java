package com.karzhou.parser.parser;

import com.karzhou.parser.composite.CommonText;
import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.composite.ComponentType;
import com.karzhou.parser.composite.impl.TextLetter;
import com.karzhou.parser.composite.impl.TextSign;


public class WordParser implements TextParser {
    private static final String CHECK_LETTER_REGEXP = "\\w";

    @Override
    public CommonText parse(String word) {
        TextComponent textComponent = new TextComponent(ComponentType.WORD);

        for (int i = 0; i < word.length(); i++) {
            if(String.valueOf(word.charAt(i)).matches(CHECK_LETTER_REGEXP)){
                textComponent.addComponent(new TextLetter(word.charAt(i)));
            }
            else {
                textComponent.addComponent(new TextSign(word.charAt(i)));
            }
        }

        return textComponent;
    }



}
