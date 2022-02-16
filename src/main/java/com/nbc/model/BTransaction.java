package com.nbc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "b_transactions")
public class BTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "transaction_ref", unique = true, nullable = false)
    private String transactionRef;
    @Column(name = "account", nullable = false)
    private String account;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "transaction_time")
    private Timestamp transactionTime;
}
