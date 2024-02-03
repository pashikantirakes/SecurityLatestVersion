package com.example.demo.serviceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

/* 
 * this is a custom class of userDetails,used for representing user details and authorities(roles)
 * for authentication and authorize purpose.
 */
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String userName;
	private String email;
	private String password;
   private Collection<? extends GrantedAuthority> authorities;
   
   public UserDetailsImpl(Long id,String userName,String email,
		   String password,
		   Collection<? extends GrantedAuthority> auth) {
	   this.id=id;
	   this.userName=userName;
	   this.email=email;
	   this.password=password;
	   this.authorities=auth;
   }
   
   public static UserDetailsImpl build(User user) {
	   List<GrantedAuthority> authorities=user.getRoles()
			   .stream()
			   .map(e->new SimpleGrantedAuthority(e.getName().name()))
			   .collect(Collectors.toList());	   
	   return new UserDetailsImpl(
			   user.getId(),
			   user.getName(),
			   user.getEmail(),
			   user.getPassword(),
			   authorities);
   }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}

	public Long getId() {
		return id;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public String getEmail() {
		return email;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	 @Override
	  public boolean equals(Object o) {
	    if (this == o)
	      return true;
	    if (o == null || getClass() != o.getClass())
	      return false;
	    UserDetailsImpl user = (UserDetailsImpl) o;
	    return Objects.equals(id, user.id);
	  }
}
