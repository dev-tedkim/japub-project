package com.app.japub.domain.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FileDaoImpl implements FileDao {
	private final FileMapper fileMapper;

	@Override
	public int insert(FileDto fileDto) {
		return fileMapper.insert(fileDto);
	}

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) {
		return fileMapper.findByBoardNum(boardNum);
	}

	@Override
	public int deleteByFileNum(Long fileNum) {
		return fileMapper.deleteByFileNum(fileNum);
	}

	@Override
	public List<FileDto> findByYesterDay() {
		return fileMapper.findByYesterDay();
	}
}
