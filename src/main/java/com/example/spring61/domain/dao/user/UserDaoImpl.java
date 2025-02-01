package com.example.spring61.domain.dao.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.UserDto;
import com.example.spring61.domain.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
	@Autowired
	private final UserMapper userMapper;

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
	public UserDto findByUserId(String userId) throws Exception {
		return userMapper.findByUserId(userId);
	}

	@Override
	public UserDto findByUserEmail(String userEmail) throws Exception {
		return userMapper.findByUserEmail(userEmail);
	}

}
