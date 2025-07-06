package com.app.japub.domain.service.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.japub.common.DbConstants;
import com.app.japub.domain.dao.file.FileDao;
import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.FileDto;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileDao fileDao;
	private static final int THUMBNAIL_SIZE = 100;

	@Override
	public List<FileDto> findByBoardNum(Long boardNum) {
		try {
			return fileDao.findByBoardNum(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fileService findByBoardNum error");
			return Collections.emptyList();
		}
	}

	@Override
	public void insertFiles(BoardDto boardDto) {
		List<FileDto> insertFiles = boardDto.getInsertFiles();
		if (insertFiles == null || insertFiles.isEmpty()) {
			return;
		}

		for (FileDto insertFile : insertFiles) {
			insertFile.setBoardNum(boardDto.getBoardNum());
			if (fileDao.insert(insertFile) != DbConstants.SUCCESS_CODE) {
				throw new RuntimeException("fileService insertFiles error");
			}
		}
	}

	@Override
	public void deleteFiles(BoardDto boardDto) {
		List<FileDto> deleteFiles = boardDto.getDeleteFiles();
		if (deleteFiles == null || deleteFiles.isEmpty()) {
			return;
		}

		for (FileDto deleteFile : deleteFiles) {
			if (fileDao.deleteByFileNum(deleteFile.getFileNum()) != DbConstants.SUCCESS_CODE) {
				throw new RuntimeException("fileService deleteFiles error");
			}
		}
	}

	@Override
	public File getUploadPath(String parent, String child) {
		File uploadPath = new File(parent, child);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		return uploadPath;
	}

	@Override
	public boolean isImage(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType != null && contentType.startsWith("image/");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fileService isImage error");
		}
	}

	@Override
	public String getContentType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			if (contentType == null) {
				String fileName = file.getName().toLowerCase();
				if (!fileName.contains(".")) {
					throw new RuntimeException("파일 확장자가 존재하지 않습니다.");
				} else if (fileName.endsWith(".jpg")) {
					contentType = "image/jpeg";
				} else if (fileName.endsWith(".jpeg")) {
					contentType = "image/jpeg";
				} else if (fileName.endsWith(".gif")) {
					contentType = "image/gif";
				} else if (fileName.endsWith(".png")) {
					contentType = "image/png";
				} else {
					throw new RuntimeException("지원하지 않는 파일 형식입니다.");
				}
			}
			return contentType;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fileService getContentType error", e);
		}
	}

	@Override
	public void createThumbnails(File originalFile, File thumbnailFile, int size) {
		try (InputStream in = new FileInputStream(originalFile);
				OutputStream out = new FileOutputStream(thumbnailFile);) {
			Thumbnailator.createThumbnail(in, out, size, size);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fileService createThumbnails error");
		}
	}

	@Override
	public FileDto upload(MultipartFile multipartFile, String defaultDirectory, String datePath) {
		File uploadPath = getUploadPath(defaultDirectory, datePath);
		String fileUuid = UUID.randomUUID().toString();
		String originalFileName = multipartFile.getOriginalFilename();
		String fileName = fileUuid + "_" + originalFileName;
		File file = new File(uploadPath, fileName);
		try {
			multipartFile.transferTo(file);
			FileDto fileDto = new FileDto();
			fileDto.setFileUuid(fileUuid);
			fileDto.setFileName(originalFileName);
			fileDto.setFileSize(multipartFile.getSize());
			fileDto.setFileUploadPath(datePath);
			if (isImage(file)) {
				fileDto.setFileType(true);
				File thumbnailFile = new File(uploadPath, "t_" + fileName);
				createThumbnails(file, thumbnailFile, THUMBNAIL_SIZE);
			}
			return fileDto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fileService upload error");
		}
	}

	@Override
	public void autoDeleteFiles(List<FileDto> yesterDayFiles, String defaultDirectory, String yesterDayPath) {
		List<Path> paths = new ArrayList<>();
		yesterDayFiles.stream().map(file -> Paths.get(defaultDirectory, getThumbnailPath(file).replace("t_", "")))
				.forEach(paths::add);
		yesterDayFiles.stream().map(file -> Paths.get(defaultDirectory, getThumbnailPath(file))).forEach(paths::add);
		Stream.of(new File(defaultDirectory, yesterDayPath).listFiles()).filter(file -> !paths.contains(file.toPath()))
				.forEach(File::delete);
	}

	@Override
	public String getThumbnailPath(FileDto fileDto) {
		return fileDto.getFileUploadPath() + "/" + "t_" + fileDto.getFileUuid() + "_" + fileDto.getFileName();
	}

	@Override
	public void setFilePath(FileDto fileDto) {
		fileDto.setFilePath(getThumbnailPath(fileDto).replace("t_", ""));
	}

	@Override
	public List<FileDto> findByYesterDay() {
		try {
			return fileDao.findByYesterDay();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fileService findByYesterDay error");
			return Collections.emptyList();
		}
	}


}
