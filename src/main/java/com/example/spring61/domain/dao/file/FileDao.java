package com.example.spring61.domain.dao.file;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.FileDto;

public interface FileDao {
	public abstract List<FileDto> findByBoardNum(Long boardNum) throws Exception;
	public abstract int insert(FileDto fileDto) throws Exception;
	public abstract int deleteByBoardNum(@Param("boardNum") Long boardNum) throws Exception;
}
