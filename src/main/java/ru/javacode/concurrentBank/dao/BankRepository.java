package ru.javacode.concurrentBank.dao;

import ru.javacode.concurrentBank.model.BankAccount;

import java.util.List;

public interface  BankRepository {
    BankAccount get(long id);

    void add(BankAccount account);

    List<BankAccount> getAll();
}
