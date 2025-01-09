package com.example.spring61.domain.dao.board;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.Criteria;

public interface BoardDao {
	public abstract List<BoardDto> findAllByCriteria(Criteria criteria) throws Exception;
	public abstract int insert(BoardDto boardDto) throws Exception;
	public abstract int  update(BoardDto boardDto) throws Exception;
	public abstract int delete(@Param("boardNum") Long boardNum, @Param("userNum")Long userNum) throws Exception;
	public abstract BoardDto findByBoardNum(Long boardNum) throws Exception;
	public abstract int updateBoardHitByBoardNum(Long boardNum) throws Exception;
	public abstract int getTotal(Criteria criteria) throws Exception;
}
