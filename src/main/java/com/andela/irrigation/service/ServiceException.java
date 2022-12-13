package com.andela.irrigation.service;

import com.andela.irrigation.model.FieldError;

import java.util.Collections;
import java.util.Map;

/**
 * <p>Wraps system exceptions thrown from service layer and it's also used
 * to report data integrity violations.</p>
 */
public class ServiceException extends RuntimeException{

    /**
     * <p>Error code which is a compact representation for error messages.</p>
     */
    public final ServiceExceptionCode serviceExceptionCode;

    /**
     * <p>Map containing one entry per field with error.</p>
     */
    public final Map<String,FieldError> errorMap;
    /**
     * <p>General purpose constructor used to report problem which are neither
     * system errors nor data integrity violations.</p>
     * @param serviceExceptionCode Exception code
     */
    public ServiceException(ServiceExceptionCode serviceExceptionCode) {
        super(serviceExceptionCode.message);
        this.serviceExceptionCode = serviceExceptionCode;
        this.errorMap = Map.of();
    }

    /**
     * <p>Use this constructor to report data integrity problems detected in submitted requests.</p>
     * @param serviceExceptionCode Error code which is a compact representation for <code>message</code> string.
     * @param errorMap Error map whose keys and values are respectively field names and error codes.
     */
    public ServiceException(ServiceExceptionCode serviceExceptionCode, Map<String,FieldError> errorMap) {
        super(serviceExceptionCode.message);
        this.serviceExceptionCode = serviceExceptionCode;
        this.errorMap = Collections.unmodifiableMap(errorMap);
    }

    /**
     * <p>Use this constructor to report system exceptions.</p>
     * @param serviceExceptionCode Error code which is a compact representation for <code>message</code> string.
     * @param cause System exception
     */
    public ServiceException(ServiceExceptionCode serviceExceptionCode, Throwable cause) {
        super(serviceExceptionCode.message, cause);
        this.serviceExceptionCode = serviceExceptionCode;
        this.errorMap = Map.of();
    }

    /**
     *
     * @return Structure containing error details.
     */
    public ServiceExceptionCode getServiceExceptionCode() {
        return serviceExceptionCode;
    }
}
