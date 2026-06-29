package com.example.badwalletapi.service.impl;

import com.example.badwalletapi.dto.*;
import com.example.badwalletapi.model.Transaction;
import com.example.badwalletapi.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface WalletServiceApi {
    void seedWallets(int numWallets, int eventsPerWallet);

    Wallet createWallet(CreateWalletRequest request);

    Page<Wallet> listWallets(Pageable pageable);

    Wallet findByPhoneNumber(String phoneNumber);

    BigDecimal getBalance(String phoneNumber);

    Transaction deposit(Long walletId, DepositRequest request);

    Transaction withdraw(WithdrawRequest request);

    Transaction transfer(TransferRequest request);

    Transaction payInvoice(PaymentRequest request);

    List<Transaction> getTransactions(String phoneNumber);
}
