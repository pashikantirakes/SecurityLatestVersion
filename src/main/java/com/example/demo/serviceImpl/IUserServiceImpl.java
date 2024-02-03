package com.example.demo.serviceImpl;

import java.util.Optional;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.service.UserService;

/*
 * this class implements UserDetailsService,it is responsible for Loading user details and  creating user details obj.for authentcation
 */
@Service
public class IUserServiceImpl implements UserService,UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Override
	//when user attempts to login it provides username,and it is responsible for looking up the user in the user repo. based on the provide username
	//if the user is not found it simply throw the exception.
	//if the user is found the userDetailsImpl obj.is returned.
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	 User user=userRepo.findByName(username).orElseThrow(()->new RuntimeException());
	 return UserDetailsImpl.build(user);
	
	}
		
		
		/*new org.springframework.security.core.userdetails.User(
				username, 
				user.getPassword(),
				user.getRoles()
				.stream()
				.map(e->new SimpleGrantedAuthority(e)
				.collect(Collectors.toSet())
				);*/
			

	@Override
	public Long saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepo.save(user).getId();
	}

	@Override
	public User findByUserName(String userName) {
		Optional<User> opt=userRepo.findByName(userName);
		if(opt.isPresent())
			return opt.get();
		else
		return null;
	}

}
