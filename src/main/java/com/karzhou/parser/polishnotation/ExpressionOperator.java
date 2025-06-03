package com.karzhou.parser.polishnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ExpressionOperator {
    OR_EXPRESSION(5, "|"), XOR_EXPRESSION(6, "^"), AND_EXPRESSION(7, "&"),
    LEFT_SHIFT_EXPRESSION(10, "<<"), RIGHT_SHIFT_EXPRESSION(10, ">>"),
    TILDE_EXPRESSION(13, "~"), OPEN_BRACKET_EXPRESSION(14, "("),
    CLOSE_BRACKET_EXPRESSION(14, ")"),

    PLUS(1, "+"), MINUS(2, "-"), MULTIPLY(3, "*");

    private final int rate;
    private final String value;
    public int getRate() {
        return rate;
    }
    public String getValue() {
        return value;
    }

    ExpressionOperator(int rate, String value) {
        this.rate = rate;
        this.value = value;
    }
    public static ExpressionOperator getExpressionOperator(String value) {
        for (ExpressionOperator operator : ExpressionOperator.values()) {
            if (operator.getValue().equals(value)) {
                return operator;
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + value);
    }

}
