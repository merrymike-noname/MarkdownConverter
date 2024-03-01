package org.merrymike.soft;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String markdownText = """
               **bold**
               _italic_
               _italic2_
               `monospaced`
               _txt
               ```
               Preformatted text
               ```

               Paragraph1. Lorem Ipsum Dolor Sit Amet.
               This is still paragraph 1.

               And after a blank line this is paragraph 2.""";
        String htmlText = convertMarkdownToHTML(markdownText);
        System.out.println(htmlText);
    }

    public static String convertMarkdownToHTML(String markdownText) {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|_(.*?)_",
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
                    nestedFormattingCheck(matcher.group(entry.getKey()));
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
        Pattern pattern = Pattern.compile("```([\\s\\S]*?)```|`([^`]+)`|\\*\\*(.*?)\\*\\*|_(.*?)_",
                Pattern.MULTILINE);
        if (group != null) {
            Matcher nestedMatcher;
            nestedMatcher = pattern.matcher(group);
            if (nestedMatcher.find()) {
                throw new IllegalArgumentException("File is incorrect. Nested formatting found in: " + group);
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