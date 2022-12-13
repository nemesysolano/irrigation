package com.andela.irrigation.service;

import lombok.extern.slf4j.Slf4j;

/**
 * Triggered when the expected entity doesn't exist.
 */
@Slf4j
public class NonExistingEntityError extends ServiceException{
    /**
     *
     * <p>Use this constructor to report system exceptions.</p>
     * @param entityName Name of the non-existing entity.
     */
    public NonExistingEntityError(String entityName) {
        super(ServiceExceptionCode.ENTITY_NOT_FOUND);
        log.error(
                String.format(
                        "%s (%s)",
                        ServiceExceptionCode.ENTITY_NOT_FOUND.message,
                        entityName
                )
        );
    }
}

