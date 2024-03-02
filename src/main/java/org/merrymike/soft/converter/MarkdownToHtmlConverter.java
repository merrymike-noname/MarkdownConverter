
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

public class MarkdownToHtmlConverter {
    public String convertMarkdownToHTML(String markdownText) {
        // this is a redundant comment that will be reverted
        unclosedFormattingCheck(markdownText);
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|\\b_(\\S*?)_\\b",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdownText);

        Map<Integer, String> replacementRegexpMap = Map.of(
                1, "<pre>$1</pre>",
                2, "<tt>$1</tt>",
                3, "<b>$1</b>",
                4, "<i>$1</i>"
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
        for (String paragraph : text.split("\n\n")) {
            parsedText.append("<p>").append(paragraph).append("</p>\n");
        }
        return parsedText.toString();
    }
}
