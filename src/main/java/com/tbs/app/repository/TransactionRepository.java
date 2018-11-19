package com.tbs.app.repository;

import com.tbs.app.model.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public List<Transaction> findAllByAccountId(Long id);
}
