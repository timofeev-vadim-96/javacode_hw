package ru.javacode.concurrentBank;

import ru.javacode.concurrentBank.exception.InsufficientFundsException;

import java.util.concurrent.atomic.AtomicLong;

public class BankAccount {
    private long id;

    private AtomicLong account;

    private static long counter = 0;

    public BankAccount(long initValue) {
        id = counter++;
        account = new AtomicLong(initValue);
    }

    public long withdraw(long amount) {
        if (account.get() < amount) {
            throw new InsufficientFundsException("На счете недостаточно средств");
        }

        return account.addAndGet(-amount);
    }

    public long deposit(long amount) {
        return account.addAndGet(amount);
    }

    public BankAccount() {
        this(0);
    }

    public long getId() {
        return id;
    }

    public long getAccount() {
        return account.get();
    }
}
