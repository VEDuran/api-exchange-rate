package com.exchangerate.model.api;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ExchangeRateResponse {

    private Double amount;
    private Double exchangeRate;
    private Double amountWithExchangeRate;
    private String originCurrency;
    private String destinationCurrency;
    private Date creationDateOfExchangeRate;

}
