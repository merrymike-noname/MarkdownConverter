
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.exception;

public class NestedFormattingException extends IllegalArgumentException {
    public NestedFormattingException(String nestedFormattingSubstring) {
        super("Nested formatting found in: " + nestedFormattingSubstring);
    }
}
