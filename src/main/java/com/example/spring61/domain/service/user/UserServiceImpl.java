package com.example.spring61.domain.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring61.domain.dao.user.UserDao;
import com.example.spring61.domain.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserDao userDao;
	private final PasswordService passwordService;
	private final MailService mailService;
	private final int SUCCESS_CODE = 1;

	@Override
	public UserDto findByUserIdAndUserPassword(String userId, String userPassword) {
		try {
			UserDto userDto = userDao.findByUserId(userId);
			if (userDto != null && passwordService.matches(userPassword, userDto.getUserPassword())) {
				return userDto;
			}
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
			String password = userDto.getUserPassword();
			if (password != null) {
				userDto.setUserPassword(passwordService.encodePassword(password));
			}
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
			String encodedPassword = passwordService.encodePassword(userDto.getUserPassword());
			userDto.setUserPassword(encodedPassword);
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
			UserDto userDto = userDao.findByUserNum(userNum);
			if (userDto != null && passwordService.matches(userPassword, userDto.getUserPassword())) {
				return userDto;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService checkPassword error");
		}
		return null;
	}

	@Override
	public UserDto findByUserId(String userId) {
		try {
			return userDao.findByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService findByUserId error");
		}
		return null;
	}

	@Override
	public UserDto findByUserEmail(String userEmail) {
		try {
			return userDao.findByUserEmail(userEmail);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService findByUserEmail error");
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean resetPasswordAndSendMail(UserDto userDto) {
		try {
			String tempPassword = passwordService.createTempPassword();
			userDto.setUserPassword(tempPassword);
			if (!update(userDto)) {
				throw new RuntimeException("resetPasswordAndSendMail update error");
			}
			userDto.setUserPassword(tempPassword); /* password를 다시 temp로 변경해서 전달 */
			if (!mailService.sendEmail(userDto)) {
				throw new RuntimeException("resetPasswordAndSendMail mailService error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService findCredentialsByEmail error");
			throw new RuntimeException(e);
		}
		return true;
	}

	@Override
	public boolean updateTempPassword(UserDto userDto) {
		try {
			return userDao.updateTempPassword(userDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("userService updateTempPassword error");
		}
		return false;
	}
}
