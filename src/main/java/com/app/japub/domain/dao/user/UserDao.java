package com.app.japub.domain.dao.user;

import com.app.japub.domain.dto.UserDto;

public interface UserDao {
	public abstract UserDto findByUserId(String userId);

	public abstract UserDto findByUserEmail(String userEmail);

	public abstract UserDto findByUserNum(Long userNum);

	public abstract int insert(UserDto userDto);

	public abstract int update(UserDto userDto);

	public abstract int delete(Long userNum);
}
