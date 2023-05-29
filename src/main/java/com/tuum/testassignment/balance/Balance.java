package com.tuum.testassignment.balance;

import java.util.Objects;

public class Balance {

    private Long id;

    private Long accountId;

    private Long amount;

    private String currency;

    public Balance(Long accountId, Long amount, String currency) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
    }

    public Balance(Long id, Long accountId, Long amount, String currency) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance = (Balance) o;
        return Objects.equals(id, balance.id) && Objects.equals(accountId, balance.accountId) && Objects.equals(amount,
                balance.amount) && Objects.equals(currency, balance.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, amount, currency);
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

    public void add(Long amount) {
        this.amount += amount;
    }

    public void deduct(Long amount) {
        this.amount -= amount;
    }

}
