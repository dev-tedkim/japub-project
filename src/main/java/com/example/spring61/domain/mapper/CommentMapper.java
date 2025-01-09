package com.example.spring61.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.CommentDto;
import com.example.spring61.domain.dto.Criteria;

@Mapper
public interface CommentMapper {
	public abstract int insert(CommentDto commentDto) throws Exception;
	public abstract int update(CommentDto commentDto) throws Exception;
	public abstract int delete(@Param("cno") Long cno, @Param("userNum") Long userNum) throws Exception;
	public abstract List<CommentDto> findByBoardNumAndCriteria(@Param("boardNum") Long boardNum, @Param("criteria")Criteria criteria) throws Exception;
	public abstract int getTotalByBoardNum(Long boardNum) throws Exception;
}
