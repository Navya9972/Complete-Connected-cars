package com.connected.car.user.payload;

import com.connected.car.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse{
    private String accessToken;
    private UserDto loggedInUser;
    private String statusMsg;


}
