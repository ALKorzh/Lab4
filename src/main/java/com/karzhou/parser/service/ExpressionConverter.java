package com.karzhou.parser.service;

import com.karzhou.parser.interpreter.InterpreterClient;
import com.karzhou.parser.interpreter.MathExpression;
import com.karzhou.parser.interpreter.PolishNotationInterpreter;
import com.karzhou.parser.polishnotation.PolishNotationConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionConverter{
    // search all number expressions
    private static final String NUMBER_EXPRESSION_REGEX = "\\p{Punct}*\\d[\\p{Punct}\\d]+";
    private static final Logger LOGGER = LogManager.getLogger(ExpressionConverter.class);

    public String replaceExpressionsByNumber(String text){
        List<String> mustReplace = getAllExpressions(text);
        List<String> willReplace = convertToNumbers(mustReplace);

        String result = text;
        for (int i = 0; i < mustReplace.size(); i++) {
            result = result.replace(mustReplace.get(i), willReplace.get(i));
        }

        LOGGER.info("All expressions in the text were replaced by numbers");

        return result;
    }

    private List<String> getAllExpressions(String text){
        List<String> expressions = new ArrayList<>();

        Pattern p = Pattern.compile(NUMBER_EXPRESSION_REGEX);
        Matcher matcher = p.matcher(text);

        while (matcher.find()){
            expressions.add(matcher.group());
        }

        return expressions;
    }
    private List<String> convertToNumbers(List<String> input){
        List<String> result = new ArrayList<>();

        PolishNotationConverter polishNotationParser = new PolishNotationConverter();
        PolishNotationInterpreter interpreter = new PolishNotationInterpreter();

        for(String value : input){
            List<MathExpression> mathExpressions = interpreter.parse(polishNotationParser.calculatePolishNotation(value));
            InterpreterClient interpreterClient = new InterpreterClient();
            int res = interpreterClient.handleExpression(mathExpressions);
            result.add(String.valueOf(res));
        }

        return result;
    }

}

