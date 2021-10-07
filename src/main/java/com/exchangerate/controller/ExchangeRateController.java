package com.exchangerate.controller;

import com.exchangerate.model.api.ExchangeRateRequest;
import com.exchangerate.model.api.ExchangeRateResponse;
import com.exchangerate.service.ExchangeRateService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/exchange-rate", produces = "application/json")
@Validated
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @ApiOperation(value = "Permite obtener el monto con la conversion solicitada")
    @GetMapping(value = "/")
    public Single<ResponseEntity<ExchangeRateResponse>> getExchangeRate(
            @RequestParam
            @NotNull(message = "El campo 'amount' no puede ser nulo")
                    Double amount,

            @RequestParam
            @NotNull(message = "El campo 'originCurrency' no puede ser nulo")
            @Pattern(regexp = "^(PEN|USD)+$", message = "El campo 'originCurrency' solo permite los valores 'USD' o 'PEN'")
                    String originCurrency,

            @RequestParam
            @NotNull(message = "El campo destinationCurrency no puede ser nulo")
            @Pattern(regexp = "^(PEN|USD)+$", message = "El campo 'destinationCurrency' solo permite los valores 'USD' o 'PEN'")
                    String destinationCurrency) {

        return exchangeRateService.getExchangeRate(amount, originCurrency, destinationCurrency)
                .map(ResponseEntity::ok);

    }

    @ApiOperation(value = "Permite grabar el tipo de cambio")
    @PostMapping(value = "/")
    public Completable saveExchangeRate(@Valid @RequestBody ExchangeRateRequest exchangeRateRequest) {
        return exchangeRateService.saveExchangeRate(exchangeRateRequest);
    }

    @ApiOperation(value = "Permite actualizar el tipo de cambio")
    @PutMapping(value = "/")
    public Completable updateExchangeRate(@Valid @RequestBody ExchangeRateRequest exchangeRateRequest) {
        return exchangeRateService.updateExchangeRate(exchangeRateRequest);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, NotFoundException.class})
    public Map<String, List<String>> handleValidationExceptions(Object ex) {
        Map<String, List<String>> errorMessages = new HashMap<>();
        List<String> errors = null;

        if (ex instanceof MethodArgumentNotValidException) {
            errors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            
        } else if (ex instanceof ConstraintViolationException) {
            errors = ((ConstraintViolationException) ex).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

        } else if (ex instanceof NotFoundException) {
            errors = Collections.singletonList(((NotFoundException) ex).getMessage());
        }

        errorMessages.put("errors", errors);
        return errorMessages;
    }
}
