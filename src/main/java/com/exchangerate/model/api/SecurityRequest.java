package com.exchangerate.model.api;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SecurityRequest {

    @NotNull(message = "El campo 'user' no puede ser nulo")
    private String user;

    @NotNull(message = "El campo 'password' no puede ser nulo")
    private String password;
}
