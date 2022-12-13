package com.andela.irrigation.service;

import com.andela.irrigation.ApplicationErrorCode;
import com.andela.irrigation.ApplicationError;
import lombok.extern.slf4j.Slf4j;

/**
 * Triggered when the expected entity doesn't exist.
 */
@Slf4j
public class NonExistingEntityError extends ApplicationError {
    /**
     *
     * <p>Use this constructor to report system exceptions.</p>
     * @param entityName Name of the non-existing entity.
     */
    public NonExistingEntityError(String entityName) {
        super(ApplicationErrorCode.ENTITY_NOT_FOUND);
        log.error(
                String.format(
                        "%s (%s)",
                        ApplicationErrorCode.ENTITY_NOT_FOUND.message,
                        entityName
                )
        );
    }
}

