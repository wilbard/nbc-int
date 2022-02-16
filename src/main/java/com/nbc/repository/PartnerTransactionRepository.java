package com.nbc.repository;

import com.nbc.model.PartnerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerTransactionRepository extends JpaRepository<PartnerTransaction, Long> {

    PartnerTransaction findById(long id);
    PartnerTransaction findByTransactionRef(String transactionRef);

    List<PartnerTransaction> findAllByInstitution(String institution);
}
