package com.karzhou.parser.service;

import com.karzhou.parser.composite.CommonText;
import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.composite.ComponentType;
import com.karzhou.parser.composite.impl.TextLetter;
import com.karzhou.parser.composite.impl.TextSign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;


public class TextOperation {
    private static final Logger LOGGER = LogManager.getLogger(TextOperation.class);
    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y')); // не совсем конст

    public static TextComponent sortParagraphsByNumOFSentences(TextComponent text) {
            TextComponent result = new TextComponent(ComponentType.TEXT);

            if (text == null) {
                LOGGER.error("text can't be null");
                return result;
            }

            List<CommonText> paragraphs = new ArrayList<>();
            for (int i = 0; i < text.getComponentsSize(); i++) {
                paragraphs.add(text.getComponent(i));
            }
            paragraphs = paragraphs.stream().sorted(Comparator.comparingInt(CommonText::getComponentsSize)).collect(Collectors.toList());
            for (CommonText paragraph : paragraphs) {
                result.addComponent(paragraph);
            }

            LOGGER.info("Paragraphs was sorted by number of sentences");

            return result;
        }

    public static TextComponent sortByLengthOfWordsText(TextComponent text) {
        TextComponent result = new TextComponent(ComponentType.TEXT);

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }


        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            TextComponent resultParagraph = new TextComponent(ComponentType.PARAGRAPH);

            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                TextComponent resultSentence = new TextComponent(ComponentType.SENTENCE);

