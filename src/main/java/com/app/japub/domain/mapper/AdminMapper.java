package com.app.japub.domain.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.app.japub.domain.dto.CommentDto;

@Mapper
public interface AdminMapper {
	public abstract int deleteByBoardNum(Long boardNum);
	public abstract int deleteByCommentNum(Long commentNum);
	public abstract int updateComment(CommentDto commentDto);
}
