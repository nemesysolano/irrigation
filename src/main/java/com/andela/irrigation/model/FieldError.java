package com.andela.irrigation.model;

/**
 * This enumeration documents all errors and managed exceptions.
 */
public enum FieldError {
    TEXT_FIELD_EMPTY_OR_TOO_LARGE("Text field is empty or too large.", -1),
    NUMBER_OUT_OF_RANGE("Numeric field is empty or out of range.", -2),
    DUPLICATED_NAME("There exists another entity with the same name.", -3);
    /**
     * Error description.
     */
    public final String message;

    /**
     * Compact representation for <code>message</code>.
     */
    public final int code;

    private FieldError(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
