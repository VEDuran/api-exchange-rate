package com.exchangerate.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="CurrencyExchangeRate")
public class CurrencyExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchangeRate")
    private Double exchangeRate;
}
