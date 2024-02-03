package com.example.demo.payLoad.request;

import java.util.Set;

import com.example.demo.entity.Role;

import lombok.Data;

@Data
public class SignUpRequest {

	private String userName;
	private String email;
	private String password;
	private Set<String> roles;
}
