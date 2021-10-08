package com.exchangerate.service;

import com.exchangerate.model.api.ExchangeRateResponse;
import com.exchangerate.model.entity.CurrencyExchangeRate;
import com.exchangerate.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

class ExchangeRateServiceImplTest {

    @Mock
    private ExchangeRateRepository repository;

    private ExchangeRateServiceImpl exchangeRateServiceImpl;

    @Test
    void getExchangeRate() {

        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
        currencyExchangeRate.setCurrency("USD");
        currencyExchangeRate.setExchangeRate(3.5);
        currencyExchangeRate.setId(1L);


        given(repository.findByCurrency(eq("USD")))
                .willReturn(Optional.of(currencyExchangeRate));

        exchangeRateServiceImpl.getExchangeRate(1.0, "PEN", "USD")
                .test()
                .assertValue(value -> value.equals(buildMockExchangeRateResponse()));
    }

    private ExchangeRateResponse buildMockExchangeRateResponse() {
        return ExchangeRateResponse.builder()
                .amount(1.0)
                .exchangeRate(3.5)
                .amountWithExchangeRate(0.29)
                .originCurrency("USD")
                .destinationCurrency("PEN")
                .build();
    }


}