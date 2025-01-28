package ru.javacode.concurrentBank.service;

import ru.javacode.concurrentBank.dao.BankRepository;
import ru.javacode.concurrentBank.exception.InsufficientFundsException;
import ru.javacode.concurrentBank.model.BankAccount;

import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentBankService implements BankService {
    private final BankRepository bankRepository;

    public ConcurrentBankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public BankAccount createAccount(long amount) {
        BankAccount bankAccount = new BankAccount(amount);
        bankRepository.add(bankAccount);

        return bankAccount;
    }

    @Override
    public void transfer(long senderId, long recipientId, long amount) {
        if (senderId == recipientId) {
            throw new RuntimeException("id отправителя и получателя не должны быть равны");
        }

        BankAccount sender = bankRepository.get(senderId);
        BankAccount recipient = bankRepository.get(recipientId);

        BankAccount firstLock = sender.getId() < recipient.getId() ? sender : recipient;
        BankAccount secondLock = sender.getId() < recipient.getId() ? recipient : sender;

        synchronized (firstLock) {
            synchronized (secondLock) {
                withdraw(sender, amount);
                deposit(recipient, amount);
            }
        }
    }

    @Override
    public void transfer(BankAccount sender, BankAccount recipient, long amount) {
        transfer(sender.getId(), recipient.getId(), amount);
    }

    private long withdraw(BankAccount account, long amount) {
        if (account.getBalance().get() < amount) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        return account.getBalance().addAndGet(-amount);
    }

    private long deposit(BankAccount account, long amount) {
        return account.getBalance().addAndGet(amount);
    }

    @Override
    public long getTotalBalance() {
        return bankRepository.getAll().stream()
                .map(BankAccount::getBalance)
                .map(AtomicLong::get)
                .reduce(0L, Long::sum);
    }
}
