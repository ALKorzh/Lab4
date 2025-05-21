package com.karzhou.parser.interpreter;

import java.util.List;

public class InterpreterClient {
    private InterpreterContext interpreterContext = new InterpreterContext();

    public int handleExpression(List<MathExpression> expressions){
            for(MathExpression terminal : expressions){
                terminal.interpret(interpreterContext);
            }
            return interpreterContext.pop();
    }

}
