package ru.javacode.concurrentBank.dao;

import ru.javacode.concurrentBank.exception.AccountNotFoundException;
import ru.javacode.concurrentBank.model.BankAccount;

import java.util.List;

public class BankRepositoryImpl implements BankRepository {
    private List<BankAccount> accounts;

    public BankRepositoryImpl(List<BankAccount> accounts) {
        this.accounts = accounts;
    }

    @Override
    public BankAccount get(long id) {
        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account with id = %d is not found".formatted(id)));
    }

    @Override
    public void add(BankAccount account) {
        accounts.add(account);
    }

    @Override
    public List<BankAccount> getAll() {
        return accounts;
    }
}
