package com.exchangerate.service;

import com.exchangerate.model.api.SecurityRequest;
import com.exchangerate.model.api.SecurityResponse;

public interface SecurityService {

	SecurityResponse validateAuthorization(SecurityRequest securityRequest) throws IllegalAccessException;

}
