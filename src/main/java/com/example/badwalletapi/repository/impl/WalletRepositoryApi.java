package com.example.badwalletapi.repository.impl;

import com.example.badwalletapi.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WalletRepositoryApi {
    Optional<Wallet> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    Page<Wallet> findAll(Pageable pageable);
}
