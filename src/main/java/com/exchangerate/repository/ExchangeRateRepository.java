package com.exchangerate.repository;

import com.exchangerate.model.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {

    Optional<CurrencyExchangeRate> findByCurrency(String currency);
}
