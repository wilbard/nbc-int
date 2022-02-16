package com.nbc.service;

import com.nbc.model.PartnerTransaction;
import com.nbc.repository.PartnerTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartnerTransactionService {

    private PartnerTransactionRepository partnerTransactionRepository;

    public PartnerTransactionService(PartnerTransactionRepository partnerTransactionRepository) {
        this.partnerTransactionRepository = partnerTransactionRepository;
    }

    @Transactional
    public PartnerTransaction newPartnerTransaction(PartnerTransaction partnerTransaction) {
        return this.partnerTransactionRepository.save(partnerTransaction);
    }
    @Transactional
    public void addPartnerTransaction(PartnerTransaction partnerTransaction) {
        this.partnerTransactionRepository.save(partnerTransaction);
    }
    @Transactional
    public PartnerTransaction updatePartnerTransaction(PartnerTransaction partnerTransaction) {
        return this.partnerTransactionRepository.save(partnerTransaction);
    }
    @Transactional
    public void update(PartnerTransaction partnerTransaction) {
        this.partnerTransactionRepository.save(partnerTransaction);
    }
    @Transactional
    public void remove(PartnerTransaction partnerTransaction) {
        this.partnerTransactionRepository.delete(partnerTransaction);
    }

    public PartnerTransaction findById(long id) {
        return this.partnerTransactionRepository.findById(id);
    }

    public PartnerTransaction findByTransactionRef(String transactionRef) {
        return this.partnerTransactionRepository.findByTransactionRef(transactionRef);
    }

    public Page<PartnerTransaction> findAll(int page, int limit, String sort, String order) {
        if (order.equalsIgnoreCase("asc")) {
            return this.partnerTransactionRepository.findAll(PageRequest.of(page, limit, Sort.by(sort).ascending()));
        } else {
            return this.partnerTransactionRepository.findAll(PageRequest.of(page, limit, Sort.by(sort).descending()));
        }
    }

    public List<PartnerTransaction> findAll() {
        return this.partnerTransactionRepository.findAll();
    }

    public List<PartnerTransaction> findAll(String institution) {
        return this.partnerTransactionRepository.findAllByInstitution(institution);
    }
}
