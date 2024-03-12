
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

public class MarkdownToAnsiConverter {
    public String convertMarkdownToANSI(String markdownText) {
        unclosedFormattingCheck(markdownText);
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|\\b_(\\S*?)_\\b",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdownText);

        Map<Integer, String> replacementRegexpMap = Map.of(
                1, "\u001B[7m$1\u001B[27m",
                2, "\u001B[7m$1\u001B[27m",
                3, "\u001B[1m$1\u001B[22m",
                4, "\u001B[3m$1\u001B[23m"
        );

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
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|_(\\S*?)_",
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
        Pattern pattern = Pattern.compile("(^|\\W|\\s)_\\S([^_\\W]+)$|(^|\\W|\\s)\\*\\*\\S([^\\*\\*\\W]+)$|(^|\\W|\\s)`\\S([^`\\W]+)$");
        Matcher matcher;
        for (String line : textToCheck.split("\n")) {
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                if (matcher.group() != null) {
                    throw new UnclosedFormattingException(matcher.group());
                }
            }
        }
    }

    private String splitByParagraphs(String text) {
        StringBuilder parsedText = new StringBuilder();
        String[] paragraphs = text.split("\n\n+");

        for (int i = 0; i < paragraphs.length - 1; i++) {
            if (paragraphs[i].startsWith("\u001B[7m") && paragraphs[i].endsWith("\u001B[27m")) {
                if (!parsedText.isEmpty() && parsedText.charAt(parsedText.length() - 1) == '\n') {
                    parsedText.deleteCharAt(parsedText.length() - 1);
                }
                parsedText.append(paragraphs[i]).append("\n");
            } else {
                parsedText.append(paragraphs[i].replace("\n", " ")).append("\n\n");
            }
        }

        if (paragraphs[paragraphs.length - 1].startsWith("\u001B[7m")
                && paragraphs[paragraphs.length - 1].endsWith("\u001B[27m")) {
            parsedText.append(paragraphs[paragraphs.length - 1]);
        } else {
            parsedText.append(paragraphs[paragraphs.length - 1].replace("\n", " "));
        }
        return parsedText.toString();
    }
}
