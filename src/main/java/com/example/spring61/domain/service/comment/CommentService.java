package com.example.spring61.domain.service.comment;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.CommentDto;
import com.example.spring61.domain.dto.Criteria;

public interface CommentService {
	public abstract boolean insert(CommentDto commentDto);

	public abstract boolean update(CommentDto commentDto);

	public abstract boolean delete(@Param("cno") Long cno, @Param("userNum") Long userNum);

	public abstract List<CommentDto> findByBoardNumAndCriteria(@Param("boardNum") Long boardNum,
			@Param("criteria") Criteria criteria);

	public abstract int getTotalByBoardNum(Long boardNum);
}
