package com.example.spring61.domain.service.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring61.domain.dao.comment.CommentDao;
import com.example.spring61.domain.dto.CommentDto;
import com.example.spring61.domain.dto.Criteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
	@Autowired
	private final CommentDao commentDao;
	private final int SUCCESS_CODE = 1;
	@Override
	public boolean insert(CommentDto commentDto) {
		try {
			return commentDao.insert(commentDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService insert error");
		}
		return false;
	}
	@Override
	public boolean update(CommentDto commentDto) {
		try {
			return commentDao.update(commentDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService update error");
		}
		return false;
	}
	@Override
	public boolean delete(Long cno, Long userNum) {
		try {
			return commentDao.delete(cno, userNum) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService delete error");
		}
		return false;
	}
	@Override
	public List<CommentDto> findByBoardNumAndCriteria(Long boardNum, Criteria criteria) {
		try {
			return commentDao.findByBoardNumAndCriteria(boardNum, criteria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService findByBoardNumAndCriteria error");
		}
		return null;
	}
	@Override
	public int getTotalByBoardNum(Long boardNum) {
		try {
			return commentDao.getTotalByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentService getTotalByBoardNum error");
		}
		return 0;
	}


}
