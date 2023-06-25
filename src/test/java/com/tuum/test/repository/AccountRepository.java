package com.tuum.test.repository;

import com.tuum.test.model.Account;
import com.tuum.test.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
