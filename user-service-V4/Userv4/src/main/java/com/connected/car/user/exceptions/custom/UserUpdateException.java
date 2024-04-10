package com.connected.car.user.exceptions.custom;

public class UserUpdateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserUpdateException(int userId)
	{
		super(String.format("Failed to update user with Id: %d" , userId));
	}
	
	public UserUpdateException(String message)
	{
		super(message);
	}
}
