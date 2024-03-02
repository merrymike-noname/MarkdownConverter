
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft;

import org.merrymike.soft.converter.MarkdownToHtmlConverter;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar MarkdownToHtmlConverter.jar <input_file> [--out <output_file>]");
            return;
        }

        String markdownText = readMarkdownText(args[0]);
        MarkdownToHtmlConverter converter = new MarkdownToHtmlConverter();
        String htmlText = converter.convertMarkdownToHTML(markdownText);

        if (args.length >= 3 && args[1].equals("--out")) {
            writeOutput(args[2], htmlText);
        } else {
            System.out.println(htmlText);
        }
    }

    private static void writeOutput(String outputFile, String htmlText) {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(outputFile))){
            bufferedWriter.write(htmlText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Output written successfully to " + outputFile);
    }

    private static String readMarkdownText(String inputFile) {
        StringBuilder markdownText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                markdownText.append(line).append("\n");
            }
            int lastIndex = markdownText.lastIndexOf("\n");
            if (lastIndex != -1) {
                markdownText.delete(lastIndex, lastIndex + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return markdownText.toString();
    }
}