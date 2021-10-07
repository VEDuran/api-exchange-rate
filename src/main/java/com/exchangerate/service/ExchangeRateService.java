package com.exchangerate.service;

import com.exchangerate.model.api.ExchangeRateRequest;
import com.exchangerate.model.api.ExchangeRateResponse;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface ExchangeRateService {

    Single<ExchangeRateResponse> getExchangeRate(Double amount,String originCurrency, String destinationCurrency);

    Completable saveExchangeRate(ExchangeRateRequest exchangeRateRequest);

    Completable updateExchangeRate(ExchangeRateRequest exchangeRateRequest);
}
