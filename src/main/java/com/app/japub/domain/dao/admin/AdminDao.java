package com.app.japub.domain.dao.admin;

import com.app.japub.domain.dto.CommentDto;

public interface AdminDao {
	public abstract int deleteByBoardNum(Long boardNum);
	public abstract int deleteByCommentNum(Long commentNum);
	public abstract int updateComment(CommentDto commentDto);
}
