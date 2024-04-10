package com.connected.car.user.controller;

import com.connected.car.user.constants.ResponseMessage;
import com.connected.car.user.dto.UserDto;
import com.connected.car.user.entity.ApiResponse;
import com.connected.car.user.payload.LoginResponse;
import com.connected.car.user.service.UserService;
import com.connected.car.user.service.impl.KeycloakUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/users")
@Validated
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;


	private UserService userService;
	private KeycloakUserService keycloakUserService;

	public UserController(UserService userService,KeycloakUserService keycloakUserService) {
		this.userService = userService;
		this.keycloakUserService = keycloakUserService;
	}

	@PostMapping("/login/{username}/{password}")
	public LoginResponse userLogin(@PathVariable("username") String username, @PathVariable("password") String password){
		System.out.println("I AM INSIDE LOGIN CONTROLLER METHOD");
		return userService.login(username, password);
	}


	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto user = userService.createUser(userDto);
		ApiResponse response = new ApiResponse(true, ResponseMessage.CREATE_MESSAGE, user);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserDto userDto,
			@PathVariable("userId") Integer userId) {
		UserDto updatedUser = userService.updateUser(userDto, userId);
		ApiResponse response = new ApiResponse(true, ResponseMessage.UPDATE_MESSAGE, updatedUser);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


//	@PutMapping("/update/{userId}")
//	public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserDto userDto,
//												  @PathVariable("userId") Integer userId) {
//		UserDto updatedUser = userService.updateUser(userDto, userId);
//		ApiResponse response = new ApiResponse(true, ResponseMessage.UPDATE_MESSAGE, updatedUser);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}



	@GetMapping("/getUser/{userId}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable("userId") Integer userId) {
		UserDto user = userService.getUserById(userId);
		ApiResponse response = new ApiResponse(true, ResponseMessage.FETCHING_MESSAGE, user);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userId, HttpServletRequest request) {
		userService.deleteUser(userId);
		ApiResponse response = new ApiResponse(true, ResponseMessage.DELETE_MESSAGE);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<ApiResponse> getAllUsers() {
		List<UserDto> userDetails = userService.getAllUsers();
		ApiResponse response = new ApiResponse(true, ResponseMessage.FETCHING_MESSAGE, userDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getAllActiveUsers")
	public ResponseEntity<ApiResponse> getAllActiveUsers() {
		List<UserDto> userDetails = userService.getAllActiveUsers();
		ApiResponse response = new ApiResponse(true, ResponseMessage.FETCHING_MESSAGE, userDetails);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getUser/byemail/{email}")
	public ResponseEntity<ApiResponse> getUser(@PathVariable("email") String email) {
		UserDto user = userService.getUserByEmail(email);
		ApiResponse response = new ApiResponse(true, ResponseMessage.FETCHING_MESSAGE, user);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/getStatus/{userId}")
	public ResponseEntity<ApiResponse> getStatusById(@PathVariable("userId") Integer userId) {
		boolean status = userService.getStatusById(userId);
		if (status) {
			ApiResponse response = new ApiResponse(true, ResponseMessage.ACTIVE_MESSAGE);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			ApiResponse response = new ApiResponse(true, ResponseMessage.INACTIVE_MESSAGE);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/InactiveStatus/{userId}")
	public ResponseEntity<ApiResponse> updateStatusToInactive(@PathVariable("userId") Integer userId) {
		userService.userDeactivation(userId);
		ApiResponse response = new ApiResponse(true, ResponseMessage.INACTIVATE_MESSAGE);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/activeStatus/{userId}")
	public ResponseEntity<ApiResponse> updateStatusToActive(@PathVariable("userId") Integer userId) {
		userService.userActivation(userId);
		ApiResponse response = new ApiResponse(true, ResponseMessage.ACTIVATE_MESSAGE);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


//	@PutMapping("/update/role")
//	public void updateRole(@RequestBody UserDto userDto){
//		userService.updateRole(userDto.getEmail(),userDto.getRole());
//		return keycloakUserService.updateRole(email);
//	}


	@GetMapping("/get/loggedin/user/details")
	public UserDto getLoggedInUserDetails(){
		return userService.getLoggedInUserDetails();
	}

	@PutMapping("/change/password/{password}")
	public void changePassword(@PathVariable("password") String password){
		userService.changePassword(password);
	}




}