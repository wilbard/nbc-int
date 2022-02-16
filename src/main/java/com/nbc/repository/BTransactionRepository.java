package com.nbc.repository;

import com.nbc.model.BTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BTransactionRepository extends JpaRepository<BTransaction, Long> {

    BTransaction findById(long id);
    BTransaction findByTransactionRef(String transactionRef);
}
