package org.merrymike.soft;

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

        int lastEnd = 0;
        while (matcher.find()) {
            // Append the text before the match
            result.append(markdownText, lastEnd, matcher.start());

            // Process each match
            if (matcher.group(1) != null) {
                nestedFormattingCheck(pattern, matcher.group(1));
                result.append("<pre>").append(matcher.group(1)).append("</pre>");

            } else if (matcher.group(2) != null) {

                nestedFormattingCheck(pattern, matcher.group(2));
                result.append("<tt>").append(matcher.group(2)).append("</tt>");

            } else if (matcher.group(3) != null) {
                nestedFormattingCheck(pattern, matcher.group(3));
                result.append("<b>").append(matcher.group(3)).append("</b>");
            } else if (matcher.group(4) != null) {

                nestedFormattingCheck(pattern, matcher.group(4));
                result.append("<i>").append(matcher.group(4)).append("</i>");
            }

            lastEnd = matcher.end();
        }

        // Append any remaining text after the last match
        result.append(markdownText.substring(lastEnd));

        return splitByParagraphs(result.toString());
    }

    private static void nestedFormattingCheck(Pattern pattern, String group) {
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