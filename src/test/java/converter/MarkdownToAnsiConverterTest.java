
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package converter;

import org.junit.jupiter.api.Test;
import org.merrymike.soft.converter.Converter;
import org.merrymike.soft.converter.MarkdownToAnsiConverter;
import org.merrymike.soft.exception.NestedFormattingException;
import org.merrymike.soft.exception.UnclosedFormattingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkdownToAnsiConverterTest {
    private final Converter ansiConverter = new MarkdownToAnsiConverter();

    @Test
    void boldTextIsConvertedRight() {
        String markdownText = "**bold text**";
        String convertedText = ansiConverter.convert(markdownText);
        assertEquals("\u001B[1mbold text\u001B[22m", convertedText);
    }

    @Test
    void italicTextIsConvertedRight() {
        String markdownText = "_italic text_";
        String convertedText = ansiConverter.convert(markdownText);
        assertEquals("\u001B[3mitalic text\u001B[23m", convertedText);
    }

    @Test
    void monospacedTextIsConvertedRight() {
        String markdownText = "`monospaced text`";
        String convertedText = ansiConverter.convert(markdownText);
        assertEquals("\u001B[7mmonospaced text\u001B[27m", convertedText);
    }

    @Test
    void preformattedTextIsConvertedRight() {
        String markdownText = """
                ```
                preformatted text
                ```""";
        String expectedAnsi = """
                \u001B[7m
                preformatted text
                \u001B[27m""";
        String actualAnsi = ansiConverter.convert(markdownText);
        assertEquals(expectedAnsi, actualAnsi);
    }

    @Test
    void differentFormattingInOneLine() {
        String markdownText = "text **bold text** text _italic text_ text `monospaced text`";
        String expectedAnsi =
                "text \u001B[1mbold text\u001B[22m text \u001B[3mitalic text\u001B[23m text \u001B[7mmonospaced text\u001B[27m";
        String actualAnsi = ansiConverter.convert(markdownText);
        assertEquals(expectedAnsi, actualAnsi);
    }

    @Test
    void unclosedFormattingThrowsException() {
        String unclosedBold = "**unclosed formatting";
        assertThrows(UnclosedFormattingException.class, () -> ansiConverter.convert(unclosedBold));
        String unclosedItalic = "_unclosed formatting";
        assertThrows(UnclosedFormattingException.class, () -> ansiConverter.convert(unclosedItalic));
        String unclosedMonospaced = "`unclosed formatting";
        assertThrows(UnclosedFormattingException.class, () -> ansiConverter.convert(unclosedMonospaced));
    }

    @Test
    void nestedFormattingThrowsException() {
        String nestedFormatting = "**`_nested formatting_`**";
        assertThrows(NestedFormattingException.class, () -> ansiConverter.convert(nestedFormatting));
    }

    @Test
    void snakeCaseIsTreatedRight() {
        String markdownText = "snake_case";
        String convertedText = ansiConverter.convert(markdownText);
        assertEquals("snake_case", convertedText);
    }

    // This test will not pass (for an example)
    @Test
    void standaloneFormatOpeningsAreTreatedRight() {
        String markdownText = "text ** bold text ** text _ italic text _ text ` monospaced text `";
        String expectedAnsi = "text ** bold text ** text _ italic text _ text \u001B[7m monospaced text \u001B[27m";
        String actualAnsi = ansiConverter.convert(markdownText);
        assertEquals(expectedAnsi, actualAnsi);
    }
}
