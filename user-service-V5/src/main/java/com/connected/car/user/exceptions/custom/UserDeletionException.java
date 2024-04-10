package com.connected.car.user.exceptions.custom;

public class UserDeletionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDeletionException(int userId)
	{
		super(String.format("Failed to delete the user with Id: %d", userId));
	}
	
	public UserDeletionException(String message)
	{
		super(message);
	}
}
