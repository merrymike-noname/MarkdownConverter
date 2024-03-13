
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.converter;

import java.util.Map;

public class MarkdownToAnsiConverter extends MarkdownConverter {
    @Override
    protected Map<Integer, String> buildReplacementMap() {
        return Map.of(
                1, "\u001B[7m$1\u001B[27m",
                2, "\u001B[7m$1\u001B[27m",
                3, "\u001B[1m$1\u001B[22m",
                4, "\u001B[3m$1\u001B[23m");
    }

    @Override
    protected String splitByParagraphs(String text) {
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
