package com.connected.car.user.exceptions.custom;

public class UserDeactivationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDeactivationException(int userID)
	{
		super(String.format("Failed to deactivate the user with Id: %d" , userID));
	}
	
	public UserDeactivationException(String message)
	{
		super(message);
	}
}
