package com.exchangerate.service;

import com.exchangerate.model.api.ExchangeRateRequest;
import com.exchangerate.model.api.ExchangeRateResponse;
import com.exchangerate.model.entity.CurrencyExchangeRate;
import com.exchangerate.processor.ExchangeRateProcessor;
import com.exchangerate.repository.ExchangeRateRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository repository;

    @Override
    public Single<ExchangeRateResponse> getExchangeRate(Double amount, String originCurrency, String destinationCurrency) {
        return findExchangeRateByCurrency(destinationCurrency)
                .map(currencyExchangeRateOpt -> {
                    if (currencyExchangeRateOpt.isPresent()) {
                        return ExchangeRateProcessor.
                                buildExchangeRateResponse(amount, originCurrency, destinationCurrency, currencyExchangeRateOpt.get());
                    }
                    throw new NotFoundException("El tipo de cambio solicitado no se encuentra registrado");
                });
    }

    @Override
    public Completable saveExchangeRate(ExchangeRateRequest exchangeRateRequest) {

        return findExchangeRateByCurrency(exchangeRateRequest.getCurrency())
                .flatMapCompletable(currencyExchangeRateOpt -> {

                    if (!currencyExchangeRateOpt.isPresent()) {
                        return Completable.fromCallable(() -> repository.save(ExchangeRateProcessor.buildCurrencyExchangeRate(exchangeRateRequest)));
                    } else {
                        return Completable.error(new NotFoundException("Ya existe el registro"));
                    }

                });
    }

    @Override
    public Completable updateExchangeRate(ExchangeRateRequest exchangeRateRequest) {
        return findExchangeRateByCurrency(exchangeRateRequest.getCurrency())
                .flatMapCompletable(currencyExchangeRateOpt -> {

                    if (currencyExchangeRateOpt.isPresent()) {
                        CurrencyExchangeRate currencyExchangeRate = currencyExchangeRateOpt.get();
                        currencyExchangeRate.setExchangeRate(exchangeRateRequest.getExchangeRate());

                        return Completable.fromCallable(() -> repository.save(currencyExchangeRate));
                    } else {
                        return Completable.error(new NotFoundException("No existe el tipo de moneda a actualizar"));
                    }

                });
    }

    private Single<Optional<CurrencyExchangeRate>> findExchangeRateByCurrency(String currency) {
        return Single.fromCallable(() -> repository.findByCurrency(currency));
    }

}
