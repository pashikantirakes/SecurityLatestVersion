package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	 @Id
	 /*
	  * It signifies that the values of Pk fields in JPA entity classes will be generated automatically.
	  * This is typically achieved using an automatically incremented seq.number provided by the db.
	  * The annotation prevents the manual assignment of pk values and commonly utilizes a mechanism controlled by the db for value generation.
	  * GenerationType is enum used to specify the automatic key generation strategies offered by JPA.These strategies how you generate pk values for an entity.
	  * GT.Auto - The JPA implementation chooses a db-specific key generation strategy,Typically it will 1st try Seq.or Identity .
	  * GT.Identity - The pk value is automatically generated by the db,this is achieved by using the db auto increment columns.
	  * GT.Seq - It generate pk value using a db seq.This strategy creates a db obj and then generates pk using the next value of that obj. 
	  */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	
	/*
	 * fetch attribute to control loading behavior (Lazy,Eager) 
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_roles",
	joinColumns = @JoinColumn(name="user_id"),
	inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<Role> roles=new HashSet<>();

	public User(String userName, String email2, String encode) {
		this.name=userName;
		this.email=email2;
		this.password=encode;
	}
}
