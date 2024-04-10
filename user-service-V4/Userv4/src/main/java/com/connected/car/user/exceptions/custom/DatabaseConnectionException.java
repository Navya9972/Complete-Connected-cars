package com.connected.car.user.exceptions.custom;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseConnectionException extends RuntimeException {
	    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
	public DatabaseConnectionException() {
        super("\n\n -- AN ERROR OCCURRED WHILE ESTABLISHING CONNECTION WITH DATABASE PLEASE CHECK YOUR CREDENTIALS--\n");
    }
	public DatabaseConnectionException(String message) {
        super(message);
    }
	
	
	}




