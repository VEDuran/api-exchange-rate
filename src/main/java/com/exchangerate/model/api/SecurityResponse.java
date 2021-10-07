package com.exchangerate.model.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityResponse {

    private String user;
    private String token;
}
