package ru.javacode.concurrentBank;

import ru.javacode.concurrentBank.dao.BankRepository;
import ru.javacode.concurrentBank.dao.BankRepositoryImpl;
import ru.javacode.concurrentBank.model.BankAccount;
import ru.javacode.concurrentBank.service.ConcurrentBankService;
import ru.javacode.concurrentBank.service.BankService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) {
        List<BankAccount> accounts = new CopyOnWriteArrayList<>();
        BankRepository repository = new BankRepositoryImpl(accounts);

        BankService bank = new ConcurrentBankService(repository);

        // Создание счетов
        BankAccount account1 = bank.createAccount(1000);
        BankAccount account2 = bank.createAccount(500);

        // Перевод между счетами
        Thread transferThread1 = new Thread(() -> bank.transfer(account1, account2, 200));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2, account1, 100));

        transferThread1.start();
        transferThread2.start();

        try {
            transferThread1.join();
            transferThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вывод общего баланса
        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}
