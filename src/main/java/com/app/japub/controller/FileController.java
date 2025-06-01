package com.app.japub.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.japub.domain.dto.FileDto;
import com.app.japub.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;
	private static final String DEFAULT_DERECTORY = "C:/upload/download";

	@GetMapping("/display")
	public ResponseEntity<byte[]> display(String fileName) {
		File file = new File(DEFAULT_DERECTORY, fileName);
		if (!file.exists()) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
		try {
			HttpHeaders header = new HttpHeaders();
			String contentType = Files.probeContentType(file.toPath());
			contentType = contentType == null ? "image/jpeg" : contentType;
			header.add("Content-Type", contentType);
			byte[] result = FileCopyUtils.copyToByteArray(file);
			return new ResponseEntity<byte[]>(result, header, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> download(String fileName) {
		File file = new File(DEFAULT_DERECTORY, fileName);
		Resource resource = new FileSystemResource(file);
		if (!file.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		String resourceName = resource.getFilename();
		resourceName = resourceName.substring(resourceName.indexOf("_") + 1);
		HttpHeaders header = new HttpHeaders();
		try {
			header.add("Content-Disposition",
					"attachment;filename=" + new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/{boardNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> getFiles(@PathVariable Long boardNum) {
		List<FileDto> files = fileService.findByBoardNum(boardNum);
		return new ResponseEntity<List<FileDto>>(files, HttpStatus.OK);
	}

	@GetMapping(value = "/size/{boardNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> getSize(@PathVariable Long boardNum) {
		Integer size = fileService.findByBoardNum(boardNum).size();
		return new ResponseEntity<Integer>(size, HttpStatus.OK);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> upload(MultipartFile[] multipartFiles) {
		String datePath = fileService.getDatePath();
		File uploadPath = fileService.getUploadPath(DEFAULT_DERECTORY, datePath);
		List<FileDto> files = new ArrayList<>();
		if (multipartFiles == null || multipartFiles.length == 0) {
			return new ResponseEntity<List<FileDto>>(HttpStatus.OK);
		}
		for (MultipartFile multipartFile : multipartFiles) {
			try {
				FileDto fileDto = fileService.upload(multipartFile, uploadPath, datePath);
				files.add(fileDto);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<List<FileDto>>(HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<List<FileDto>>(files, HttpStatus.OK);
	}
}
