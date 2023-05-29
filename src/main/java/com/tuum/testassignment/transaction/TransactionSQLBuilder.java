package com.tuum.testassignment.transaction;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

public class TransactionSQLBuilder implements ProviderMethodResolver {

    public static String getTransactionsByAccountId(Long accountId) {
        return new SQL()
                .SELECT("*")
                .FROM("TRANSACTION")
                .WHERE("ACCOUNT_ID = #{accountId}")
                .toString();
    }

    public static String createTransaction(Transaction transaction) {
        return new SQL()
                .INSERT_INTO("TRANSACTION")
                .VALUES("ACCOUNT_ID", "#{accountId}")
                .VALUES("AMOUNT", "#{amount}")
                .VALUES("CURRENCY", "#{currency}")
                .VALUES("DIRECTION", "#{direction}")
                .VALUES("DESCRIPTION", "#{description}")
                .toString();
    }
}
