package com.connected.car.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.connected.car.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	List<User> findByStatus(boolean b);
	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);

	Optional<User> findByEmail(String email);
}
