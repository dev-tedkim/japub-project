package com.app.japub.domain.dao.admin;

import org.springframework.stereotype.Repository;

import com.app.japub.domain.dto.CommentDto;
import com.app.japub.domain.mapper.AdminMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminDaoImpl implements AdminDao {
	private final AdminMapper adminMapper;

	@Override
	public int deleteByBoardNum(Long boardNum) {
		return adminMapper.deleteByBoardNum(boardNum);
	}

	@Override
	public int deleteByCommentNum(Long commentNum) {
		return adminMapper.deleteByCommentNum(commentNum);
	}

	@Override
	public int updateComment(CommentDto commentDto) {
		return adminMapper.updateComment(commentDto);
	}

}
