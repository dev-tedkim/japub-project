package com.app.japub.domain.service.admin;

import org.springframework.stereotype.Service;

import com.app.japub.common.DbConstants;
import com.app.japub.domain.dao.admin.AdminDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	private final AdminDao adminDao;

	@Override
	public boolean deleteByBoardNum(Long boardNum) {
		try {
			return adminDao.deleteByBoardNum(boardNum) == DbConstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("adminService deleteByBoardNum error");
			return false;
		}
	}

	@Override
	public boolean deleteByCommentNum(Long commentNum) {
		try {
			return adminDao.deleteByCommentNum(commentNum) == DbConstants.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("adminService deleteByCommentNum error");
			return false;
		}
	}
}
