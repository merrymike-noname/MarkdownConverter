package org.merrymike.soft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String input = "This is _italic_ text. `This` is **bold** and `_monospaced_` text. _**Test text**_";
        Pattern pattern = Pattern.compile("`(.*?)`|\\*\\*(.*?)\\*\\*|\\b_(.*?)_\\b");
        Matcher matcher = pattern.matcher(input);

        StringBuilder replacedString = new StringBuilder();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                String monospacedText = matcher.group(1);
                matcher.appendReplacement(replacedString, "<tt>" + monospacedText + "</tt>");
            } else if (matcher.group(2) != null) {
                String boldText = matcher.group(2);
                matcher.appendReplacement(replacedString, "<b>" + boldText + "</b>");
            } else if (matcher.group(3) != null) {
                String italicText = matcher.group(3);
                matcher.appendReplacement(replacedString, "<i>" + italicText + "</i>");
            }
        }
        matcher.appendTail(replacedString);

        System.out.println(replacedString);
    }
}