package ru.javacode.concurrentBank.service;

import ru.javacode.concurrentBank.model.BankAccount;

/**
 * В виртуальном банке "ConcurrentBank" решено внедрить многопоточность для обработки операций по счетам клиентов.
 * Система должна поддерживать возможность одновременного пополнения (deposit), снятия (withdraw), а также переводов
 * (transfer) между счетами. Каждый счет имеет свой уникальный номер.
 *
 * Реализуйте класс BankAccount с методами deposit, withdraw и getBalance, поддерживающими многопоточное взаимодействие.
 *
 * Реализуйте класс ConcurrentBank для управления счетами и выполнения переводов между ними. Класс должен предоставлять
 * методы createAccount для создания нового счета и transfer для выполнения переводов между счетами.
 *
 * Переводы между счетами должны быть атомарными, чтобы избежать ситуаций, когда одна часть транзакции выполняется
 * успешно, а другая нет.
 *
 * Реализуйте метод getTotalBalance, который возвращает общий баланс всех счетов в банке.
 */
public interface BankService {
    BankAccount createAccount(long amount);

    void transfer(long senderId, long recipientId, long amount);

    void transfer(BankAccount sender, BankAccount recipient, long amount);

    long getTotalBalance();
}
