package com.url.shortner.exception;

import com.google.gson.Gson;
import com.url.shortner.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private Gson gson;

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> unhandledException(Exception ex) {
        logger.error("Unhandled exception caught in GlobalExceptionHandler", ex);

        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        response.setTraceId(MDC.get("uuId"));

        String json = gson.toJson(response);
        logger.info("Final ErrorResponse: {}", json);

        return finalResponseDueToException(json);
    }

    private ResponseEntity<String> finalResponseDueToException(String res) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(res);
    }

}