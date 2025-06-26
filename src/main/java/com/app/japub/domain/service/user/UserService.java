package com.app.japub.domain.service.user;

import com.app.japub.domain.dto.UserDto;

public interface UserService {
	public abstract UserDto findByUserId(String userId);

	public abstract UserDto findByUserEmail(String userEmail);

	public abstract UserDto findByUserNum(Long userNum);

	public abstract boolean insert(UserDto userDto);

	public abstract boolean update(UserDto userDto);

	public abstract boolean delete(Long userNum);
	
	public abstract UserDto login(String userId, String userPassword);
	
	public abstract UserDto findByUserNumAndUserPassword(Long userNum, String userPassword);
	
	public abstract void setUserPasswordAndSendMail(UserDto userDto);

}
