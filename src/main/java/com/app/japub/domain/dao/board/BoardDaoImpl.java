package com.app.japub.domain.dao.board;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.Criteria;
import com.app.japub.domain.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardDaoImpl implements BoardDao{
	private final BoardMapper boardMapper;

	@Override
	public List<BoardDto> findByCriteria(Criteria criteria)  {
		return boardMapper.findByCriteria(criteria);
	}

	@Override
	public BoardDto findByBoardNum(Long boardNum)  {
		return boardMapper.findByBoardNum(boardNum);
	}

	@Override
	public int insert(BoardDto BoardDto)  {
		return boardMapper.insert(BoardDto);
	}

	@Override
	public int update(BoardDto BoardDto)  {
		return boardMapper.update(BoardDto);
	}

	@Override
	public int delete(Long userNum, Long boardNum)  {
		return boardMapper.delete(userNum, boardNum);
	}

	@Override
	public Long countByCriteria(Criteria criteria)  {
		return boardMapper.countByCriteria(criteria);
	}

	@Override
	public int incrementBoardReadCount(Long boardNum)  {
		return boardMapper.incrementBoardReadCount(boardNum);
	}

	@Override
	public BoardDto findByUserNumAndBoardNum(Long userNum, Long boardNum) {
		return boardMapper.findByUserNumAndBoardNum(userNum, boardNum);
	}
}
