package com.example.spring61.domain.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.UserDto;
import com.example.spring61.domain.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao{
	@Autowired
	private final UserMapper userMapper;

	@Override
	public UserDto findByUserIdAndUserPassword(String userId, String userPassword) throws Exception {
		return userMapper.findByUserIdAndUserPassword(userId, userPassword);
	}

	@Override
	public UserDto findByUserNum(Long userNum) throws Exception {
		return userMapper.findByUserNum(userNum);
	}

	@Override
	public int update(UserDto userDto) throws Exception {
		return userMapper.update(userDto);
	}

	@Override
	public int insert(UserDto userDto) throws Exception {
		return userMapper.insert(userDto);
	}

	@Override
	public UserDto findByUserNumAndUserPassword(Long userNum, String userPassword) throws Exception {
		return userMapper.findByUserNumAndUserPassword(userNum, userPassword);
	}

	@Override
	public UserDto findByUserId(String userId) throws Exception {
		return userMapper.findByUserId(userId);
	}
}
