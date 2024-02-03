package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
	Long saveUser(User user);
	User findByUserName(String userName); 

}
