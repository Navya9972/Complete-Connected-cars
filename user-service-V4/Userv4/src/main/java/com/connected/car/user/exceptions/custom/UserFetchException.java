package com.connected.car.user.exceptions.custom;

public class UserFetchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserFetchException(int userId)
	{
		super(String.format("Failed to fetch user details with Id:%d", userId));
	}
	
	public UserFetchException(String message)
	{
		super(message);
	}

}