                List<TextComponent> allWords = new ArrayList<>();
                List<TextSign> allSigns = new ArrayList<>();
                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    CommonText lexeme = sentence.getComponent(k);
                    for(int l = 0; l < lexeme.getComponentsSize(); l++){
                        if(lexeme.getComponent(l) != null && lexeme.getComponent(l) instanceof TextComponent && lexeme.getComponent(l).getTypeOfTextComponent().equals(ComponentType.WORD)){
                            allWords.add((TextComponent)lexeme.getComponent(l));
                        }
                        else {
                            allSigns.add((TextSign) lexeme.getComponent(l));
                        }
                    }
                }
                allWords.sort(((o1, o2) -> {
                    int counter1 = 0;
                    int counter2 = 0;
                    for(int s = 0; s < o1.getComponentsSize(); s++){
                        if(o1.getComponent(s) instanceof TextLetter){
                            counter1++;
                        }
                    }
                    for(int s = 0; s < o2.getComponentsSize(); s++){
                        if(o2.getComponent(s) instanceof TextLetter){
                            counter2++;
                        }
                    }
                    return counter1 - counter2;
                }));

                for(TextComponent textComponent : allWords){
                    resultSentence.addComponent(textComponent);
                }
                for(TextSign sign : allSigns){
                    resultSentence.addComponent(sign);
                }
                resultParagraph.addComponent(resultSentence);
                }

                result.addComponent(resultParagraph);
            }

        LOGGER.info("Sentences was sorted by length of tokens");

        return result;
    }

    public static TextComponent reverseSortLexemesByOrderSymbol(TextComponent text, String searchSymbol){
        TextComponent result = new TextComponent(ComponentType.TEXT);

        if(text == null){
            LOGGER.error("text can't be null");
            return result;
        }

        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            TextComponent resultParagraph = new TextComponent(ComponentType.PARAGRAPH);

            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                TextComponent resultSentence = new TextComponent(ComponentType.SENTENCE);

                List<CommonText> allLexemes = new ArrayList<>();
                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    allLexemes.add(sentence.getComponent(k));
                }
                allLexemes.sort(Comparator.comparing( o -> ((CommonText) o).countOfOrderedSymbol(searchSymbol))
                        .thenComparing( (e1, e2) -> ((CommonText) e2).getTextMessage().compareToIgnoreCase(((CommonText) e1).getTextMessage())));
                Collections.reverse(allLexemes);

                for(CommonText lexeme : allLexemes){
                    resultSentence.addComponent(lexeme);
                }
                resultParagraph.addComponent(resultSentence);
            }

            result.addComponent(resultParagraph);
        }

        LOGGER.info("Tokens was sorted in reverse order by number of mentioned symbols");

        return result;
    }

    public static List<TextComponent> findSentencesWithLongestWord(TextComponent text) {
        List<TextComponent> result = new ArrayList<>();
        int maxWordLength = 0;

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }

        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    CommonText lexeme = sentence.getComponent(k);
                    for (int l = 0; l < lexeme.getComponentsSize(); l++) {
                        if (lexeme.getComponent(l) instanceof TextComponent && 
                            lexeme.getComponent(l).getTypeOfTextComponent().equals(ComponentType.WORD)) {
                            int wordLength = countLetters((TextComponent) lexeme.getComponent(l));
                            maxWordLength = Math.max(maxWordLength, wordLength);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                boolean hasLongestWord = false;

                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    CommonText lexeme = sentence.getComponent(k);
                    for (int l = 0; l < lexeme.getComponentsSize(); l++) {
                        if (lexeme.getComponent(l) instanceof TextComponent && 
                            lexeme.getComponent(l).getTypeOfTextComponent().equals(ComponentType.WORD)) {
                            int wordLength = countLetters((TextComponent) lexeme.getComponent(l));
                            if (wordLength == maxWordLength) {
                                hasLongestWord = true;
                                break;
                            }
                        }
                    }
                    if (hasLongestWord) break;
                }

                if (hasLongestWord) {
                    result.add((TextComponent) sentence);
                }
            }
        }

        LOGGER.info("Found sentences with longest word (length: " + maxWordLength + ")");
        return result;
    }

    public static TextComponent removeSentencesWithLessWords(TextComponent text, int minWords) {
        TextComponent result = new TextComponent(ComponentType.TEXT);

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }

        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            TextComponent resultParagraph = new TextComponent(ComponentType.PARAGRAPH);

            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                int wordCount = countWords(sentence);

                if (wordCount >= minWords) {
                    resultParagraph.addComponent(sentence);
                }
            }

            if (resultParagraph.getComponentsSize() > 0) {
                result.addComponent(resultParagraph);
            }
        }

        LOGGER.info("Removed sentences with less than " + minWords + " words");
        return result;
    }


    public static Map<String, Integer> countDuplicateWords(TextComponent text) {
        Map<String, Integer> wordCount = new HashMap<>();

        if (text == null) {
            LOGGER.error("text can't be null");
            return wordCount;
        }

        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    CommonText lexeme = sentence.getComponent(k);
                    for (int l = 0; l < lexeme.getComponentsSize(); l++) {
                        if (lexeme.getComponent(l) instanceof TextComponent && 
                            lexeme.getComponent(l).getTypeOfTextComponent().equals(ComponentType.WORD)) {
                            String word = lexeme.getComponent(l).getTextMessage().toLowerCase();
                            wordCount.merge(word, 1, Integer::sum);
                        }
                    }
                }
            }
        }

        wordCount.entrySet().removeIf(entry -> entry.getValue() <= 1);

        LOGGER.info("Found " + wordCount.size() + " duplicate words");
        return wordCount;
    }


    public static Map<TextComponent, Map<String, Integer>> countVowelsAndConsonants(TextComponent text) {
        Map<TextComponent, Map<String, Integer>> result = new HashMap<>();

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }


        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                int vowels = 0;
                int consonants = 0;


                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    CommonText lexeme = sentence.getComponent(k);
                    for (int l = 0; l < lexeme.getComponentsSize(); l++) {
                        if (lexeme.getComponent(l) instanceof TextComponent && 
                            lexeme.getComponent(l).getTypeOfTextComponent().equals(ComponentType.WORD)) {
                            TextComponent word = (TextComponent) lexeme.getComponent(l);
                            for (int m = 0; m < word.getComponentsSize(); m++) {
                                if (word.getComponent(m) instanceof TextLetter) {
                                    char letter = Character.toLowerCase(word.getComponent(m).getTextMessage().charAt(0));
                                    if (Character.isLetter(letter)) {
                                        if (VOWELS.contains(letter)) {
                                            vowels++;
                                        } else {
                                            consonants++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Map<String, Integer> counts = new HashMap<>();
                counts.put("vowels", vowels);
                counts.put("consonants", consonants);
                result.put((TextComponent) sentence, counts);
            }
        }

        LOGGER.info("Counted vowels and consonants in " + result.size() + " sentences");
        return result;
    }

    private static int countLetters(TextComponent word) {
        int count = 0;
        for (int i = 0; i < word.getComponentsSize(); i++) {
            if (word.getComponent(i) instanceof TextLetter) {
                count++;
            }
        }
        return count;
    }

    private static int countWords(CommonText sentence) {
        int count = 0;
        for (int i = 0; i < sentence.getComponentsSize(); i++) {
            CommonText lexeme = sentence.getComponent(i);
            for (int j = 0; j < lexeme.getComponentsSize(); j++) {
                if (lexeme.getComponent(j) instanceof TextComponent && 
                    lexeme.getComponent(j).getTypeOfTextComponent().equals(ComponentType.WORD)) {
                    count++;
                }
            }
        }
        return count;
    }
}













