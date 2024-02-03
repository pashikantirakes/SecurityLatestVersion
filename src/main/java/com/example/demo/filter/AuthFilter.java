package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.JWTUtil;
@Component
public class AuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil util;
	@Autowired
	private UserDetailsService userService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// The filter begins  by checking if there is an "Authoriation" header in the req..and it contains Bearer token
		//If the Authoriation header found and token==Bearer indicate the presence of an access token,the filter proceeds
		 // to validate and authenticate this token.
		String authHeader=request.getHeader("Authoriation");
		if(authHeader !=null && authHeader.startsWith("Bearer")) {
			String token=authHeader.substring(7);
			//Read username from token
			String userName=util.getUserName(token);
			//If username exist and he did not login
			if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
				//Load current user from db
				UserDetails userDetails=userService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken filter=
						new UsernamePasswordAuthenticationToken(
								userDetails.getUsername(),
								userDetails.getPassword(),
								userDetails.getAuthorities());
				//Link with request
				//filter.setAuthenticationDetailsSource((AuthenticationDetailsSource<HttpServletRequest, ?>) new WebAuthenticationDetailsSource().buildDetails(request));
			    filter.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//link auth to secu.context
				SecurityContextHolder.getContext().setAuthentication((Authentication) filter);
				
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
