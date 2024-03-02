
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.exception;

public class UnclosedFormattingException extends IllegalArgumentException {
    public UnclosedFormattingException(String unclosedFormattingSubstring) {
        super("Unclosed formatting found in: " + unclosedFormattingSubstring);
    }
}
