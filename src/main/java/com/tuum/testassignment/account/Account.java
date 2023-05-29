package com.tuum.testassignment.account;

import com.tuum.testassignment.balance.Balance;

import java.util.List;
import java.util.Objects;

public class Account {

    private Long id;

    private Long customerId;

    private String country;

    private List<Balance> balances;

    public Account() {
    }

    public Account(Long customerId, String country) {
        this.customerId = customerId;
        this.country = country;
    }

    public Account(Long customerId, String country, List<Balance> balances) {
        this.customerId = customerId;
        this.country = country;
        this.balances = balances;
    }

    public Account(Long id, Long customerId, String country, List<Balance> balances) {
        this.id = id;
        this.customerId = customerId;
        this.country = country;
        this.balances = balances;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long accountId) {
        this.id = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(customerId, account.customerId) && Objects.equals(country,
                account.country) && Objects.equals(balances, account.balances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, country, balances);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", country='" + country + '\'' +
                ", balances=" + balances +
                '}';
    }
}
