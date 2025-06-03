package com.karzhou.parser;

import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.service.TextComponentCreator;
import com.karzhou.parser.service.TextOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;


public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final int MIN_WORDS_IN_SENTENCE = 3;

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting text processing...");

            LOGGER.info("Creating text component...");
            TextComponent textComponent = TextComponentCreator.create();

            LOGGER.info("\n=== Original text ===");
            LOGGER.info(textComponent.getTextMessage());

            LOGGER.info("\n=== Sorting paragraphs by number of sentences ===");
            TextComponent sortedParagraphs = TextOperation.sortParagraphsByNumOFSentences(textComponent);
            LOGGER.info(sortedParagraphs.getTextMessage());

            LOGGER.info("\n=== Finding sentences with longest word ===");
            List<TextComponent> sentencesWithLongestWord = TextOperation.findSentencesWithLongestWord(textComponent);
            for (TextComponent sentence : sentencesWithLongestWord) {
                LOGGER.info(sentence.getTextMessage());
            }

            LOGGER.info("\n=== Removing sentences with less than " + MIN_WORDS_IN_SENTENCE + " words ===");
            TextComponent filteredText = TextOperation.removeSentencesWithLessWords(textComponent, MIN_WORDS_IN_SENTENCE);
            LOGGER.info(filteredText.getTextMessage());

            LOGGER.info("\n=== Finding duplicate words ===");
            Map<String, Integer> duplicateWords = TextOperation.countDuplicateWords(textComponent);
            for (Map.Entry<String, Integer> entry : duplicateWords.entrySet()) {
                LOGGER.info("Word: '" + entry.getKey() + "' appears " + entry.getValue() + " times");
            }

            LOGGER.info("\n=== Counting vowels and consonants in sentences ===");
            Map<TextComponent, Map<String, Integer>> vowelsAndConsonants = TextOperation.countVowelsAndConsonants(textComponent);
            for (Map.Entry<TextComponent, Map<String, Integer>> entry : vowelsAndConsonants.entrySet()) {
                LOGGER.info("Sentence: " + entry.getKey().getTextMessage());
                LOGGER.info("Vowels: " + entry.getValue().get("vowels") + 
                          ", Consonants: " + entry.getValue().get("consonants"));
            }
            
            LOGGER.info("\nText processing completed successfully!");
            
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing text: " + e.getMessage());
            LOGGER.error("Stack trace:", e);
            System.exit(1);
        }
    }
} 