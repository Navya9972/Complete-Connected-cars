package com.connected.car.user.service;

import java.util.List;

import com.connected.car.user.dto.UserDto;
import com.connected.car.user.entity.Roles;
import com.connected.car.user.payload.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

	UserDto createUser(UserDto user);
	UserDto updateUser(UserDto user, Integer userId);
	UserDto getUserByEmail(String email);
	List<UserDto> getAllUsers();
	void deleteUser(Integer userId);
	boolean getStatusById(Integer userId);
	void userDeactivation(Integer userId);
	void userActivation(Integer userId);
	List<UserDto> getAllActiveUsers();

	LoginResponse login(String username, String password);

	 UserDto getLoggedInUserDetails();

	UserDto getUserById(Integer userId);
	void changePassword(String password);

//	void updateRole(String email, List<Roles> role);
}
