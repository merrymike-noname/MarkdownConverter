
/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package org.merrymike.soft.exception;

public class IncorrectFormatException extends IllegalArgumentException {
    public IncorrectFormatException(String format) {
        super("'--format' argument is invalid (should be either 'html' or 'ansi'). " +
                "You passed: " + format);
    }
}
