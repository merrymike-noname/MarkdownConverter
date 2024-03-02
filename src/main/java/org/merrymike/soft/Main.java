package org.merrymike.soft;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String markdownText = """
               **bold** _ erw
               _italic _
               _italic2_
               `monospaced`
               _txt t_re
               ** wew wew
               
               ```
               Preformatted _text_
               ```

               Paragraph1. Lorem_Ipsum_Dolor Sit Amet.
               This**is**still _paragraph_ 1.

               And after a blank `line` this is paragraph 2.""";
        unclosedFormattingCheck(markdownText);
        String htmlText = convertMarkdownToHTML(markdownText);
        System.out.println(htmlText);
    }

    public static String convertMarkdownToHTML(String markdownText) {
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

    private static void nestedFormattingCheck(String group) {
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|_(\\S*?)_",
                Pattern.MULTILINE);
        if (group != null) {
            Matcher nestedMatcher;
            nestedMatcher = pattern.matcher(group);
            if (nestedMatcher.find()) {
                throw new IllegalArgumentException("File is incorrect. Nested formatting found in: " + group);
            }
        }
    }

    private static void unclosedFormattingCheck(String textToCheck) {
        Pattern pattern = Pattern.compile("(^|\\W|\\s)_\\S([^_\b]+)$|(^|\\W|\\s)\\*\\*\\S([^\\*\\*]+)$|(^|\\W|\\s)`\\S([^`]+)$");
        Matcher matcher;
        for (String line : textToCheck.split("\n")) {
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                if (matcher.group() != null) {
                    throw new IllegalArgumentException("Unclosed formatting found in: " + matcher.group());
                }
            }
        }
    }

    private static String splitByParagraphs(String text) {
        StringBuilder parsedText = new StringBuilder();
        for (String paragraph : text.split("\n\n")) {
            parsedText.append("<p>").append(paragraph).append("</p>\n");
        }
        return parsedText.toString();
    }
}