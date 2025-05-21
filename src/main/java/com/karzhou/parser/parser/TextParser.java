package com.karzhou.parser.parser;

import com.karzhou.parser.composite.CommonText;

@FunctionalInterface
public interface TextParser {
    CommonText parse(String text);
}
