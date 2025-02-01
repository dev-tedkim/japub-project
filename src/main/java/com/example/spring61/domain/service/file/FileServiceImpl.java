package com.example.spring61.domain.service.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring61.domain.dao.file.FileDao;
import com.example.spring61.domain.dto.BoardDto;
import com.example.spring61.domain.dto.FileDto;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileDao fileDao;
	private final int SUCCESS_CODE = 1;

	@Override
	public String getDatePath() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	}

	@Override
	public Boolean isImage(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType != null && contentType.startsWith("image");
		} catch (IOException e) {
			throw new RuntimeException("fileService isImage error", e);
		}
	}

	@Override
	public void createThumbnail(File originalFile, File thumbnailFile) {
		try (InputStream in = new FileInputStream(originalFile);
				OutputStream out = new FileOutputStream(thumbnailFile);) {
			Thumbnailator.createThumbnail(in, out, 100, 100);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fileService createThumbnail error", e);
		}
	}

	@Override
	public boolean insert(FileDto fileDto) {
		try {
			return fileDao.insert(fileDto) == SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fileService insert error");
			return false;
		}
	}

	@Override
	public void insertFiles(BoardDto boardDto) {
		List<FileDto> files = boardDto.getFiles();
		if (files == null || files.isEmpty()) {
			return;
		}
		for (FileDto fileDto : files) {
			fileDto.setBoardNum(boardDto.getBoardNum());
			if (!insert(fileDto)) {
				throw new RuntimeException("boardService file insert error");
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
			return null;
		}
	}

	@Override
	public FileDto uploadFile(MultipartFile multipartFile, File uploadPath, String datePath) {
		String uuid = UUID.randomUUID().toString();
		String originalFileName = multipartFile.getOriginalFilename();
		String fileName = uuid + "_" + originalFileName;
		File file = new File(uploadPath, fileName);

		try {
			multipartFile.transferTo(file);
			FileDto fileDto = new FileDto();
			fileDto.setUuid(uuid);
			fileDto.setUploadPath(datePath);
			fileDto.setFileName(originalFileName);

			if (isImage(file)) {
				fileDto.setFileType(true);
				File thumbnailFile = new File(uploadPath, "t_" + uuid + "_" + originalFileName);
				createThumbnail(file, thumbnailFile);
			}
			return fileDto;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fileService saveFile error", e);
		}
	}

	@Override
	public File getUploadPath(String deaultDirectory, String datePath) {
		File uploadPath = new File(deaultDirectory, datePath);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		return uploadPath;
	}

	@Override
	public int delete(Long boardNum) {
		try {
			return fileDao.delete(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
