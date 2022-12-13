package com.andela.irrigation;

import com.andela.irrigation.model.FieldError;

import java.util.Collections;
import java.util.Map;

/**
 * <p>Wraps system exceptions thrown from service layer and it's also used
 * to report data integrity violations.</p>
 */
public class ApplicationError extends RuntimeException{

    /**
     * <p>Error code which is a compact representation for error messages.</p>
     */
    public final ApplicationErrorCode serviceApplicationErrorCode;

    /**
     * <p>Map containing one entry per field with error.</p>
     */
    private final Map<String,FieldError> errorMap;
    /**
     * <p>General purpose constructor used to report problem which are neither
     * system errors nor data integrity violations.</p>
     * @param serviceApplicationErrorCode Exception code
     */
    public ApplicationError(ApplicationErrorCode serviceApplicationErrorCode) {
        super(serviceApplicationErrorCode.message);
        this.serviceApplicationErrorCode = serviceApplicationErrorCode;
        this.errorMap = Map.of();
    }

    /**
     * <p>Use this constructor to report data integrity problems detected in submitted requests.</p>
     * @param serviceApplicationErrorCode Error code which is a compact representation for <code>message</code> string.
     * @param errorMap Error map whose keys and values are respectively field names and error codes.
     */
    public ApplicationError(ApplicationErrorCode serviceApplicationErrorCode, Map<String,FieldError> errorMap) {
        super(serviceApplicationErrorCode.message);
        this.serviceApplicationErrorCode = serviceApplicationErrorCode;
        this.errorMap = Collections.unmodifiableMap(errorMap);
    }

    /**
     * <p>Use this constructor to report system exceptions.</p>
     * @param serviceApplicationErrorCode Error code which is a compact representation for <code>message</code> string.
     * @param cause System exception
     */
    public ApplicationError(ApplicationErrorCode serviceApplicationErrorCode, Throwable cause) {
        super(serviceApplicationErrorCode.message, cause);
        this.serviceApplicationErrorCode = serviceApplicationErrorCode;
        this.errorMap = Map.of();
    }

    /**
     *
     * @return This enumeration's elements map error codes and messages passed to ServiceException constructor
     */
    public ApplicationErrorCode getServiceApplicationErrorCode() {
        return serviceApplicationErrorCode;
    }

    /**
     *
     * @return Map containing one entry per field with error.
     */
    public Map<String, FieldError> getErrorMap() {
        return errorMap;
    }
}
