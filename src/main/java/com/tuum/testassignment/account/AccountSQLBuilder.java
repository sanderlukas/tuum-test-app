package com.tuum.testassignment.account;

import com.tuum.testassignment.balance.Balance;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

public class AccountSQLBuilder implements ProviderMethodResolver {

    public static String createAccount(Account account) {
        return new SQL()
                .INSERT_INTO("ACCOUNT")
                .VALUES("CUSTOMER_ID", "#{customerId}")
                .VALUES("COUNTRY", "#{country}")
                .toString();
    }

    public static String getBalances(Long accountId) {
        return new SQL()
                .SELECT("*")
                .FROM("BALANCE")
                .WHERE("ACCOUNT_ID = #{accountId}")
                .toString();
    }

    public static String updateBalance(Balance balance) {
        return new SQL()
                .UPDATE("BALANCE")
                .SET("AMOUNT = #{amount}")
                .WHERE("ID = #{id}")
                .toString();
    }

}
