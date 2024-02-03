package com.example.demo.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class JWTUtil {
     @Value("${app.secret}")
	private String secret;
public String generateToken(Authentication auth){//(String subject) {
	UserDetails userDetails=(UserDetails) auth.getPrincipal();
	return Jwts.builder()
			.setSubject(userDetails.getUsername())
			.setIssuer("Rakesh")
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis()+TimeUnit.MINUTES.toMillis(15)))
			.signWith(SignatureAlgorithm.HS512, secret.getBytes())
			.compact()
			;
	
}
public Claims getClaims(String token) {
	return Jwts.parser()
			.setSigningKey(secret.getBytes())
			.parseClaimsJws(token).getBody();
	
}
public String getUserName(String token) {
	return getClaims(token).getSubject();
}
/*
public boolean validateJwtToken(String authToken) {
	try {
		Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(authToken);
		return true;
	}catch(SignatureException e) {
		logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
	}
	*/
//}
}
