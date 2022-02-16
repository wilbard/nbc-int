package com.nbc.service;

import com.nbc.model.BTransaction;
import com.nbc.model.BTransaction;
import com.nbc.repository.BTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BTransactionService {
    
    private BTransactionRepository bTransactionRepository;
    
    public BTransactionService(BTransactionRepository bTransactionRepository) {
        this.bTransactionRepository = bTransactionRepository;
    }

    @Transactional
    public BTransaction newBTransaction(BTransaction bTransaction) {
        return this.bTransactionRepository.save(bTransaction);
    }
    @Transactional
    public void addBTransaction(BTransaction bTransaction) {
        this.bTransactionRepository.save(bTransaction);
    }
    @Transactional
    public BTransaction updateBTransaction(BTransaction bTransaction) {
        return this.bTransactionRepository.save(bTransaction);
    }
    @Transactional
    public void update(BTransaction bTransaction) {
        this.bTransactionRepository.save(bTransaction);
    }
    @Transactional
    public void remove(BTransaction bTransaction) {
        this.bTransactionRepository.delete(bTransaction);
    }

    public BTransaction findById(long id) {
        return this.bTransactionRepository.findById(id);
    }

    public BTransaction findByTransactionRef(String transactionRef) {
        return this.bTransactionRepository.findByTransactionRef(transactionRef);
    }

    public Page<BTransaction> findAll(int page, int limit, String sort, String order) {
        if (order.equalsIgnoreCase("asc")) {
            return this.bTransactionRepository.findAll(PageRequest.of(page, limit, Sort.by(sort).ascending()));
        } else {
            return this.bTransactionRepository.findAll(PageRequest.of(page, limit, Sort.by(sort).descending()));
        }
    }

    public List<BTransaction> findAll() {
        return this.bTransactionRepository.findAll();
    }
}
