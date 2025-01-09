package com.example.spring61.domain.dao.comment;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.CommentDto;
import com.example.spring61.domain.dto.Criteria;

@Repository
public interface CommentDao {
	public abstract int insert(CommentDto commentDto) throws Exception;

	public abstract int update(CommentDto commentDto) throws Exception;

	public abstract int delete(@Param("cno") Long cno, @Param("userNum") Long userNum) throws Exception;

	public abstract List<CommentDto> findByBoardNumAndCriteria(@Param("boardNum") Long boardNum,
			@Param("criteria") Criteria criteria) throws Exception;

	public abstract int getTotalByBoardNum(Long boardNum) throws Exception;
}
