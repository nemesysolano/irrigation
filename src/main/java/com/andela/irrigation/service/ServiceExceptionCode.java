package com.andela.irrigation.service;

/**
 * <p>This enumeration's elements map error codes and messages passed to <code>ServiceException</code> constructor.</p>
 */
public enum ServiceExceptionCode {
    DATA_INTEGRITY_ERROR("The posted data did not pass validation process.", -1),
    ENTITY_NOT_FOUND("Entity with the given id doesn't exist.", -2),
    CONFLICTING_INFORMATION("Conflict with the current state of the target resource.", -3);

    public final int code;
    public final String message;

    ServiceExceptionCode(String message, int code) {
        this.code = code;
        this.message = message;
    }
}
