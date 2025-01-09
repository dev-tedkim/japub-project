package com.example.spring61.domain.service.file;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.FileDto;

public interface FileService {
	public abstract List<FileDto> findByBoardNum(Long boardNum);
	public abstract boolean insert(FileDto fileDto);
	public abstract int deleteByBoardNum(@Param("boardNum") Long boardNum);
}
