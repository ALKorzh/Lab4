package com.karzhou.parser.polishnotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PolishNotationConverter {
    private List<ExpressionOperator> expressionOperators;
    private static final Logger LOGGER = LogManager.getLogger(PolishNotationConverter.class);

    public PolishNotationConverter() {
        expressionOperators = new ArrayList<>(Arrays.asList(ExpressionOperator.values()));
    }

    public String calculatePolishNotation(String expression){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) == '<' && expression.charAt(i + 1) == '<'){
                list.add(String.valueOf(expression.charAt(i)) + expression.charAt(i + 1));
                i++;
            }
            else if(expression.charAt(i) == '>' && expression.charAt(i + 1) == '>'){
                list.add(String.valueOf(expression.charAt(i)) + expression.charAt(i + 1));
                i++;
            }

            else if(isNumber(String.valueOf(expression.charAt(i)))){
                StringBuilder number = new StringBuilder();
                int counter = i;
                do {
                    number.append(expression.charAt(counter));
                    counter++;
                    if(counter >= expression.length()){
                        break;
                    }
                }
                while (isNumber(String.valueOf(expression.charAt(counter))));

                i = counter - 1;
                list.add(number.reverse().toString());
            }
            else if(expression.charAt(i) == '-'){
                StringBuilder number = new StringBuilder();
                int counter = i++;
                do {
                    number.append(expression.charAt(counter));
                    counter++;
                    if(counter >= expression.length()){
                        break;
                    }
                }
                while (isNumber(String.valueOf(expression.charAt(counter))));

                i = counter - 1;
                list.add(number.reverse().toString());
            }
            else{
                list.add(String.valueOf(expression.charAt(i)));
            }
        }

        return convert(list);
    }

    private String convert(List<String> expression){
        ArrayDeque<String> result = new ArrayDeque<>();
        ArrayDeque<String> forWait = new ArrayDeque<>();

        for(String character : expression){
             if(!character.equals("")){
                if(isNumber(character)){
                    result.push(character);
                }
                else {
                    if(forWait.isEmpty()){
                        forWait.push(character);
                    }
                    else{
                        String lastWait = forWait.pop();

                        if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(character)).getValue().equals("(")){
                            forWait.push(lastWait);
                            forWait.push(character);
                        }
                        else if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(character)).getValue().equals(")") && !lastWait.equals("(")){
                            result.push(lastWait);

                            String lastPop;
                            while (!forWait.isEmpty()){
                                lastPop = forWait.pop();

                                if(lastPop.equals("(")){
                                    break;
                                }
                                else{
                                    result.push(lastPop);
                                }

                            }

                        }
                        else if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(lastWait)).getValue().equals("(")){
                            forWait.push(lastWait);
                            forWait.push(character);
                        }
                        else if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(lastWait)).getValue().equals(")")){
                            forWait.push(lastWait);
                        }

                        else if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(character)).getRate() > Objects.requireNonNull(ExpressionOperator.getExpressionOperator(lastWait)).getRate()){
                            forWait.push(lastWait);
                            forWait.push(character);
                        }
                        else if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(character)).getRate() <= Objects.requireNonNull(ExpressionOperator.getExpressionOperator(lastWait)).getRate()){
                            result.push(lastWait);

                            String lastPop;

                            if(!forWait.isEmpty()){
                                while(true){
                                    lastPop = forWait.pop();

                                    if(lastPop.equals("(")){
                                        forWait.push(lastPop);
                                        forWait.push(character);
                                        break;
                                    }
                                    if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(lastPop)).getRate() >= Objects.requireNonNull(ExpressionOperator.getExpressionOperator(character)).getRate()){
                                        result.push(lastPop);
                                    }
                                    else if(Objects.requireNonNull(ExpressionOperator.getExpressionOperator(lastPop)).getRate() < Objects.requireNonNull(ExpressionOperator.getExpressionOperator(character)).getRate()){
                                        forWait.push(lastPop);
                                        forWait.push(character);
                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        while (!forWait.isEmpty()){
            String add = forWait.pop();
            if(!add.equals("(") && !add.equals(")")){
                result.push(add);
            }
        }

        return reverseNotation(result);
    }
    private boolean isNumber(String character){
        for (ExpressionOperator expressionOperator : expressionOperators) {
            if (expressionOperator.getValue().equals(character)) {
                return false;
            }
        }
        return !String.valueOf(character).equals(">") && !String.valueOf(character).equals("<");
    }
    private String reverseNotation(ArrayDeque<String> deque){
        StringBuilder stringBuilder = new StringBuilder();

        for(String str : deque){
            stringBuilder.append(str);
            stringBuilder.append(" ");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);

        LOGGER.info("String converted successfully");

        return stringBuilder.reverse().toString();
    }

}
