package com.connected.car.user.exceptions.custom;
 
public class DuplicationException extends RuntimeException {
 
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
 
	public DuplicationException() {
		super("User already registered");
		
	}
 
 
 
	public DuplicationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
 
	
	
}
 