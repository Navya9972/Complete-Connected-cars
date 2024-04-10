package com.connected.car.user.service.impl;

import com.connected.car.user.dto.UserDto;
import com.connected.car.user.entity.Roles;
import com.connected.car.user.entity.User;
import com.connected.car.user.exceptions.custom.*;
import com.connected.car.user.entity.ApiResponse;
import com.connected.car.user.payload.LoginResponse;
import com.connected.car.user.repository.UserRepository;
import com.connected.car.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.Keycloak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private KeycloakUserService keycloakUserService;

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	RestTemplate restTemplate = new RestTemplate();

//	@Bean
//	public PasswordEncoder getPasswordEncoderBean(){
//		passwordEncoder = restTemplate.getForObject("http://localhost:8088/keycloak/get/passwordencoder/bean",PasswordEncoder.class);
//		return passwordEncoder;
//	}

	private final String userInfoEndpoint="http://localhost:8080/realms/connected-cars/protocol/openid-connect/userinfo";

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Override
	public UserDto createUser(UserDto userDto) {

	    logger.info("Creating user");
	    userDto.setStatus(true);
		userDto.setRole(List.of(Roles.ROLE_user));
	    User user = dtoToEntity(userDto);
//		UserDto loggedInUser = getLoggedInUserDetails();
//		user.setCreatedBy(loggedInUser.getEmail());

	    try {
	    	if (userRepository.existsByEmail(userDto.getEmail())) {
	            throw new DuplicationException("Email already exists");
	        }
	    	if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
	            throw new DuplicationException("Phone number already exists");
	        }

	        User savedUser = userRepository.save(user);
	        UserDto savedUserDto = entityToDto(savedUser);
			System.out.println("CHECKING ROLE "+savedUser.getRole());

			UserDto keycloakUser = new UserDto();
//			keycloakUser.setId(user.getId());
			keycloakUser.setFirstName(savedUserDto.getFirstName());
			keycloakUser.setLastName(savedUserDto.getLastName());
			keycloakUser.setEmail(savedUserDto.getEmail());
			keycloakUser.setPhoneNumber(savedUserDto.getPhoneNumber());
			keycloakUser.setAddress(savedUserDto.getAddress());
			keycloakUser.setStatus(savedUserDto.isStatus());
			keycloakUser.setPassword(userDto.getPassword());
//			keycloakUser.setRole(userDto.getRole());
			boolean status = keycloakUserService.addUserToKeycloakServer(keycloakUser);


	        logger.info("User created successfully "+status);
			if(status==true)
				return savedUserDto;
			else
				throw new UserCreationException("Failed to create user");
	        
	    }catch (DuplicationException e) {
	        logger.error("Exception while creating user");
	        throw new DuplicationException();
	    }
	    
	    catch (Exception e) {
	        logger.error("Exception while creating user");
	        throw new UserCreationException("Failed to create user");
	    }
	}
	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		logger.info("Updating user with ID {}", userId);
		UserDto u = getLoggedInUserDetails();
		String loggedInUser = u.getFirstName();
		try {
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setPhoneNumber(userDto.getPhoneNumber());
			user.setAddress(userDto.getAddress());
			user.setEmail(userDto.getEmail());
//			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setModifiedBY(loggedInUser);
			user.setModifiedDate(LocalDateTime.now());
			user.setRole(userDto.getRole());
			User updatedUser = userRepository.save(user);
			UserDto updatedUserDto = entityToDto(updatedUser);



			logger.info("User updated successfully");
			return updatedUserDto;	
		} catch (UserNotFoundException e) {
			logger.error("User not found for update with ID {}", userId);
			throw new UserNotFoundException(userId);
		} catch (Exception e) {
			logger.error("Exception while updating user with ID {}", userId);
			throw new UserUpdateException(userId);
		}
	}



	@Override
	public UserDto getUserByEmail(String email) {
		logger.info("Fetching user by EMAIL: {}", email);
		try {
			User user = (userRepository.findByEmail(email)).orElseThrow(() -> new UserEmailNotFoundException(email));
			UserDto userDto = entityToDto(user);
			logger.info("User fetched successfully: {}", userDto);
			return userDto;
		} catch (UserNotFoundException e) {
			logger.error("User not found with email {}", email);
			throw e;
			
		} catch (Exception e) {
			logger.error("Exception while fetching user with email {}", email);
			throw new UserFetchException(email);
		}
	}

	
	@Override
	public List<UserDto> getAllUsers() {
		logger.info("Fetching all users");
		try {
			List<User> users = userRepository.findAll();
			List<UserDto> userDtos = users.stream().map(this::entityToDto).toList();
			if (userDtos.isEmpty()) {
				throw new ResourceNotFoundException("No users found");
			}
			logger.info("Fetched {} users successfully", userDtos.size());
			return userDtos;	
		} catch (ResourceNotFoundException e) {
			logger.error("No users found");
			throw e;	
		} catch (Exception e) {
			logger.error("Exception while fetching all users");
			throw new UserFetchException("Failed to fetch all users");
		}
		
	}
	
	@Override
	public List<UserDto> getAllActiveUsers() {
		logger.info("Fetching All Active users");
		try {
			List<User> users = userRepository.findByStatus(true);
			List<UserDto> userDtos = users.stream().map(this::entityToDto).toList();
			if (userDtos.isEmpty()) {
				throw new ResourceNotFoundException("No users found");
			}
			logger.info("Fetched {} Active users successfully", userDtos.size());
			return userDtos;	
		} catch (ResourceNotFoundException e) {
			logger.error("No users found");
			throw e;	
		} catch (Exception e) {
			logger.error("Exception while fetching all Active users");
			throw new UserFetchException("Failed to fetch all Active users");
		}
		
	}

	@Override
	public LoginResponse login(String email, String password) {
		System.out.println("I AM INSIDE LOGIN API!!");
		LoginResponse loginResponse = new LoginResponse();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		HttpEntity<String> request = new HttpEntity<>(headers);
		boolean status = getUserByEmail(email).isStatus();
				System.out.println("STATUS "+status);

		String clientId = "userClient";
		String clientSecret = "bjfVAS9Wgs1kMCqpQ4ETJbu88oRqRfUZ";
		String credentials = clientId + ":" + clientSecret;
		String encodedCredentials = new String(Base64.getEncoder().encode(credentials.getBytes()));
		headers.set("Authorization", "Basic " + encodedCredentials);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("scope", "openid");
		body.add("username", email);
		body.add("password", password);
		body.add("client_id",clientId);
		body.add("client_secret", clientSecret);

		if(status==true){
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(
					"http://localhost:8080/realms/connected-cars/protocol/openid-connect/token",
					requestEntity,
					String.class
			);

			HttpStatusCode statusCode = response.getStatusCode();
			HttpHeaders responseHeaders = response.getHeaders();
			String responseBody = response.getBody();

			if(statusCode == HttpStatus.OK){
				System.out.println("STATUS OK ");
				String accessToken = extractAccessTokenFromResponse(responseBody);

				UserDto loggedInUser = getUserByEmail(email);
				System.out.println("JUST FOR CHECKING I AM PRINTING FIRST NAME "+loggedInUser.getFirstName());
				loginResponse.setLoggedInUser(loggedInUser);
				loginResponse.setAccessToken(accessToken);
				loginResponse.setStatusMsg("Token generated successfully!");
				return loginResponse;
			}
			else{
				loginResponse.setAccessToken(null);
				loginResponse.setLoggedInUser(null);
				loginResponse.setStatusMsg("Token not generated");
				return loginResponse;
			}
		}
		else{
			loginResponse.setStatusMsg("User "+email+" is inactive");
			return loginResponse;
		}


	}




	private String extractAccessTokenFromResponse(String responseBody) {
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			String accessToken = jsonNode.get("access_token").asText();

			return accessToken;


		} catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }


	@Override
	public UserDto getUserById(Integer userId) {
		logger.info("Fetching user by ID: {}", userId);
		try {
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			UserDto userDto = entityToDto(user);
			logger.info("User fetched successfully: {}", userDto);
			return userDto;
		} catch (UserNotFoundException e) {
			logger.error("User not found with ID {}", userId);
			throw e;

		} catch (Exception e) {
			logger.error("Exception while fetching user with ID {}", userId);
			throw new UserFetchException(userId);
		}
	}


	@Override
	public void deleteUser(Integer userId) {

		logger.info("Deleting user with ID: {}", userId);
		try {
			Optional<User> optionalUser = userRepository.findById(userId);

			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				userRepository.delete(user);
				logger.info("User deleted successfully");

			} else {
				throw new UserNotFoundException(userId);
			}
		} catch (UserNotFoundException e) {
			// This exception is thrown when trying to delete a non-existent user.
			logger.error("User not found for deletion with ID {}", userId);
			throw new UserNotFoundException(userId);
		} catch (Exception e) {
			logger.error("Exception while deleting user with ID {}", userId);
			throw new UserDeletionException(userId);
		}
	}

	@Override
	public boolean getStatusById(Integer userId) {
		logger.info("Fetching status for user with ID: {}", userId);
		try {
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			boolean status = user.isStatus();
			logger.info("Status for user with ID {}: {}", userId, status);
			return status;
		} catch (UserNotFoundException e) {
			logger.error("User not found with ID {}", userId);
			throw new UserNotFoundException(userId);
		} catch (Exception e) {
			logger.error("Exception while fetching status for user with ID {}", userId);
			throw new UserFetchException(userId);
		}
	}
	@Override
	public void userDeactivation(Integer userId) {
		UserDto u = getLoggedInUserDetails();
		String loggedInUser = u.getFirstName();
	    logger.info("Deactivating user with ID: {}", userId);
	    try {
	        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
	        if (user.isStatus()) {
	            user.setStatus(false);
				user.setModifiedBY(loggedInUser);
				user.setModifiedDate(LocalDateTime.now());
	            userRepository.save(user);
	            logger.info("User deactivated successfully");
	        } else {
	            throw new UserAlreadyInactiveException(userId);
	        }
	    } catch (UserNotFoundException e) {
	        logger.error("User not found for deactivation with ID {}", userId);
	        throw e;
	        
	    } catch (UserAlreadyInactiveException e) {
	        logger.warn("User with ID {} is already inactive", userId);
	        throw e;
	    } catch (Exception e) {
	        logger.error("Exception while deactivating user with ID {}", userId);
	        throw new UserDeactivationException(userId);
	    }
	}


	@Override
	public void userActivation(Integer userId) {
		UserDto u = getLoggedInUserDetails();
		String loggedInUser = u.getFirstName();
	    logger.info("Activating user with ID: {}", userId);
	    try {
	        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

	        if (!user.isStatus()) {
	            user.setStatus(true);
				user.setModifiedBY(loggedInUser);
				user.setModifiedDate(LocalDateTime.now());
	            userRepository.save(user);
	            logger.info("User activated successfully");
	        } else {
	            throw new UserAlreadyActiveException(userId);
	        }
	    } catch (UserNotFoundException e) {
	        logger.error("User not found for activation with ID {}", userId);
	        throw e;
	    } catch (UserAlreadyActiveException e) {
	        logger.warn("User with ID {} is already active", userId);
	        throw e;
	    } catch (Exception e) {
	        logger.error("Exception while activating user with ID {}", userId);
	        throw new UserActivationException(userId);
	    }
	}


	private User dtoToEntity(UserDto userDto) {
		User user = new User();
		user.setId(userDto.getId());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setAddress(userDto.getAddress());
		user.setStatus(userDto.isStatus());
		user.setRole(userDto.getRole());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return user;
	}

	private UserDto entityToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setPhoneNumber(user.getPhoneNumber());
		userDto.setAddress(user.getAddress());
		userDto.setStatus(user.isStatus());
		userDto.setRole(user.getRole());
		userDto.setPassword(user.getPassword());
		return userDto;
	}

	public UserDto getLoggedInUserDetails(){
		System.out.println("I AM IN \"GET LOGGED IN USER\" METHOD");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("AUTHENTICATION TYPE: " + authentication.getClass().getName());
		System.out.println("CHECKING USER NAME "+authentication.getName());
		System.out.println("CHECKING DETAILS "+authentication.getDetails());
		boolean temp1 = authentication != null;
		System.out.println("AUTHENTICATION OBJECT "+temp1);
		boolean temp2 = authentication instanceof JwtAuthenticationToken;
		System.out.println("AUTHENTICATION PRINCIPAL OBJECT OF AUTHTOKEN " + temp2);




		if(authentication != null && authentication instanceof JwtAuthenticationToken){
			System.out.println("I AM IN IF BLOCK ");
			JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
			Map<String, Object> claims = jwtToken.getTokenAttributes();
			String username = (String) claims.get("name");

				String tokenString = (String) jwtToken.getToken().getTokenValue();
			String email = getUserDetailsFromUserInfoEndpoint(tokenString);
			System.out.println("USER EMAIL "+email);
			UserDto loggedInUser = getUserByEmail(email);

			System.out.println("FISRT NAME FROM FINAL OBJ "+loggedInUser.getFirstName());
			return loggedInUser;
		}
		else{
			System.out.println("Not able fetch the logged in user");
			return null;
		}


	}



	private String getUserDetailsFromUserInfoEndpoint(String accessToken) {
		System.out.println("I AM IN NEW METHOD");
		try {
			String userInfoUrl = "http://localhost:8080/realms/connected-cars/protocol/openid-connect/userinfo";
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + accessToken);
			System.out.println("Request Headers: " + headers);

			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
			HttpStatusCode statusCode = responseEntity.getStatusCode();
			System.out.println("Response Status Code: " + statusCode);

			if (statusCode == HttpStatus.OK) {
				String userInfoResponse = responseEntity.getBody();
				System.out.println("NEW USER " + userInfoResponse);
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode userInfoNode = objectMapper.readTree(userInfoResponse);
				return objectMapper.treeToValue(userInfoNode, User.class).getEmail();


			}
			else{
				return null;
			}
		}

		catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

    }

	@Override
	public void changePassword(String password) {
		UserDto logggedInUser = getLoggedInUserDetails();
		logggedInUser.setPassword(password);
		User u = dtoToEntity(logggedInUser);
		userRepository.save(u);
		keycloakUserService.changePasswordInKeycloak(logggedInUser,password);
	}

//	@Override
//	public void updateRole(String email, List<Roles> role) {
//		UserDto u = getUserByEmail(email);
//		u.setRole(role);
//		User updatedUser = userRepository.save(dtoToEntity(u));
//		keycloakUserService.updateRole(email,role);

//	}


}
