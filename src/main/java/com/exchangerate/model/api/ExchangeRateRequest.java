package com.exchangerate.model.api;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ExchangeRateRequest {

    @NotNull(message = "El campo 'currency' no puede ser nulo")
    @Pattern(regexp = "^(PEN|USD)+$", message = "El campo currency solo permite los valores 'USD' o 'PEN'")
    private String currency;

    @NotNull(message = "El campo 'exchangeRate' no puede ser nulo")
    private Double exchangeRate;
}
