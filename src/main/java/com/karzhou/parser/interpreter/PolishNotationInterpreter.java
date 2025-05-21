package com.karzhou.parser.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.karzhou.parser.interpreter.MathOperations.*;

public class PolishNotationInterpreter {
    private static final String SPLIT_REGEX = "\\p{Blank}+";

    public List<MathExpression> parse(String polishNotation){
        List<MathExpression> expressions = new ArrayList<>();

        Arrays.asList(polishNotation.split(SPLIT_REGEX)).forEach( token -> {
             switch (token){
                 case  AND_SIGN:
                     expressions.add(res-> res.push(res.pop() & res.pop()));
                     break;
                 case LEFT_SHIFT_SIGN :
                     expressions.add(res -> {
                         int right = res.pop();
                         int left = res.pop();
                         res.push(left << right);
                     });
                     break;
                 case RIGHT_SHIFT_SIGN :
                     expressions.add(res -> {
                         int right = res.pop();
                         int left = res.pop();
                         res.push(left >> right);
                     });
                     break;
                 case TILDE_SIGN :
                     expressions.add(res-> res.push(~res.pop()));
                     break;
                 case OR_SIGN :
                     expressions.add(res-> res.push(res.pop() | res.pop()));
                     break;
                 case XOR_SIGN :
                     expressions.add(res-> res.push(res.pop() ^ res.pop()));
                     break;
                 case PLUS_SIGN :
                     expressions.add(res -> res.push(res.pop() + res.pop()));
                     break;
                 case  MINUS_SIGN :
                     expressions.add(res -> res.push(-res.pop() + res.pop()));
                     break;
                 case MULTIPLY_SIGN :
                     expressions.add(res -> res.push(res.pop() * res.pop()));
                     break;
                 case DIVISION_SIGN :
                     expressions.add(res -> res.push(1 / res.pop() * res.pop()));
                     break;
                 default:
                    expressions.add(res -> res.push(Integer.parseInt(token)));
             }
        });

        return expressions;
    }

}












