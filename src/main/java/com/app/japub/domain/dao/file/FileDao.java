package com.app.japub.domain.dao.file;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app.japub.domain.dto.FileDto;

@Repository
public interface FileDao {
	public abstract int insert(FileDto fileDto);

	public abstract List<FileDto> findByBoardNum(Long boardNum);

	public abstract int deleteByFileNum(Long fileNum);
	
	public abstract List<FileDto> findByYesterDay();
}
