package com.connected.car.user.exceptions.custom;

public class UserAlreadyInactiveException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyInactiveException()
	{
		super("The given user is already in inactivate state");
	}
	
	public UserAlreadyInactiveException(int userId)
	{
		super("User with ID " + userId + " is already inactive.");
	}

}
