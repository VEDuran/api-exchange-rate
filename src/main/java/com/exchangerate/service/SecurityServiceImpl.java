package com.exchangerate.service;

import com.exchangerate.model.api.SecurityRequest;
import com.exchangerate.model.api.SecurityResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Override
	public SecurityResponse validateAuthorization(SecurityRequest securityRequest) throws IllegalAccessException {

		if(securityRequest.getUser().equals("USR") && securityRequest.getPassword().equals("123")) {
			return SecurityResponse.builder()
					.user(securityRequest.getUser())
					.token(getJWTToken(securityRequest.getUser()))
					.build();
		} else {
			throw new IllegalAccessException("El 'user' y/o 'password' son incorrectos");
		}
	}

	private String getJWTToken(String userName) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId("softtekJWT").setSubject(userName)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		return "Bearer " + token;
	}

}
