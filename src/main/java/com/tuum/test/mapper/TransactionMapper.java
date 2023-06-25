package com.tuum.test.mapper;

import com.tuum.test.mapper.provider.TransactionQueryProvider;
import com.tuum.test.model.Account;
import com.tuum.test.model.Transaction;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TransactionMapper {

    @InsertProvider(type= TransactionQueryProvider.class, method="insertTransaction")
    void insertTransaction(@Param("id") String id, @Param("transactionId") String transactionId, @Param("accountId") Long accountId,
                           @Param("amount") Double amount, @Param("currency") String currency, @Param("direction") String direction, @Param("description") String description);

    @Select("select * from transaction where account_id = #{accountId}")
    List<Transaction> getTransactionsByAccountId(Long accountId);

}
