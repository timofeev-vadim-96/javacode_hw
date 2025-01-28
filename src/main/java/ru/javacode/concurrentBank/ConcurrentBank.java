package ru.javacode.concurrentBank;

import ru.javacode.concurrentBank.exception.InsufficientFundsException;

import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentBank implements Bank {
    private final CopyOnWriteArrayList<BankAccount> accounts;

    public ConcurrentBank() {
        this.accounts = new CopyOnWriteArrayList<>();
    }

    @Override
    public BankAccount createAccount(long amount) {
        BankAccount bankAccount = new BankAccount(amount);
        accounts.add(bankAccount);

        return bankAccount;
    }

    @Override
    public void transfer(BankAccount sender, BankAccount recipient, long amount) {
        long senderBalance = sender.getAccount();
        long recipientBalance = recipient.getAccount();
        if (sender.getAccount() >= amount) { //если достаточно средств
            long senderNewBalance = sender.withdraw(amount);
            if (senderNewBalance == senderBalance - amount) { //если деньги списались
                long recipientNewBalance = recipient.deposit(amount);
                if (recipientNewBalance != recipientBalance + amount) { //если деньги не зачислились
                    sender.deposit(amount);
                }
            }
        } else {
            throw new InsufficientFundsException("Недостаточно средств");
        }
    }

    @Override
    public long getTotalBalance() {
        return accounts.stream()
                .map(BankAccount::getAccount)
                .reduce(0L, Long::sum);
    }
}
