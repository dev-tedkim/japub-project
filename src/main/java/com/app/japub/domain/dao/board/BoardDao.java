package com.app.japub.domain.dao.board;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.Criteria;

public interface BoardDao {
	public abstract List<BoardDto> findByCriteria(Criteria criteria);

	public abstract BoardDto findByBoardNum(Long boardNum);

	public abstract int insert(BoardDto BoardDto);

	public abstract int update(BoardDto BoardDto);

	public abstract int delete(@Param("userNum") Long userNum, @Param("boardNum") Long boardNum);

	public abstract Long countByCriteria(Criteria criteria);

	public abstract int incrementBoardReadCount(Long boardNum);
}
