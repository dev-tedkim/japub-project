package com.app.japub.domain.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
	public abstract int deleteByBoardNum(Long boardNum);
	public abstract int deleteByCommentNum(Long commentNum);
}
