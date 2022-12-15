package com.andela.irrigation.dto.job;

public enum JobStatusCode {
    SUCCESS("Successfully finished.", 0),
    SCHEDULED("Scheduled but not started.", -1),
    RUNNING("Running without problem.", -2),
    REACHED_MAX_TRIALS("Max trial threshold reached without a successful outcome.", -4),
    CANCELED("Unexpected interruption; do not trust any results.", -5);

    public final String description;
    public final int code;

    JobStatusCode(String description, int code) {
        this.description = description;
        this.code = code;
    }
}
