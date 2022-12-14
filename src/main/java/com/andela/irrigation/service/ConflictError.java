package com.andela.irrigation.service;

import com.andela.irrigation.ApplicationErrorCode;
import com.andela.irrigation.ApplicationError;
import com.andela.irrigation.model.FieldError;

import java.util.Map;

/**
 * Use this class to flag conflict errors (usually unique constraint violations in natural keys).
 */
public class ConflictError extends ApplicationError {
    /**
     *
     * <p>Use this constructor to report system exceptions.</p>
     * @param errorMap Error map whose keys and values are respectively field names and error codes.
     */
    public ConflictError(Map<String, FieldError> errorMap) {
        super(ApplicationErrorCode.CONFLICTING_INFORMATION, errorMap);
    }
}
