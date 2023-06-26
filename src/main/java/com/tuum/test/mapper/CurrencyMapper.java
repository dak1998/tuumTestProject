package com.tuum.test.mapper;

import com.tuum.test.mapper.provider.CurrencyQueryProvider;
import com.tuum.test.model.Currency;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CurrencyMapper {

    @InsertProvider(type= CurrencyQueryProvider.class, method="insertCurrency")
    void insertCurrency(@Param("id") String id, @Param("accountId") Long accountId, @Param("currency") String currency, @Param("amount") Double amount);

    @Select("select * from currency_account_mapping where account_id = #{accountId}")
    List<Currency> getCurrencyListByAccountId(Long accountId);

    @Update("update currency_account_mapping set available_amount=#{updatedAmount} where account_id = #{accountId} and currency = #{currency}")
    void updateAccountBalance(Long accountId, String currency, Double updatedAmount);
}
