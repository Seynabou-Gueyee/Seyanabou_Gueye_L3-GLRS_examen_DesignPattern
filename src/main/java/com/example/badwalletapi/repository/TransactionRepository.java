package com.example.badwalletapi.repository;

import com.example.badwalletapi.model.Transaction;
import com.example.badwalletapi.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletOrderByTimestampDesc(Wallet wallet);
}
