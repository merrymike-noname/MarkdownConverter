
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.converter;

import java.util.Map;

public class MarkdownToHtmlConverter extends MarkdownConverter {
    @Override
    protected Map<Integer, String> buildReplacementMap() {
        return Map.of(
                1, "<pre>$1</pre>",
                2, "<tt>$1</tt>",
                3, "<b>$1</b>",
                4, "<i>$1</i>");
    }

    @Override
    protected String splitByParagraphs(String text) {
        StringBuilder parsedText = new StringBuilder();
        for (String paragraph : text.split("\n\n+")) {
            parsedText.append("<p>").append(paragraph).append("</p>\n");
        }
        return parsedText.toString();
    }
}
