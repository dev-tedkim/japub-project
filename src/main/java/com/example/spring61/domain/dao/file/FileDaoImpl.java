package com.example.spring61.domain.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.spring61.domain.dto.FileDto;
import com.example.spring61.domain.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FileDaoImpl implements FileDao {
	private final FileMapper fileMapper;

	@Override
	public int insert(FileDto fileDto) throws Exception {
		return fileMapper.insert(fileDto);
	}

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) throws Exception {
		return fileMapper.findByBoardNum(boardNum);
	}

	@Override
	public int delete(Long boardNum) throws Exception {
		return fileMapper.delete(boardNum);
	}

}
