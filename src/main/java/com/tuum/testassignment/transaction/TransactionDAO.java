package com.tuum.testassignment.transaction;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransactionDAO {

    @InsertProvider(type = TransactionSQLBuilder.class)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createTransaction(Transaction transaction);

    @SelectProvider(type = TransactionSQLBuilder.class)
    @Results(value = {
            @Result(column = "account_id", property = "accountId")
    })
    List<Transaction> getTransactionsByAccountId(Long accountId);
}
