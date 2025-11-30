package com.url.shortner.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;      // human readable
    private String traceId;      // for correlation (from MDC ideally)
}
