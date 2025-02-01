package com.example.spring61.domain.service.file;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.FileDto;

public interface FileService {
	public abstract String getDatePath();
	public abstract Boolean isImage(File file);
	public abstract void createThumbnail(File originalFile, File thumbnailFile);
	public abstract boolean insert(FileDto fileDto);
	public abstract List<FileDto> findByBoardNum(Long boardNum);
	public abstract FileDto uploadFile(MultipartFile multipartFile, File uploadPath, String datePath);
	public abstract File getUploadPath(String deaultDirectory, String datePath);
	public abstract void insertFiles(BoardDto boardDto);
	public abstract int delete(Long boardNum);
}
