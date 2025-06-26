package com.app.japub.domain.service.admin;

import com.app.japub.domain.dto.CommentDto;


public interface AdminService {
	public abstract boolean deleteByBoardNum(Long boardNum);
	public abstract boolean deleteByCommentNum(Long commentNum);
	public abstract boolean updateComment(CommentDto commentDto);
}
