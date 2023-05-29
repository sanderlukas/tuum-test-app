package com.tuum.testassignment.rabbitmq;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("rabbitmq")
@Validated
public class RabbitMQProperties {

    @NotNull
    private String accountEventQueue;

    @NotNull
    private String deadAccountEventQueue;

    @NotNull
    private String balanceEventQueue;

    @NotNull
    private String deadBalanceEventQueue;

    @NotNull
    private String transactionEventQueue;

    @NotNull
    private String deadTransactionEventQueue;

    @NotNull
    private String eventExchange;

    @NotNull
    private String deadEventExchange;

    @NotNull
    private String accountKey;

    @NotNull
    private String deadAccountKey;

    @NotNull
    private String balanceKey;

    @NotNull
    private String deadBalanceKey;

    @NotNull
    private String transactionKey;

    @NotNull
    private String deadTransactionKey;

    public String getAccountEventQueue() {
        return accountEventQueue;
    }

    public void setAccountEventQueue(String accountEventQueue) {
        this.accountEventQueue = accountEventQueue;
    }

    public String getBalanceEventQueue() {
        return balanceEventQueue;
    }

    public void setBalanceEventQueue(String balanceEventQueue) {
        this.balanceEventQueue = balanceEventQueue;
    }

    public String getTransactionEventQueue() {
        return transactionEventQueue;
    }

    public void setTransactionEventQueue(String transactionEventQueue) {
        this.transactionEventQueue = transactionEventQueue;
    }

    public String getDeadAccountEventQueue() {
        return deadAccountEventQueue;
    }

    public void setDeadAccountEventQueue(String deadAccountEventQueue) {
        this.deadAccountEventQueue = deadAccountEventQueue;
    }

    public String getDeadBalanceEventQueue() {
        return deadBalanceEventQueue;
    }

    public void setDeadBalanceEventQueue(String deadBalanceEventQueue) {
        this.deadBalanceEventQueue = deadBalanceEventQueue;
    }

    public String getDeadTransactionEventQueue() {
        return deadTransactionEventQueue;
    }

    public void setDeadTransactionEventQueue(String deadTransactionEventQueue) {
        this.deadTransactionEventQueue = deadTransactionEventQueue;
    }

    public String getEventExchange() {
        return eventExchange;
    }

    public void setEventExchange(String eventExchange) {
        this.eventExchange = eventExchange;
    }

    public String getDeadEventExchange() {
        return deadEventExchange;
    }

    public void setDeadEventExchange(String deadEventExchange) {
        this.deadEventExchange = deadEventExchange;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getBalanceKey() {
        return balanceKey;
    }

    public void setBalanceKey(String balanceKey) {
        this.balanceKey = balanceKey;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public String getDeadAccountKey() {
        return deadAccountKey;
    }

    public void setDeadAccountKey(String deadAccountKey) {
        this.deadAccountKey = deadAccountKey;
    }

    public String getDeadBalanceKey() {
        return deadBalanceKey;
    }

    public void setDeadBalanceKey(String deadBalanceKey) {
        this.deadBalanceKey = deadBalanceKey;
    }

    public String getDeadTransactionKey() {
        return deadTransactionKey;
    }

    public void setDeadTransactionKey(String deadTransactionKey) {
        this.deadTransactionKey = deadTransactionKey;
    }
}
