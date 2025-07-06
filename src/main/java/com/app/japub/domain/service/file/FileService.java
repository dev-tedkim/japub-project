package com.app.japub.domain.service.file;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.dto.ProductDto;

public interface FileService {
	public abstract List<FileDto> findByBoardNum(Long boardNum);
	public abstract void insertFiles(BoardDto boardDto);
	public abstract void deleteFiles(BoardDto boardDto);
	public abstract File getUploadPath(String parent, String child);
	public abstract boolean isImage(File file);
	public abstract String getContentType(File file);
	public abstract void createThumbnails(File originalFile, File thumbnailFile, int size);
	public abstract FileDto upload(MultipartFile multipartFile, String defaultDirectory, String datePath);
	public abstract void autoDeleteFiles(List<FileDto> yesterDayFiles, String defaultDirectory, String yesterDayPath);
	public abstract String getThumbnailPath(FileDto fileDto);
	public abstract void setFilePath(FileDto fileDto);
	public abstract List<FileDto> findByYesterDay();
}
