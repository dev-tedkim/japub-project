package com.example.spring61.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64.Decoder;
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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
	private final String DEFAULT_DIRECTORY_PATH = "C:/upload";
	@Autowired
	private final FileService fileService;

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FileDto>> upload(MultipartFile[] multipartFiles) {
		String datePath = getDatePath();
		File uploadPath = new File(DEFAULT_DIRECTORY_PATH, datePath);
		List<FileDto> files = new ArrayList<>();
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		if (multipartFiles != null) {
			for (MultipartFile multipartFile : multipartFiles) {
				FileDto fileDto = new FileDto();
				String uuid = UUID.randomUUID().toString();
				String originalFileName = multipartFile.getOriginalFilename();
				String fileName = uuid + "_" + originalFileName;
				try {
					File file = new File(uploadPath, fileName);
					multipartFile.transferTo(file);
					fileDto.setUuid(uuid);
					fileDto.setFileName(originalFileName);
					fileDto.setUploadPath(datePath);
					if (isFileType(file)) {
						fileDto.setFileType(true);
						try (InputStream in = new FileInputStream(file);
								OutputStream out = new FileOutputStream(new File(uploadPath, "t_" + fileName));) {
							Thumbnailator.createThumbnail(in, out, 100, 100);
						}
					}
					files.add(fileDto);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					System.out.println("fileController upload error fileName = " + originalFileName);
				}
			}
		}
		return new ResponseEntity<List<FileDto>>(files, HttpStatus.OK);
	}

	@GetMapping("/{boardNum}")
	public ResponseEntity<List<FileDto>> getFiles(@PathVariable Long boardNum) {
		List<FileDto> files = fileService.findByBoardNum(boardNum);
		return new ResponseEntity<List<FileDto>>(files, HttpStatus.OK);
	}

	@GetMapping("/display")
	public ResponseEntity<byte[]> display(String fileName) throws UnsupportedEncodingException {
		try {
			File file = new File(DEFAULT_DIRECTORY_PATH, fileName);
			if (!file.exists()) {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
			String contentType = Files.probeContentType(file.toPath());
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", contentType);
			byte[] result = FileCopyUtils.copyToByteArray(file);
			return new ResponseEntity<byte[]>(result, header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> download(String fileName) {
		System.out.println("fileName");
		Resource resource = new FileSystemResource(new File(DEFAULT_DIRECTORY_PATH, fileName));
		if (!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		String resourceName = resource.getFilename();
		resourceName = resourceName.substring(resourceName.indexOf("_") + 1);
		System.out.println(resourceName);
		HttpHeaders header = new HttpHeaders();
		try {
			header.add("Content-Disposition",
					"attachment;filename=" + new String(resourceName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{boardNum}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> delete(@RequestBody FileDto fileDto, @PathVariable Long boardNum) {
		System.out.println("들어옴");
		String fileName = fileDto.getUploadPath() + "/t_" + fileDto.getUuid() + "_" + fileDto.getFileName();
		File file = new File(DEFAULT_DIRECTORY_PATH, fileName);
		if (file.exists()) {
			file.delete();
		}
		if (fileDto.isFileType()) {
			fileName = fileName.replace("t_", "");
			file = new File(DEFAULT_DIRECTORY_PATH, fileName);
			if (file.exists()) {
				file.delete();
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private String getDatePath() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	}

	private boolean isFileType(File file) throws IOException {
		String contentType = Files.probeContentType(file.toPath());
		return contentType.startsWith("image");
	}
	

	@PostMapping("/test")
	public void test(@RequestBody FileDto FileDto) {
		System.out.println("테스트 들어옴");
	}
	

}
