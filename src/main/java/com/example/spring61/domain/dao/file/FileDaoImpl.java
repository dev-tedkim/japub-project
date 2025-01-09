package com.example.spring61.domain.dao.file;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.FileDto;
import com.example.spring61.domain.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class FileDaoImpl implements FileDao{
	@Autowired
	private final FileMapper fileMapper;

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) throws Exception {
		return fileMapper.findByBoardNum(boardNum);
	}

	@Override
	public int insert(FileDto fileDto) throws Exception {
		return fileMapper.insert(fileDto);
	}

	@Override
	public int deleteByBoardNum(Long boardNum) throws Exception {
		return fileMapper.deleteByBoardNum(boardNum);
	}

}
