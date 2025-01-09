package com.example.spring61.domain.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring61.domain.dao.user.UserDao;
import com.example.spring61.domain.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Autowired
	private final UserDao userDao;
	private final int SUCCESS_CODE = 1;

	@Override
	public UserDto findByUserIdAndUserPassword(String userId, String userPassword) {
		try {
			return userDao.findByUserIdAndUserPassword(userId, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService findByUserIdAndUserPassword error");
		}
		return null;
	}

	@Override
	public UserDto findByUserNum(Long userNum) {
		try {
			return userDao.findByUserNum(userNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService findByUserNum error");
		}
		return null;
	}

	@Override
	public boolean update(UserDto userDto) {
		try {
			return userDao.update(userDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService update error");
		}
		return false;
	}

	@Override
	public boolean insert(UserDto userDto) {
		try {
			return userDao.insert(userDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService insert error");
		}
		return false;
	}

	@Override
	public UserDto checkPassword(Long userNum, String userPassword) {
		try {
			return userDao.findByUserNumAndUserPassword(userNum, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService checkPassword error");
		}
		return null;
	}
}
