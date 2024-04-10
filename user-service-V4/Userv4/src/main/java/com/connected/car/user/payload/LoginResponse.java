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
    public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public UserDto getLoggedInUser() {
		return loggedInUser;
	}
	public void setLoggedInUser(UserDto loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	private String statusMsg;


}
