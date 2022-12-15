package com.andela.irrigation;

import com.andela.irrigation.controller.AsynchronousError;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Utility functions for asynchronous programming.
 */
@Slf4j
public class AsyncUtils {
    /**
     * <p>Fetches async result in a safe manner.</p>
     * @param future future object
     * @return Result wrapped by future object
     * @param <T> Generic type
     * @throws AsynchronousError that is caught by controller advisor.
     */

    /**
     * Used to hide public constructor
     */
    private AsyncUtils() {

    }
    public static <T> T fetchAsync(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (ExecutionException exception) {
            log.error("Execution error occurred while fetching Future result", exception);

            Throwable cause = exception.getCause();

            if(cause instanceof ApplicationError) {
                throw (ApplicationError) cause;
            }

            throw new AsynchronousError(cause);
        } catch(InterruptedException exception) {
            Thread.currentThread().interrupt();
            log.error("A problem occurred in thread scheduler while fetching Future result", exception);

            throw new AsynchronousError(exception);
        }
    }
}
