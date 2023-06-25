package com.tuum.test.mapper;

import com.tuum.test.mapper.provider.AccountQueryProvider;
import com.tuum.test.model.Account;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AccountMapper {

    @InsertProvider(type= AccountQueryProvider.class, method="insertAccount")
    void insertAccount(@Param("id") String id, @Param("customerId") String customerId, @Param("country") String country);

    @Select("select * from account where id = #{id}")
    Account getAccountById(String id);

    @Select("select * from account where account_id = #{accountId}")
    Account getAccountByAccountId(Long accountId);
}
