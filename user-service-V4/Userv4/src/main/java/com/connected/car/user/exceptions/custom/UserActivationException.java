package com.connected.car.user.exceptions.custom;

public class UserActivationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserActivationException(int userId)
	{
		super(String.format("Failed to put the user into active state with Id: %d" , userId));
	}
	
	public UserActivationException(String message)
	{
		super(message);
	}

}
