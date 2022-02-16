package com.nbc.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "partners_transactions")
public class PartnerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "institution")
    private String institution;
    @Column(name = "transaction_ref", unique = true, nullable = false)
    private String transactionRef;
    @Column(name = "account", nullable = false)
    private String account;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @CreationTimestamp
    @Column(name = "transaction_time")
    private Timestamp transactionTime;
}
