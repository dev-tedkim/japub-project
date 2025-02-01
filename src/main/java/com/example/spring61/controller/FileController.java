package com.example.spring61.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring61.domain.dto.FileDto;
import com.example.spring61.domain.service.file.FileService;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@RequestMapping("/files")
@RestController
@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;
	private final String DEFAULT_DIRECTORY = "c:/upload";
	
	@PostMapping(value ="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> upload(MultipartFile[] multipartFiles) {
		List<FileDto> files = new ArrayList<>();
		if (multipartFiles != null) {
			String datePath = fileService.getDatePath();
			File uploadPath = fileService.getUploadPath(DEFAULT_DIRECTORY, datePath);
			for (MultipartFile multipartFile : multipartFiles) {
				try {
					FileDto fileDto = fileService.uploadFile(multipartFile, uploadPath, datePath);
					files.add(fileDto);
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<List<FileDto>>(HttpStatus.BAD_REQUEST);
				}
			}
		}
		return new ResponseEntity<List<FileDto>>(files, HttpStatus.OK);
	}

	@GetMapping("/display")
	public ResponseEntity<byte[]> display(String fileName) {
		File file = new File(DEFAULT_DIRECTORY, fileName);
		if (!file.exists()) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}

		try {
			String contentType = Files.probeContentType(file.toPath());
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", contentType == null ? "application/actet-stream" : contentType);
			byte[] result = FileCopyUtils.copyToByteArray(file);
			return new ResponseEntity<byte[]>(result, header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/{boardNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> getFiles(@PathVariable Long boardNum) {
		return new ResponseEntity<List<FileDto>>(fileService.findByBoardNum(boardNum), HttpStatus.OK);
	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> download(String fileName) {
		File file = new File(DEFAULT_DIRECTORY, fileName);
		if (!file.exists()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Resource resource = new FileSystemResource(file);
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

	@DeleteMapping(value = "/{boardNum}", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> delete(@RequestBody FileDto fileDto) {
		String fileName = fileDto.getUploadPath() + "/t_" + fileDto.getUuid() + "_" + fileDto.getFileName();
		File file = new File(DEFAULT_DIRECTORY, fileName);
		if (file.exists()) {
			file.delete();
		}
		file = new File(DEFAULT_DIRECTORY, fileName.replace("t_", ""));
		if (file.exists()) {
			file.delete();
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
