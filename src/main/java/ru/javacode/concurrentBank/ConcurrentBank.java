package ru.javacode.concurrentBank;

import ru.javacode.concurrentBank.exception.InsufficientFundsException;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

public class ConcurrentBank implements Bank {
    private final CopyOnWriteArrayList<BankAccount> accounts;

    private final Semaphore semaphore;

    public ConcurrentBank() {
        this.accounts = new CopyOnWriteArrayList<>();
        semaphore = new Semaphore(1);
    }

    @Override
    public BankAccount createAccount(long amount) {
        BankAccount bankAccount = new BankAccount(amount);
        accounts.add(bankAccount);

        return bankAccount;
    }

    @Override
    public void transfer(BankAccount sender, BankAccount recipient, long amount) {
        try {
            semaphore.acquire();
            synchronized (sender) {
                synchronized (recipient) {
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
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    @Override
    public long getTotalBalance() {
        return accounts.stream()
                .map(BankAccount::getAccount)
                .reduce(0L, Long::sum);
    }
}
