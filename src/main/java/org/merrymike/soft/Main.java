
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft;

import org.merrymike.soft.converter.MarkdownToHtmlConverter;

public class Main {
    public static void main(String[] args) {
        handleArguments(args);

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
        MarkdownToHtmlConverter converter = new MarkdownToHtmlConverter();
        String htmlText = converter.convertMarkdownToHTML(markdownText);
        System.out.println(htmlText);
    }

    private static void handleArguments(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar MarkdownToHtmlConverter.jar <input_file> [--out <output_file>]");
            return;
        }

        String inputFile = args[0];
        if (args.length >= 3 && args[1].equals("--out")) {
            System.out.println("Output File: " + args[2]);
        }
        System.out.println("Input File: " + inputFile);

    }
}