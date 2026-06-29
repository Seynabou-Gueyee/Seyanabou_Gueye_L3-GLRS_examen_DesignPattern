package com.example.badwalletapi.service;

import com.example.badwalletapi.dto.*;
import com.example.badwalletapi.exception.NotFoundException;
import com.example.badwalletapi.exception.ValidationException;
import com.example.badwalletapi.model.Transaction;
import com.example.badwalletapi.model.TransactionType;
import com.example.badwalletapi.model.Wallet;
import com.example.badwalletapi.repository.TransactionRepository;
import com.example.badwalletapi.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class WalletService implements com.example.badwalletapi.service.impl.WalletServiceApi {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public void seedWallets(int numWallets, int eventsPerWallet) {
        for (int i = 1; i <= numWallets; i++) {
            String phoneNumber = "+2217700000" + String.format("%02d", i);
            if (walletRepository.existsByPhoneNumber(phoneNumber)) {
                continue;
            }
            Wallet wallet = new Wallet(phoneNumber, "testclient" + i + "@gmail.com", BigDecimal.valueOf(25000), "WLT-TEST" + String.format("%03d", i), "XOF");
            walletRepository.save(wallet);
            for (int eventIndex = 1; eventIndex <= eventsPerWallet; eventIndex++) {
                Transaction transaction = new Transaction(wallet, TransactionType.SEEDING, BigDecimal.valueOf(1000), OffsetDateTime.now(), "Seed event " + eventIndex, null);
                wallet.addTransaction(transaction);
            }
            walletRepository.save(wallet);
        }
    }

    public Wallet createWallet(CreateWalletRequest request) {
        if (walletRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ValidationException("Wallet with phone number already exists");
        }
        Wallet wallet = new Wallet(request.getPhoneNumber(), request.getEmail(), request.getInitialBalance(), request.getCode(), request.getCurrency());
        return walletRepository.save(wallet);
    }

    public Page<Wallet> listWallets(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    public Wallet findByPhoneNumber(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("Wallet not found"));
    }

    public BigDecimal getBalance(String phoneNumber) {
        Wallet wallet = findByPhoneNumber(phoneNumber);
        return wallet.getBalance();
    }

    public Transaction deposit(Long walletId, DepositRequest request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        Transaction transaction = new Transaction(wallet, TransactionType.DEPOSIT, request.getAmount(), OffsetDateTime.now(), "Deposit via " + request.getPaymentMethod(), null);
        wallet.addTransaction(transaction);
        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(WithdrawRequest request) {
        Wallet wallet = findByPhoneNumber(request.getPhoneNumber());
        BigDecimal fee = request.getAmount().multiply(BigDecimal.valueOf(0.01));
        BigDecimal total = request.getAmount().add(fee);
        if (wallet.getBalance().compareTo(total) < 0) {
            throw new ValidationException("Insufficient balance for withdrawal and fee");
        }
        wallet.setBalance(wallet.getBalance().subtract(total));
        Transaction transaction = new Transaction(wallet, TransactionType.WITHDRAWAL, request.getAmount(), OffsetDateTime.now(), "Withdrawal fee applied: " + fee, null);
        wallet.addTransaction(transaction);
        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    public Transaction transfer(TransferRequest request) {
        Wallet sender = findByPhoneNumber(request.getSenderPhone());
        Wallet receiver = findByPhoneNumber(request.getReceiverPhone());
        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ValidationException("Insufficient balance to transfer");
        }
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));
        Transaction transactionSender = new Transaction(sender, TransactionType.TRANSFER, request.getAmount().negate(), OffsetDateTime.now(), "Transfer to " + receiver.getPhoneNumber(), null);
        Transaction transactionReceiver = new Transaction(receiver, TransactionType.TRANSFER, request.getAmount(), OffsetDateTime.now(), "Transfer from " + sender.getPhoneNumber(), null);
        sender.addTransaction(transactionSender);
        receiver.addTransaction(transactionReceiver);
        walletRepository.save(sender);
        walletRepository.save(receiver);
        transactionRepository.save(transactionSender);
        return transactionRepository.save(transactionReceiver);
    }

    public Transaction payInvoice(PaymentRequest request) {
        Wallet wallet = findByPhoneNumber(request.getPhoneNumber());
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new ValidationException("Insufficient balance for payment");
        }
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        String description = "Payment to " + request.getServiceName();
        if (request.getFactureReferences() != null && !request.getFactureReferences().isEmpty()) {
            description += " refs=" + String.join(",", request.getFactureReferences());
        }
        Transaction transaction = new Transaction(wallet, TransactionType.PAYMENT, request.getAmount(), OffsetDateTime.now(), description, null);
        wallet.addTransaction(transaction);
        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactions(String phoneNumber) {
        Wallet wallet = findByPhoneNumber(phoneNumber);
        return transactionRepository.findByWalletOrderByTimestampDesc(wallet);
    }
}
