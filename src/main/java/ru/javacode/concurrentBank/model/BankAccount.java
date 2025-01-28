package ru.javacode.concurrentBank.model;

import java.util.concurrent.atomic.AtomicLong;

public class BankAccount {
    private final long id;

    private final AtomicLong balance;

    private static long counter = 0;

    public BankAccount(long initValue) {
        id = counter++;
        balance = new AtomicLong(initValue);
    }

    public BankAccount() {
        this(0);
    }

    public long getId() {
        return id;
    }

    public AtomicLong getBalance() {
        return balance;
    }
}
