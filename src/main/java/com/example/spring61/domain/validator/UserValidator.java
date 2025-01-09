package com.example.spring61.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.example.spring61.domain.dto.UserDto;

public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "userId", "required");
		ValidationUtils.rejectIfEmpty(errors, "userPassword", "required");
		ValidationUtils.rejectIfEmpty(errors, "userEmail", "required");
		ValidationUtils.rejectIfEmpty(errors, "userBirth", "required");
		ValidationUtils.rejectIfEmpty(errors, "userZipCode", "required");
		ValidationUtils.rejectIfEmpty(errors, "userAddress", "required");
	}

}
