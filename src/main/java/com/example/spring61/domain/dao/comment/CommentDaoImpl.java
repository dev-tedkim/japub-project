package com.example.spring61.domain.dao.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.CommentDto;
import com.example.spring61.domain.dto.Criteria;
import com.example.spring61.domain.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao{
	@Autowired
	private final CommentMapper commentMapper;

	@Override
	public int insert(CommentDto commentDto) throws Exception {
		return commentMapper.insert(commentDto);
	}

	@Override
	public int update(CommentDto commentDto) throws Exception {
		return commentMapper.update(commentDto);
	}

	@Override
	public int delete(Long cno, Long userNum) throws Exception {
		return commentMapper.delete(cno, userNum);
	}

	@Override
	public List<CommentDto> findByBoardNumAndCriteria(Long boardNum, Criteria criteria) throws Exception {
		return commentMapper.findByBoardNumAndCriteria(boardNum, criteria);
	}

	@Override
	public int getTotalByBoardNum(Long boardNum) throws Exception {
		return commentMapper.getTotalByBoardNum(boardNum);
	}
}
