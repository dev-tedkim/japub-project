package com.app.japub.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.Criteria;

@Mapper
public interface BoardMapper {
	public abstract List<BoardDto> findByCriteria(Criteria criteria);

	public abstract BoardDto findByBoardNum(Long boardNum);

	public abstract int insert(BoardDto BoardDto);

	public abstract int update(BoardDto BoardDto);

	public abstract int delete(@Param("userNum") Long userNum, @Param("boardNum") Long boardNum);

	public abstract Long countByCriteria(Criteria criteria);

	public abstract int incrementBoardReadCount(Long boardNum);
	
	public abstract BoardDto findByUserNumAndBoardNum(@Param("userNum") Long userNum, @Param("boardNum") Long boardNum);
}
