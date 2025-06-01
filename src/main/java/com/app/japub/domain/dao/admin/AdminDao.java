package com.app.japub.domain.dao.admin;

public interface AdminDao {
	public abstract int deleteByBoardNum(Long boardNum);
	public abstract int deleteByCommentNum(Long commentNum);
}
