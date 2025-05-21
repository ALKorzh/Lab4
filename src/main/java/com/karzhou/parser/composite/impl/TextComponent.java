package com.karzhou.parser.composite.impl;

import com.karzhou.parser.composite.CommonText;
import com.karzhou.parser.composite.ComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextComponent implements CommonText {
    private static final String TO_STRING_MESSAGE = "\n\t";
    private List<CommonText> components = new ArrayList<>();
    private ComponentType type;

    public TextComponent(ComponentType type) {
        this.type = type;
    }

    @Override
    public int countOfOrderedSymbol(String symbol) {
        int counter = 0;
        for(CommonText commonText : components){
            counter += commonText.countOfOrderedSymbol(symbol);
        }

        return counter;
    }
    @Override
    public ComponentType getTypeOfTextComponent() {
        return type;
    }
    @Override
    public CommonText getComponent(int index) {
        return components.get(index);
    }
    @Override
    public int getComponentsSize() {
        return components.size();
    }
    @Override
    public String getTextMessage() {
        StringBuilder stringBuilder = new StringBuilder();

        components.removeIf(Objects::isNull);
        switch (type){
            case TEXT:
                components.forEach(o -> stringBuilder.append("\n\t").append(o.getTextMessage()));
                break;
            case SENTENCE:
                components.forEach(o -> stringBuilder.append(o.getTextMessage()).append(" "));
                break;
            default:
                components.forEach(o -> stringBuilder.append(o.getTextMessage()));
        }

        return stringBuilder.toString();
    }
    @Override
    public boolean addComponent(CommonText commonText) {
        return components.add(commonText);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextComponent)) return false;
        TextComponent that = (TextComponent) o;
        return Objects.equals(components, that.components) &&
                type == that.type;
    }
    @Override
    public int hashCode() {
        int hashCode = type.hashCode();
        for(CommonText commonText : components){
            hashCode += commonText.hashCode() * 31;
        }

        return hashCode;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(CommonText paragraph : components){
            stringBuilder.append(TO_STRING_MESSAGE);
            stringBuilder.append(paragraph.getTextMessage());
        }
        return stringBuilder.toString();
    }

}
