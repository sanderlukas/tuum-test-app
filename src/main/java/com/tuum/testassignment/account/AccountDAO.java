package com.tuum.testassignment.account;

import com.tuum.testassignment.balance.Balance;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountDAO {

    @Select("SELECT * FROM ACCOUNT WHERE ID = #{accountId}")
    @Results(value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "balances", column = "id", javaType = List.class, many = @Many(select = "getBalances", fetchType = FetchType.LAZY)),
    })
    Optional<Account> getAccountById(Long accountId);

    @InsertProvider(type = AccountSQLBuilder.class)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createAccount(Account request);

    @Select("SELECT EXISTS(SELECT 1 FROM ACCOUNT WHERE ID = #{accountId})")
    boolean existsAccountWithAccountId(Long accountId);

    @SelectProvider(type = AccountSQLBuilder.class)
    List<Balance> getBalances(Long accountId);

    @Insert({"""
                <script>
                    INSERT INTO BALANCE (ACCOUNT_ID, AMOUNT, CURRENCY) VALUES
                        <foreach item='balance' collection='balances' open='(' separator='),(' close=')'>
                            #{balance.accountId}, #{balance.amount}, #{balance.currency}
                        </foreach>
                </script>
            """
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createBalances(List<Balance> balances);

    @UpdateProvider(type = AccountSQLBuilder.class)
    void updateBalance(Balance balance);
}
