package com.connected.car.user.exceptions.custom;

public class UserAlreadyActiveException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyActiveException()
	{
		super("The given user is already in active state");
	}
	
	public UserAlreadyActiveException(int userId)
	{
		super("User with ID " + userId + " is already active.");
	}

}
