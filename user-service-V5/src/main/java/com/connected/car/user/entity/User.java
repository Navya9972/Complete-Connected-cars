package com.connected.car.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="user")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable=false)
	private String firstName;
	@Column(nullable=false)
	private String lastName;
	private String email;
	private String phoneNumber;
	private Address address;
	private boolean status;
	private String password;
	private String createdBy;
	private String modifiedBY;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	@Enumerated(EnumType.STRING)
	private List<Roles> role;
}
