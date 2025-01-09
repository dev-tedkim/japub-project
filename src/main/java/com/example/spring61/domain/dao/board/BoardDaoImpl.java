package com.example.spring61.domain.dao.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.Criteria;
import com.example.spring61.domain.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardDaoImpl implements BoardDao{
	@Autowired
	private final BoardMapper boardMapper;

	@Override
	public List<BoardDto> findAllByCriteria(Criteria criteria) throws Exception {
		return boardMapper.findAllByCriteria(criteria);
	}

	@Override
	public int insert(BoardDto boardDto) throws Exception {
		return boardMapper.insert(boardDto);
	}

	@Override
	public int update(BoardDto boardDto) throws Exception {
		return boardMapper.update(boardDto);
	}

	@Override
	public int delete(Long boardNum, Long userNum) throws Exception {
		return boardMapper.delete(boardNum, userNum);
	}

	@Override
	public BoardDto findByBoardNum(Long boardNum) throws Exception{
		return boardMapper.findByBoardNum(boardNum);
	}

	@Override
	public int updateBoardHitByBoardNum(Long boardNum) throws Exception{
		return boardMapper.updateBoardHitByBoardNum(boardNum);
	}

	@Override
	public int getTotal(Criteria criteria) throws Exception {
		return boardMapper.getTotal(criteria);
	}
	
}
