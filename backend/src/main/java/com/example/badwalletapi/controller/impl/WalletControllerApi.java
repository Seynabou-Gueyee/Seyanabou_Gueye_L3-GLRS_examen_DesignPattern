package com.example.badwalletapi.controller.impl;

import com.example.badwalletapi.dto.*;
import com.example.badwalletapi.model.Transaction;
import com.example.badwalletapi.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface WalletControllerApi {

    ResponseEntity<String> seedWallets(int numWallets, int eventsPerWallet);

    ResponseEntity<Wallet> createWallet(CreateWalletRequest request);

    ResponseEntity<Page<Wallet>> listWallets(int page, int size);

    ResponseEntity<Wallet> getWalletByPhone(String phoneNumber);

    ResponseEntity<BigDecimal> getBalance(String phoneNumber);

    ResponseEntity<Transaction> deposit(Long id, DepositRequest request);

    ResponseEntity<Transaction> withdraw(WithdrawRequest request);

    ResponseEntity<Transaction> transfer(TransferRequest request);

    ResponseEntity<Transaction> payService(PaymentRequest request);

    ResponseEntity<Transaction> payFactures(PaymentRequest request);

    ResponseEntity<List<Transaction>> getTransactions(String phoneNumber);

}
