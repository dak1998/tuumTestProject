package com.tuum.test.mapper.provider;

import org.apache.ibatis.jdbc.SQL;


public class AccountQueryProvider {

    public static String insertAccount(String id, String customerId, String country) {
        return new SQL(){{
            INSERT_INTO("account");
            VALUES("id, customer_id, country", "#{id},#{customerId},#{country}");
        }}.toString();
    }
}
