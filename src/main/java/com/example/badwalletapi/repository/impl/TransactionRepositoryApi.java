package com.example.badwalletapi.repository.impl;

import com.example.badwalletapi.model.Transaction;
import com.example.badwalletapi.model.Wallet;

import java.util.List;

public interface TransactionRepositoryApi {
    List<Transaction> findByWalletOrderByTimestampDesc(Wallet wallet);
}
