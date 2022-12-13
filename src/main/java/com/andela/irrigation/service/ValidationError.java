package com.andela.irrigation.service;

import com.andela.irrigation.model.FieldError;

import java.util.Map;

/**
 * Use this class to flag errors founds in entity fields.
 */
public class ValidationError extends ServiceException{

    /**
     *
     * <p>Use this constructor to report system exceptions.</p>
     * @param errorMap Error map whose keys and values are respectively field names and error codes.
     */
    public ValidationError(Map<String, FieldError> errorMap) {
        super(ServiceExceptionCode.DATA_INTEGRITY_ERROR, errorMap);
    }
}
