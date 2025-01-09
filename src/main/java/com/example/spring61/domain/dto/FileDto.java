package com.example.spring61.domain.dto;

import lombok.Data;

@Data
public class FileDto {
	private String uuid;
	private String uploadPath;
	private boolean fileType;
	private String fileName;
	private Long boardNum;
}


