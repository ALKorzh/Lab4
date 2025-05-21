package com.karzhou.parser.composite.impl;

import com.karzhou.parser.composite.CommonText;
import com.karzhou.parser.composite.ComponentType;

import java.util.Objects;

public class TextLetter implements CommonText {
    private char letter;
    public TextLetter(char letter) {
        this.letter = letter;
    }

    @Override
    public int countOfOrderedSymbol(String symbol) {
        if(String.valueOf(letter).equalsIgnoreCase(symbol)){
            return 1;
        }
        return 0;
    }
    @Override
    public String getTextMessage() {
        return String.valueOf(letter);
    }
    @Override
    public boolean addComponent(CommonText commonText) {
        return false;
    }
    @Override
    public CommonText getComponent(int index) {
        throw new UnsupportedOperationException("That method is not for leaf object");
    }
    @Override
    public int getComponentsSize() {
        throw new UnsupportedOperationException("That method is not for leaf object");
    }
    @Override
    public ComponentType getTypeOfTextComponent() {
        throw new UnsupportedOperationException("That method is not for leaf object");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextLetter)) return false;
        TextLetter that = (TextLetter) o;
        return Objects.equals(letter, that.letter);
    }
    @Override
    public int hashCode() {
        return letter;
    }
    @Override
    public String toString() {
        return String.valueOf(letter);
    }

}
