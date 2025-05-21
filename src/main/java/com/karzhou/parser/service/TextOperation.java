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

/**
 * Класс, содержащий основные операции для обработки текста.
 * Включает методы для сортировки, поиска, фильтрации и анализа текста.
 */
public class TextOperation {
    private static final Logger LOGGER = LogManager.getLogger(TextOperation.class);
    // Множество гласных букв английского алфавита
    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u', 'y'));

    /**
     * Сортирует параграфы текста по количеству предложений в них.
     * Параграфы с меньшим количеством предложений будут первыми.
     *
     * @param text Исходный текст для сортировки
     * @return Отсортированный текст
     */
    public static TextComponent sortParagraphsByNumOFSentences(TextComponent text) {
            TextComponent result = new TextComponent(ComponentType.TEXT);

            if (text == null) {
                LOGGER.error("text can't be null");
                return result;
            }

            // Собираем все параграфы в список
            List<CommonText> paragraphs = new ArrayList<>();
            for (int i = 0; i < text.getComponentsSize(); i++) {
                paragraphs.add(text.getComponent(i));
            }
            // Сортируем параграфы по количеству предложений
            paragraphs = paragraphs.stream().sorted(Comparator.comparingInt(CommonText::getComponentsSize)).collect(Collectors.toList());
            // Создаем новый текст из отсортированных параграфов
            for (CommonText paragraph : paragraphs) {
                result.addComponent(paragraph);
            }

            LOGGER.info("Paragraphs was sorted by number of sentences");

            return result;
        }

    /**
     * Сортирует слова в предложениях по их длине.
     * Знаки препинания сохраняются на своих местах.
     *
     * @param text Исходный текст для сортировки
     * @return Текст с отсортированными словами
     */
    public static TextComponent sortByLengthOfWordsText(TextComponent text) {
        TextComponent result = new TextComponent(ComponentType.TEXT);

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }

        // Обрабатываем каждый параграф
        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            TextComponent resultParagraph = new TextComponent(ComponentType.PARAGRAPH);

            // Обрабатываем каждое предложение
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                TextComponent resultSentence = new TextComponent(ComponentType.SENTENCE);

                // Разделяем слова и знаки препинания
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
                // Сортируем слова по длине
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

                // Собираем предложение обратно
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

