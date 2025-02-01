package com.example.spring61.domain.service.board;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.Criteria;

public interface BoardService {
	public abstract List<BoardDto> findAllByCriteria(Criteria criteria);

	public abstract void insert(BoardDto boardDto);

	public abstract void update(BoardDto boardDto);

	public abstract boolean delete(@Param("boardNum") Long boardNum, @Param("userNum") Long userNum);

	public abstract BoardDto findByBoardNum(Long boardNum);

	public abstract boolean updateBoardHitByBoardNum(Long boardNum);

	public abstract int getTotal(Criteria criteria);
}
