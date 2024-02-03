package com.example.demo.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Erole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payLoad.request.LoginRequest;
import com.example.demo.payLoad.request.SignUpRequest;
import com.example.demo.payLoad.resp.JwtResponse;
import com.example.demo.payLoad.resp.MessageResponse;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.serviceImpl.UserDetailsImpl;
import com.example.demo.util.JWTUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JWTUtil jwt;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	  PasswordEncoder encoder;
	@Autowired
	private RoleRepo roleRepo;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user){
		Long id=userService.saveUser(user);
		return ResponseEntity.ok("User Saved " + id) ; 
	}
	//SignIn
		@PostMapping("/signin")
		public ResponseEntity<?> signInUser(@RequestBody LoginRequest login) {
			Authentication auth=authManager.authenticate(new UsernamePasswordAuthenticationToken
					(login.getUserName(),
					login.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(auth);
			String jwT=jwt.generateToken(auth);
			UserDetailsImpl userDetails=(UserDetailsImpl) auth.getPrincipal();
			List<String> roles=userDetails.getAuthorities()
					.stream()
					.map(t->t.getAuthority())
					.collect(Collectors.toList());
			
			return ResponseEntity.ok(new JwtResponse(jwT,
					userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
					roles));
		}

	//SignUp
	public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUp){
		if(userRepo.existsByName(signUp.getUserName()))
		return ResponseEntity.badRequest().body(new MessageResponse("User name already exists " ));
		
		if(userRepo.existsByEmail(signUp.getEmail()))
			return ResponseEntity.badRequest().body(new MessageResponse("User email already exists " ));
		
		//Create new User account
		User user=new User(signUp.getUserName(),
				signUp.getEmail(),
				encoder.encode(signUp.getPassword()));
		Set<String> strRoles=signUp.getRoles();
		Set<Role> rol=new HashSet<>();
		if(strRoles==null) {
			Role userRole=roleRepo.findByName(Erole.ROLE_USER).orElseThrow(()->new RuntimeException("Role error"));
			rol.add(userRole);
		}else {
			strRoles.forEach(role->{
				switch(role) {
				case "admin" : 
					Role adminRole=roleRepo.findByName(Erole.ROLE_ADMIN).orElseThrow(()->new RuntimeException("Role error"));
					rol.add(adminRole);
					break;
				case "mod" :
					Role modRole=roleRepo.findByName(Erole.ROLE_MODERATOR).orElseThrow(()->new RuntimeException("Role error"));
					rol.add(modRole);
					break;
				default : 
					Role userRole=roleRepo.findByName(Erole.ROLE_USER).orElseThrow(()->new RuntimeException("Role error"));
					rol.add(userRole);
				}
			});
			
		}
		user.setRoles(rol);
		userRepo.save(user);
		return ResponseEntity.ok(new MessageResponse("User register succesfully")) ;
		
	}
	
	
}
