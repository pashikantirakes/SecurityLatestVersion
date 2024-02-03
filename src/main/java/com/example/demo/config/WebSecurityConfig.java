package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.AuthFilter;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {//extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private AuthFilter securityFilter;
	
	/*
	//This is old version
	@Bean
	public void configure(AuthenticationManagerBuilder authBuild) throws Exception {
		authBuild.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}
	*/
	
	//This is SpringFramework 6.0 update version 
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider dp=new DaoAuthenticationProvider();
		dp.setUserDetailsService(userDetailsService);
		dp.setPasswordEncoder(passwordEncoder());
		return dp;
	}

	@Bean
public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
	return authConfig.getAuthenticationManager();
}
	/*
	 * this ()  specifies various configurations.
	 * It disables csrf and cors token
	 * 
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
		//It indicates the session policy stateless and sessions are not used for tracking.
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeHttpRequests().antMatchers("/api/auth/**").permitAll()
		.antMatchers("/api/test/**").permitAll()
		.anyRequest().authenticated();
		
		//It sets Auth.Provider for authentication,it likely uses the DaoAuthenticationProvider.
		//It provides USer details service and password
		http.authenticationProvider(authProvider());
		http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	 @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	
}
