package com.example.badwalletapi.exception.impl;

import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Traduit une exception en payload HTTP standardisé.
 */
public interface ExceptionTranslator {
    ResponseEntity<Map<String, String>> translate(Exception ex);
}
