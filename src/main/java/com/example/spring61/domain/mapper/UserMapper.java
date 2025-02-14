package com.example.spring61.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.UserDto;

@Mapper
public interface UserMapper {
	public abstract UserDto findByUserNum(Long userNum) throws Exception;
	public abstract int update(UserDto userDto) throws Exception;
	public abstract int insert(UserDto userDto) throws Exception;
	public abstract UserDto findByUserId(String userId) throws Exception;
	public abstract UserDto findByUserEmail(String userEmail) throws Exception;	
}
