package com.example.badwalletapi.exception.impl;

import com.example.badwalletapi.exception.NotFoundException;
import com.example.badwalletapi.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultExceptionTranslator implements ExceptionTranslator {

    @Override
    public ResponseEntity<Map<String, String>> translate(Exception ex) {
        Map<String, String> body = new HashMap<>();
        if (ex instanceof NotFoundException) {
            body.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        if (ex instanceof ValidationException) {
            body.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        body.put("error", "Internal server error");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
