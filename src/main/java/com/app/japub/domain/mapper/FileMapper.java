package com.app.japub.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.app.japub.domain.dto.FileDto;

@Mapper
public interface FileMapper {
	public abstract int insert(FileDto fileDto);

	public abstract List<FileDto> findByBoardNum(Long boardNum);

	public abstract int deleteByFileNum(Long fileNum);
	
	public abstract int countByBoardNum(Long boardNum);
	
	public abstract List<FileDto> findByYesterDay();
}
