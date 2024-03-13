
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.handler;

import org.merrymike.soft.converter.Converter;
import org.merrymike.soft.converter.MarkdownToAnsiConverter;
import org.merrymike.soft.converter.MarkdownToHtmlConverter;
import org.merrymike.soft.exception.IncorrectFormatException;

import java.io.*;

public class AppRunHandler {
    private Converter converter;

    public void runApp(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String inputFile = args[0];
        String format = parseFormat(args);
        converter = createConverterForFormat(format);

        String convertedText = converter.convert(readMarkdownText(inputFile));

        if (isOutputToFile(args)) {
            writeOutputToFile(args[2], convertedText);
        } else {
            System.out.println(convertedText);
        }
    }

    private void printUsage() {
        System.out.println("""
                Usage: java -jar MarkdownToHtmlConverter.jar <input_file> [--out <output_file>] [--format=ansi/html]

                If you want your output to be written into a file, '--format' argument should be passed last.
                """);
    }

    private boolean isOutputToFile(String[] args) {
        return args.length >= 3 && args[1].equalsIgnoreCase("--out");
    }

    private Converter createConverterForFormat(String format) {
        return switch (format.toLowerCase()) {
            case "ansi" -> new MarkdownToAnsiConverter();
            case "html" -> new MarkdownToHtmlConverter();
            default -> throw new IncorrectFormatException(format);
        };
    }

    private String parseFormat(String[] args) {
        String format = "ansi";
        for (String arg : args) {
            if (arg.startsWith("--format")) {
                String[] splitArg = arg.split("=");
                if (splitArg.length > 1) {
                    format = splitArg[1].toLowerCase();
                }
                break;
            }
        }
        return format;
    }

    private void writeOutputToFile(String outputFile, String htmlText) {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(outputFile))){
            bufferedWriter.write(htmlText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Output written successfully to " + outputFile);
    }

    private String readMarkdownText(String inputFile) {
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
