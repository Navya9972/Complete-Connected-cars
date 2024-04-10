package com.connected.car.user.exceptions.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(int userId) {
		super(String.format("No user found with id: %d " , userId));
	}
	
}
