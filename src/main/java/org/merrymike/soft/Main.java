package org.merrymike.soft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String input = "This_is _italic_ text. _This_ is _more_italic_ text.";
        Pattern pattern = Pattern.compile("\\b_(.*?)_\\b");
        Matcher matcher = pattern.matcher(input);

        StringBuffer replacedString = new StringBuffer();
        while (matcher.find()) {
            String italicText = matcher.group(1);
            matcher.appendReplacement(replacedString, "<i>" + italicText + "</i>");
        }
        matcher.appendTail(replacedString);

        System.out.println(replacedString);
    }
}