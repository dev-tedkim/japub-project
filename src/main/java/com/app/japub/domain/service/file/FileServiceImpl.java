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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.japub.common.DbConstants;
import com.app.japub.domain.dao.file.FileDao;
import com.app.japub.domain.dto.BoardDto;
import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.dto.ProductDto;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileDao fileDao;
	private static final int THUMBNAIL_SIZE = 100;

	@Override
	public void insertFiles(BoardDto boardDto) {
		List<FileDto> files = boardDto.getFiles();
		if (files == null || files.isEmpty()) {
			return;
		}
		for (FileDto fileDto : files) {
			fileDto.setBoardNum(boardDto.getBoardNum());
			if (fileDao.insert(fileDto) != DbConstants.SUCCESS_CODE) {
				throw new RuntimeException("fileService insert error");
			}
		}
	}

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
	public void deleteFiles(BoardDto boardDto) {
		List<FileDto> deleteFiles = boardDto.getDeleteFiles();
		if (deleteFiles == null || deleteFiles.isEmpty()) {
			return;
		}
		for (FileDto fileDto : deleteFiles) {
			if (fileDao.deleteByFileNum(fileDto.getFileNum()) != DbConstants.SUCCESS_CODE) {
				throw new RuntimeException("fileService deleteFiles error");
			}
		}
	}

	@Override
	public String getDatePath() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
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
	public void createThumbnails(File originalFile, File thumbnailFile, int thumbnailSize) {
		try (InputStream in = new FileInputStream(originalFile);
				OutputStream out = new FileOutputStream(thumbnailFile);) {
			Thumbnailator.createThumbnail(in, out, thumbnailSize, thumbnailSize);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fileService createThumbnails error", e);
		}
	}

	@Override
	public boolean isImage(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType != null && contentType.startsWith("image/");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fileService isImage error", e);
		}
	}

	@Override
	public FileDto upload(MultipartFile multipartFile, File uploadPath, String datePath) {
		String uuid = UUID.randomUUID().toString();
		String originalFileName = multipartFile.getOriginalFilename();
		String fileName = uuid + "_" + originalFileName;
		File file = new File(uploadPath, fileName);
		try {
			multipartFile.transferTo(file);
			FileDto fileDto = new FileDto();
			fileDto.setFileUuid(uuid);
			fileDto.setFileName(originalFileName);
			fileDto.setFileSize(multipartFile.getSize());
			fileDto.setFileUploadPath(datePath);
			if (isImage(file)) {
				fileDto.setFileType(true);
				File thumbnailFile = new File(uploadPath, "t_" + fileName);
				createThumbnails(file, thumbnailFile, THUMBNAIL_SIZE);
			}
			return fileDto;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fileService upload error", e);
		}
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

	@Override
	public void autoDeleteFiles(List<FileDto> yesterdayFiles, String defaultPath, String datePath) {
		List<Path> filePaths = yesterdayFiles.stream().map(
				file -> Paths.get(defaultPath, file.getFileUploadPath(), file.getFileUuid() + "_" + file.getFileName()))
				.collect(Collectors.toList());

		yesterdayFiles.stream().filter(FileDto::isFileType)
				.map(file -> Paths.get(defaultPath, file.getFileUploadPath(),
						"t_" + file.getFileUuid() + "_" + file.getFileName()))
				.collect(Collectors.toList()).forEach(filePaths::add);

		File[] deleteFiles = Paths.get(defaultPath, datePath).toFile()
				.listFiles(file -> !filePaths.contains(file.toPath()));

		if (deleteFiles != null) {
			Arrays.asList(deleteFiles).forEach(File::delete);
		}
	}

}
