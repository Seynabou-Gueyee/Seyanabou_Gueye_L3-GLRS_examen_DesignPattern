package com.example.badwalletapi.dto;

import java.math.BigDecimal;

public class InvoiceResponse {
    private String factureId;
    private String status;
    private BigDecimal amount;
    private String unite;

    public InvoiceResponse() {
    }

    public InvoiceResponse(String factureId, String status, BigDecimal amount, String unite) {
        this.factureId = factureId;
        this.status = status;
        this.amount = amount;
        this.unite = unite;
    }

    public String getFactureId() {
        return factureId;
    }

    public void setFactureId(String factureId) {
        this.factureId = factureId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }
}
