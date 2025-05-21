package com.karzhou.parser;

import com.karzhou.parser.composite.impl.TextComponent;
import com.karzhou.parser.service.TextComponentCreator;
import com.karzhou.parser.service.TextOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Главный класс приложения для обработки текста.
 * Реализует различные операции с текстом: сортировку, поиск, подсчет и т.д.
 */
public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    // Минимальное количество слов, которое должно быть в предложении
    private static final int MIN_WORDS_IN_SENTENCE = 3;

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting text processing...");
            
            // Создаем компонент текста из исходного файла
            LOGGER.info("Creating text component...");
            TextComponent textComponent = TextComponentCreator.create();
            
            // 1. Выводим исходный текст для проверки
            LOGGER.info("\n=== Original text ===");
            LOGGER.info(textComponent.getTextMessage());
            
            // 2. Сортируем параграфы по количеству предложений в них
            // Параграфы с меньшим количеством предложений будут первыми
            LOGGER.info("\n=== Sorting paragraphs by number of sentences ===");
            TextComponent sortedParagraphs = TextOperation.sortParagraphsByNumOFSentences(textComponent);
            LOGGER.info(sortedParagraphs.getTextMessage());
            
            // 3. Находим все предложения, содержащие самое длинное слово в тексте
            // Если несколько слов имеют одинаковую максимальную длину, будут найдены все предложения с такими словами
            LOGGER.info("\n=== Finding sentences with longest word ===");
            List<TextComponent> sentencesWithLongestWord = TextOperation.findSentencesWithLongestWord(textComponent);
            for (TextComponent sentence : sentencesWithLongestWord) {
                LOGGER.info(sentence.getTextMessage());
            }
            
            // 4. Удаляем из текста все предложения, содержащие меньше заданного количества слов
            // Это помогает отфильтровать короткие предложения
            LOGGER.info("\n=== Removing sentences with less than " + MIN_WORDS_IN_SENTENCE + " words ===");
            TextComponent filteredText = TextOperation.removeSentencesWithLessWords(textComponent, MIN_WORDS_IN_SENTENCE);
            LOGGER.info(filteredText.getTextMessage());
            
            // 5. Находим все повторяющиеся слова в тексте и подсчитываем их количество
            // Регистр букв не учитывается при поиске дубликатов
            LOGGER.info("\n=== Finding duplicate words ===");
            Map<String, Integer> duplicateWords = TextOperation.countDuplicateWords(textComponent);
            for (Map.Entry<String, Integer> entry : duplicateWords.entrySet()) {
                LOGGER.info("Word: '" + entry.getKey() + "' appears " + entry.getValue() + " times");
            }
            
            // 6. Подсчитываем количество гласных и согласных букв в каждом предложении
            // Это помогает анализировать фонетический состав текста
            LOGGER.info("\n=== Counting vowels and consonants in sentences ===");
            Map<TextComponent, Map<String, Integer>> vowelsAndConsonants = TextOperation.countVowelsAndConsonants(textComponent);
            for (Map.Entry<TextComponent, Map<String, Integer>> entry : vowelsAndConsonants.entrySet()) {
                LOGGER.info("Sentence: " + entry.getKey().getTextMessage());
                LOGGER.info("Vowels: " + entry.getValue().get("vowels") + 
                          ", Consonants: " + entry.getValue().get("consonants"));
            }
            
            LOGGER.info("\nText processing completed successfully!");
            
        } catch (Exception e) {
            // Обработка ошибок с выводом полного стека вызовов
            LOGGER.error("Error occurred while processing text: " + e.getMessage());
            LOGGER.error("Stack trace:", e);
            System.exit(1);
        }
    }
} 