package com.example.badwalletapi.exception.impl;

/**
 * Interface pour l'envoi/rapport des exceptions (logs externes, Sentry, etc.).
 */
public interface ExceptionReporter {
    void report(Exception ex);
}
