package com.karzhou.parser.interpreter;

@FunctionalInterface
public interface MathExpression {
    void interpret(InterpreterContext interpreterContext);
}
