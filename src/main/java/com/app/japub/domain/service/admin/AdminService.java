package com.app.japub.domain.service.admin;

import org.springframework.stereotype.Service;


public interface AdminService {
	public abstract boolean deleteByBoardNum(Long boardNum);
	public abstract boolean deleteByCommentNum(Long commentNum);
}
