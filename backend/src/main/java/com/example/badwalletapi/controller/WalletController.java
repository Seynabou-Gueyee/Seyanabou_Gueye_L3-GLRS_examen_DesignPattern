package com.example.badwalletapi.controller;

import com.example.badwalletapi.dto.*;
import com.example.badwalletapi.model.Transaction;
import com.example.badwalletapi.model.Wallet;
import com.example.badwalletapi.service.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WalletController implements com.example.badwalletapi.controller.impl.WalletControllerApi {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallets/seed")
    public ResponseEntity<String> seedWallets(@RequestParam int numWallets, @RequestParam int eventsPerWallet) {
        walletService.seedWallets(numWallets, eventsPerWallet);
        return ResponseEntity.ok("Seed completed");
    }

    @PostMapping("/wallets")
    public ResponseEntity<Wallet> createWallet(@Validated @RequestBody CreateWalletRequest request) {
        return ResponseEntity.ok(walletService.createWallet(request));
    }

    @GetMapping("/wallets")
    public ResponseEntity<Page<Wallet>> listWallets(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(walletService.listWallets(PageRequest.of(page, size)));
    }

    @GetMapping("/wallets/{phoneNumber}")
    public ResponseEntity<Wallet> getWalletByPhone(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(walletService.findByPhoneNumber(phoneNumber));
    }

    @GetMapping("/wallets/{phoneNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(walletService.getBalance(phoneNumber));
    }

    @PostMapping("/wallets/{id}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable Long id,
                                              @Validated @RequestBody DepositRequest request) {
        return ResponseEntity.ok(walletService.deposit(id, request));
    }

    @PostMapping("/wallets/withdraw")
    public ResponseEntity<Transaction> withdraw(@Validated @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(walletService.withdraw(request));
    }

    @PostMapping("/wallets/transfer")
    public ResponseEntity<Transaction> transfer(@Validated @RequestBody TransferRequest request) {
        return ResponseEntity.ok(walletService.transfer(request));
    }

    @PostMapping("/wallets/pay")
    public ResponseEntity<Transaction> payService(@Validated @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(walletService.payInvoice(request));
    }

    @PostMapping("/wallets/pay-factures")
    public ResponseEntity<Transaction> payFactures(@Validated @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(walletService.payInvoice(request));
    }

    @GetMapping("/wallets/{phoneNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(walletService.getTransactions(phoneNumber));
    }

    @GetMapping("/external/factures/{factureId}/current")
    public ResponseEntity<InvoiceResponse> getCurrentFacture(@PathVariable String factureId,
                                                             @RequestParam(required = false) String unite) {
        InvoiceResponse response = new InvoiceResponse(factureId, "UNPAID", BigDecimal.valueOf(5000), unite == null ? "WOYAFAL" : unite);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/external/factures/{factureId}/periode")
    public ResponseEntity<InvoiceResponse> getFactureByPeriode(@PathVariable String factureId,
                                                               @RequestParam String debut,
                                                               @RequestParam String fin) {
        InvoiceResponse response = new InvoiceResponse(factureId, "UNPAID", BigDecimal.valueOf(5000), "WOYAFAL");
        return ResponseEntity.ok(response);
    }
}
