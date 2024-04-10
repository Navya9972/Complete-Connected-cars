package com.connected.car.user.exceptions.custom;

public class UserEmailNotFoundException extends RuntimeException{

    public UserEmailNotFoundException(String email) {
        super(String.format("No user found with email: %s " , email));
    }

}
