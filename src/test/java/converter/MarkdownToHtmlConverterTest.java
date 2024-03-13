
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package converter;

import org.junit.jupiter.api.Test;
import org.merrymike.soft.converter.Converter;
import org.merrymike.soft.converter.MarkdownToHtmlConverter;
import org.merrymike.soft.exception.NestedFormattingException;
import org.merrymike.soft.exception.UnclosedFormattingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MarkdownToHtmlConverterTest {

    private final Converter htmlConverter = new MarkdownToHtmlConverter();

    @Test
    void boldTextIsConvertedRight() {
        String markdownText = "**bold text**";
        String expectedHtml = "<p><b>bold text</b></p>";
        String actualHtml = htmlConverter.convert(markdownText);
        assertEquals(expectedHtml, actualHtml.trim());
    }

    @Test
    void italicTextIsConvertedRight() {
        String markdownText = "_italic text_";
        String expectedHtml = "<p><i>italic text</i></p>";
        String actualHtml = htmlConverter.convert(markdownText);
        assertEquals(expectedHtml, actualHtml.trim());
    }

    @Test
    void monospacedTextIsConvertedRight() {
        String markdownText = "`monospaced text`";
        String expectedHtml = "<p><tt>monospaced text</tt></p>";
        String actualHtml = htmlConverter.convert(markdownText);
        assertEquals(expectedHtml, actualHtml.trim());
    }

    @Test
    void preformattedTextIsConvertedRight() {
        String markdownText = """
                ```
                preformatted text
                ```""";
        String expectedHtml = """
                <p><pre>
                preformatted text
                </pre></p>""";
        String actualHtml = htmlConverter.convert(markdownText);
        assertEquals(expectedHtml, actualHtml.trim());
    }

    @Test
    void differentFormattingInOneLine() {
        String markdownText = "This **is** still _paragraph_ 22 `monospaced` 1.";
        String expectedHtml = "<p>This <b>is</b> still <i>paragraph</i> 22 <tt>monospaced</tt> 1.</p>";
        assertEquals(expectedHtml, htmlConverter.convert(markdownText).trim());
    }

    @Test
    void unclosedFormattingThrowsException() {
        String unclosedBold = "**unclosed formatting";
        assertThrows(UnclosedFormattingException.class, () -> htmlConverter.convert(unclosedBold));
        String unclosedItalic = "_unclosed formatting";
        assertThrows(UnclosedFormattingException.class, () -> htmlConverter.convert(unclosedItalic));
        String unclosedMonospaced = "`unclosed formatting";
        assertThrows(UnclosedFormattingException.class, () -> htmlConverter.convert(unclosedMonospaced));
    }

    @Test
    void nestedFormattingThrowsException() {
        String nestedFormatting = "**`_nested formatting_`**";
        assertThrows(NestedFormattingException.class, () -> htmlConverter.convert(nestedFormatting));
    }

    @Test
    void snakeCaseIsTreatedRight() {
        String markdownText = "snake_case";
        String expectedHtml = "<p>snake_case</p>";
        String actualHtml = htmlConverter.convert(markdownText);
        assertEquals(expectedHtml, actualHtml.trim());
    }

    @Test
    void standaloneFormatOpeningsAreTreatedRight() {
        String standaloneUnderscore = "_";
        String expectedHtmlUnderscore = "<p>_</p>";
        String actualHtmlUnderscore = htmlConverter.convert(standaloneUnderscore);
        assertEquals(expectedHtmlUnderscore, actualHtmlUnderscore.trim());
        String standaloneDoubleStar = "**";
        String expectedHtmlDoubleStar = "<p>**</p>";
        String actualHtmlDoubleStar = htmlConverter.convert(standaloneDoubleStar);
        assertEquals(expectedHtmlDoubleStar, actualHtmlDoubleStar.trim());
        String standaloneBacktick = "`";
        String expectedHtmlBacktick = "<p>`</p>";
        String actualHtmlBacktick = htmlConverter.convert(standaloneBacktick);
        assertEquals(expectedHtmlBacktick, actualHtmlBacktick.trim());
    }
}
