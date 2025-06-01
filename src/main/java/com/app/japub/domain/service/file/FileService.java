package com.app.japub.domain.service.file;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.dto.ProductDto;

public interface FileService {
	public abstract void insertFiles(BoardDto boardDto);
	public abstract List<FileDto> findByBoardNum(Long boardNum); 
	public abstract void deleteFiles(BoardDto boardDto);
	public abstract String getDatePath();
	public abstract File getUploadPath(String parent, String child);
	public abstract void createThumbnails(File originalFile, File thumbnailFile, int thumbnailSize);
	public abstract boolean isImage(File file);
	public abstract FileDto upload(MultipartFile multipartFile, File uploadPath, String datePath);
	public abstract List<FileDto> findByYesterDay();
	public abstract void autoDeleteFiles(List<FileDto> yesterdayFiles, String defaultPath, String datePath);
}
