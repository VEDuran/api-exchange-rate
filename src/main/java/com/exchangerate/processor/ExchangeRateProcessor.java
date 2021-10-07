package com.exchangerate.processor;

import com.exchangerate.model.api.ExchangeRateRequest;
import com.exchangerate.model.api.ExchangeRateResponse;
import com.exchangerate.model.entity.CurrencyExchangeRate;
import org.springframework.beans.BeanUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

public class ExchangeRateProcessor {

    private ExchangeRateProcessor() {
        throw new IllegalStateException("Utility class");
    }

    public static ExchangeRateResponse buildExchangeRateResponse(Double amount, String originCurrency, String destinationCurrency,
                                                                 CurrencyExchangeRate currencyExchangeRate) {

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');

        return ExchangeRateResponse
                .builder()
                .amount(amount)
                .amountWithExchangeRate(
                        Double.parseDouble(new DecimalFormat("#0.00", simbolos).format(destinationCurrency.equals("USD")
                                ? amount * currencyExchangeRate.getExchangeRate()
                                : amount / currencyExchangeRate.getExchangeRate())))
                .originCurrency(originCurrency)
                .destinationCurrency(destinationCurrency)
                .exchangeRate(currencyExchangeRate.getExchangeRate())
                .creationDateOfExchangeRate(currencyExchangeRate.getCreationDate())
                .build();
    }

    public static CurrencyExchangeRate buildCurrencyExchangeRate(ExchangeRateRequest exchangeRateRequest) {
        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();

        BeanUtils.copyProperties(exchangeRateRequest, currencyExchangeRate);
        currencyExchangeRate.setCreationDate(new Date());

        return currencyExchangeRate;
    }
}
