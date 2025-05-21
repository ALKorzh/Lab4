package com.karzhou.parser.interpreter;

import java.util.ArrayDeque;

class InterpreterContext {
    private ArrayDeque<Integer> contextValue = new ArrayDeque<>();

    Integer pop() {
        return contextValue.pop();
    }
    void push(Integer integer) {
        contextValue.push(integer);
    }

}
