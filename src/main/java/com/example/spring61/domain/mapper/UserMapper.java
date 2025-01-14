package com.example.spring61.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.UserDto;

@Mapper
public interface UserMapper {
	public abstract UserDto findByUserIdAndUserPassword(@Param("userId") String userId, @Param("userPassword") String userPassword) throws Exception;
	public abstract UserDto findByUserNum(Long userNum) throws Exception;
	public abstract int update(UserDto userDto) throws Exception;
	public abstract int insert(UserDto userDto) throws Exception;
	public abstract UserDto findByUserNumAndUserPassword(@Param("userNum") Long userNum, @Param("userPassword") String userPassword) throws Exception;
	public abstract UserDto findByUserId(String userId) throws Exception;
}
