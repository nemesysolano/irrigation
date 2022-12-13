package com.andela.irrigation.controller;

import com.andela.irrigation.ApplicationError;
import com.andela.irrigation.ApplicationErrorCode;

/**
 * Wrapper for checked exceptions that thrown by controller methods that dep services.
 */

public class AsynchronousError extends ApplicationError {

    /**
     *
     * @param cause Underlying checked exception
     */
    public AsynchronousError(Throwable cause) {
        super(ApplicationErrorCode.INTERRUPTED_THREAD, cause);
    }
}
