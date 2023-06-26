package com.tuum.test.repository;

import com.tuum.test.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountCurrencyBalanceRepository extends JpaRepository<Currency, String> {
}
