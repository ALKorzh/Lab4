package com.karzhou.parser.composite.impl;

import com.karzhou.parser.composite.CommonText;
import com.karzhou.parser.composite.ComponentType;

import java.util.Objects;

public class TextSign implements CommonText {
    private char sign;
    public TextSign(char sign) {
        this.sign = sign;
    }

    @Override
    public int countOfOrderedSymbol(String symbol) {
        if(String.valueOf(sign).equalsIgnoreCase(symbol)){
            return 1;
        }
        return 0;
    }
    @Override
    public String getTextMessage() {
        return String.valueOf(sign);
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
        if (!(o instanceof TextSign)) return false;
        TextSign textSign = (TextSign) o;
        return Objects.equals(sign, textSign.sign);
    }
    @Override
    public int hashCode() {
        return sign;
    }
    @Override
    public String toString() {
        return String.valueOf(sign);
    }

}
