
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.converter;

import org.merrymike.soft.exception.NestedFormattingException;
import org.merrymike.soft.exception.UnclosedFormattingException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MarkdownConverter implements Converter {

    protected abstract Map<Integer, String> buildReplacementMap();

    @Override
    public String convert(String markdownText) {
        unclosedFormattingCheck(markdownText);
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|(?<=^|\\s)_(.+?)_(?=\\s|$)",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdownText);
        Map<Integer, String> replacementRegexpMap = buildReplacementMap();
        int lastEnd = 0;
        while (matcher.find()) {
            result.append(markdownText, lastEnd, matcher.start());
            for (Map.Entry<Integer, String> entry : replacementRegexpMap.entrySet()) {
                if (matcher.group(entry.getKey()) != null) {
                    if (entry.getKey() > 1) {
                        nestedFormattingCheck(matcher.group(entry.getKey()));
                    }
                    result.append(entry.getValue().replace("$1", matcher.group(entry.getKey())));
                    break;
                }
            }
            lastEnd = matcher.end();
        }
        result.append(markdownText, lastEnd, markdownText.length());
        return splitByParagraphs(result.toString());
    }

    private void nestedFormattingCheck(String group) {
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|_(.+?)_",
                Pattern.MULTILINE);
        if (group != null) {
            Matcher nestedMatcher;
            nestedMatcher = pattern.matcher(group);
            if (nestedMatcher.find()) {
                throw new NestedFormattingException(group);
            }
        }
    }

    private void unclosedFormattingCheck(String textToCheck) {
        Pattern pattern = Pattern.compile("(^|\\W|\\s)(_\\S(?!.*\\S_(\\s|$)).*|\\*\\*\\S(?!.*\\S\\*\\*(\\s|$))|`\\S(?!.*\\S`(\\s|$)))");
        Matcher matcher;
        boolean insidePreformattedBlock = false;
        for (String line : textToCheck.split("\n")) {
            if (line.trim().startsWith("```")) {
                insidePreformattedBlock = !insidePreformattedBlock;
                continue;
            }
            if (insidePreformattedBlock)
                continue;

            matcher = pattern.matcher(line);
            while (matcher.find()) {
                if (matcher.group() != null) {
                    throw new UnclosedFormattingException(matcher.group());
                }
            }
        }
    }

    protected abstract String splitByParagraphs(String text);
}
