package com.example.demo.payLoad.resp;

import java.util.List;

import com.example.demo.entity.Role;

import lombok.Data;

@Data
public class JwtResponse {
	private String token;
	private String type="Bearer";
	private Long id;
	private String userName;
	private String email;
	private String passwrod;
	private List<String> roles;
	
	public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
	    this.token = accessToken;
	    this.id = id;
	    this.userName = username;
	    this.email = email;
	    this.roles = roles;
	  }
}
