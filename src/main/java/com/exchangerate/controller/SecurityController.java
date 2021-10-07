package com.exchangerate.controller;

import com.exchangerate.model.api.SecurityRequest;
import com.exchangerate.model.api.SecurityResponse;
import com.exchangerate.service.SecurityService;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/security", produces = "application/json")
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;

	@ApiOperation(value = "Operación que permite generar un token de acceso.")
	@PostMapping(value = "/user")
	public SecurityResponse login(
			@Valid @RequestBody SecurityRequest securityRequest) throws IllegalAccessException{
		return securityService.validateAuthorization(securityRequest);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class, IllegalAccessException.class})
	public Map<String, List<String>> handleValidationExceptions(Object ex) {
		Map<String, List<String>> errorMessages = new HashMap<>();
		List<String> errors = null;

		if (ex instanceof MethodArgumentNotValidException) {
			errors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors()
					.stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());

		} else if (ex instanceof IllegalAccessException) {
			errors = Collections.singletonList(((IllegalAccessException) ex).getMessage());
		}

		errorMessages.put("errors", errors);
		return errorMessages;
	}
}