package com.andela.irrigation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * DTO that encapsulates error information sent to the client
 */
@Jacksonized
@Builder(toBuilder=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE, force = true)
@FieldNameConstants
public class ErrorResponse {
    /**
     * Error map whose keys and values are respectively field names and error messages.
     */
    public final Map<String, String> errorMap;

    /**
     * Error code
     */
    public final int code;

    /**
     * Summary message.
     */
    public final String message;
}
