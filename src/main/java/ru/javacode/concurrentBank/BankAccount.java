package ru.javacode.concurrentBank;

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

    public long getBalance() {
        return account.get();
    }
}