    /**
     * Сортирует лексемы в предложениях по количеству вхождений заданного символа.
     * При равном количестве вхождений сортировка производится в обратном алфавитном порядке.
     *
     * @param text Исходный текст
     * @param searchSymbol Символ для подсчета вхождений
     * @return Текст с отсортированными лексемами
     */
    public static TextComponent reverseSortLexemesByOrderSymbol(TextComponent text, String searchSymbol){
        TextComponent result = new TextComponent(ComponentType.TEXT);

        if(text == null){
            LOGGER.error("text can't be null");
            return result;
        }

        // Обрабатываем каждый параграф
        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            TextComponent resultParagraph = new TextComponent(ComponentType.PARAGRAPH);

            // Обрабатываем каждое предложение
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                TextComponent resultSentence = new TextComponent(ComponentType.SENTENCE);

                // Собираем все лексемы
                List<CommonText> allLexemes = new ArrayList<>();
                for (int k = 0; k < sentence.getComponentsSize(); k++) {
                    allLexemes.add(sentence.getComponent(k));
                }
                // Сортируем лексемы по количеству вхождений символа и в обратном алфавитном порядке
                allLexemes.sort(Comparator.comparing( o -> ((CommonText) o).countOfOrderedSymbol(searchSymbol))
                        .thenComparing( (e1, e2) -> ((CommonText) e2).getTextMessage().compareToIgnoreCase(((CommonText) e1).getTextMessage())));
                Collections.reverse(allLexemes);

                // Собираем предложение обратно
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

    /**
     * Находит все предложения, содержащие самое длинное слово в тексте.
     * Если несколько слов имеют одинаковую максимальную длину, будут найдены все предложения с такими словами.
     *
     * @param text Исходный текст
     * @return Список предложений, содержащих самое длинное слово
     */
    public static List<TextComponent> findSentencesWithLongestWord(TextComponent text) {
        List<TextComponent> result = new ArrayList<>();
        int maxWordLength = 0;

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }

        // Находим максимальную длину слова
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

        // Находим предложения с максимальной длиной слова
        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                boolean hasLongestWord = false;
                
                // Проверяем каждое слово в предложении
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
                
                // Если нашли самое длинное слово, добавляем предложение в результат
                if (hasLongestWord) {
                    result.add((TextComponent) sentence);
                }
            }
        }

        LOGGER.info("Found sentences with longest word (length: " + maxWordLength + ")");
        return result;
    }

    /**
     * Удаляет из текста все предложения, содержащие меньше заданного количества слов.
     *
     * @param text Исходный текст
     * @param minWords Минимальное количество слов в предложении
     * @return Текст без коротких предложений
     */
    public static TextComponent removeSentencesWithLessWords(TextComponent text, int minWords) {
        TextComponent result = new TextComponent(ComponentType.TEXT);

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }

        // Обрабатываем каждый параграф
        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            TextComponent resultParagraph = new TextComponent(ComponentType.PARAGRAPH);

            // Проверяем каждое предложение
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                int wordCount = countWords(sentence);
                
                // Оставляем только предложения с достаточным количеством слов
                if (wordCount >= minWords) {
                    resultParagraph.addComponent(sentence);
                }
            }

            // Добавляем параграф только если в нем остались предложения
            if (resultParagraph.getComponentsSize() > 0) {
                result.addComponent(resultParagraph);
            }
        }

        LOGGER.info("Removed sentences with less than " + minWords + " words");
        return result;
    }

    /**
     * Подсчитывает количество вхождений каждого слова в тексте.
     * Регистр букв не учитывается.
     *
     * @param text Исходный текст
     * @return Карта слов и количества их вхождений
     */
    public static Map<String, Integer> countDuplicateWords(TextComponent text) {
        Map<String, Integer> wordCount = new HashMap<>();

        if (text == null) {
            LOGGER.error("text can't be null");
            return wordCount;
        }

        // Подсчитываем вхождения каждого слова
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

        // Оставляем только слова, которые встречаются более одного раза
        wordCount.entrySet().removeIf(entry -> entry.getValue() <= 1);

        LOGGER.info("Found " + wordCount.size() + " duplicate words");
        return wordCount;
    }

    /**
     * Подсчитывает количество гласных и согласных букв в каждом предложении.
     *
     * @param text Исходный текст
     * @return Карта предложений и количества гласных/согласных в них
     */
    public static Map<TextComponent, Map<String, Integer>> countVowelsAndConsonants(TextComponent text) {
        Map<TextComponent, Map<String, Integer>> result = new HashMap<>();

        if (text == null) {
            LOGGER.error("text can't be null");
            return result;
        }

        // Обрабатываем каждый параграф
        for (int i = 0; i < text.getComponentsSize(); i++) {
            CommonText paragraph = text.getComponent(i);
            for (int j = 0; j < paragraph.getComponentsSize(); j++) {
                CommonText sentence = paragraph.getComponent(j);
                int vowels = 0;
                int consonants = 0;

                // Подсчитываем гласные и согласные в каждом слове
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

                // Сохраняем результаты для предложения
                Map<String, Integer> counts = new HashMap<>();
                counts.put("vowels", vowels);
                counts.put("consonants", consonants);
                result.put((TextComponent) sentence, counts);
            }
        }

        LOGGER.info("Counted vowels and consonants in " + result.size() + " sentences");
        return result;
    }

    /**
     * Подсчитывает количество букв в слове.
     *
     * @param word Слово для подсчета букв
     * @return Количество букв в слове
     */
    private static int countLetters(TextComponent word) {
        int count = 0;
        for (int i = 0; i < word.getComponentsSize(); i++) {
            if (word.getComponent(i) instanceof TextLetter) {
                count++;
            }
        }
        return count;
    }

    /**
     * Подсчитывает количество слов в предложении.
     *
     * @param sentence Предложение для подсчета слов
     * @return Количество слов в предложении
     */
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













