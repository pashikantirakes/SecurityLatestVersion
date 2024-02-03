package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Erole;
import com.example.demo.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByName(Erole name);
	
}
