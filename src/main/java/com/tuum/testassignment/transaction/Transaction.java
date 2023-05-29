package com.tuum.testassignment.transaction;

public class Transaction {

    private Long id;

    private Long accountId;

    private Long amount;

    private String currency;

    private String direction;

    private String description;

    public Transaction() {
    }

    public Transaction(Long accountId, Long amount, String currency, String direction, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.direction = direction;
        this.description = description;
    }

    public Transaction(Long id, Long accountId, Long amount, String currency, String direction, String description) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.direction = direction;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
