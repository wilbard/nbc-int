package com.nbc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sacco_static")
public class NbcStatic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "config_name")
    private String configName;
    @Column(name = "string_value")
    private String stringValue;
    @Column(name = "numeric_value")
    private long numericValue;
    @Column(name = "decimal_value")
    private float decimalValue;
    @Column(name = "description")
    private String description;
}
