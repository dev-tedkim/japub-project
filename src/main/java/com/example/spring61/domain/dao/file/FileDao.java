package com.example.spring61.domain.dao.file;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.spring61.domain.dto.FileDto;

public interface FileDao {
	public abstract int insert(FileDto fileDto) throws Exception;
	public abstract List<FileDto> findByBoardNum(Long boardNum) throws Exception;
	public abstract int delete(Long boardNum) throws Exception;
}
