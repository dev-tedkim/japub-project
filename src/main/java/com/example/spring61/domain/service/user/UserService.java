package com.example.spring61.domain.service.user;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.UserDto;

public interface UserService {
	public abstract UserDto findByUserIdAndUserPassword(@Param("userId") String userId,
			@Param("userPassword") String userPassword);

	public abstract UserDto findByUserNum(Long userNum);

	public abstract boolean update(UserDto userDto);
	
	public abstract boolean insert(UserDto userDto);
	
	public abstract UserDto checkPassword(@Param("userNum") Long userNum, @Param("userPassword") String userPassword);
	
	public abstract UserDto findByUserId(String userId);
}
