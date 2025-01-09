package com.example.spring61.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class BoardDto {
	private Long boardNum;
	private String boardTitle;
	private String boardContent;
	private String boardRegisterDate;
	private Long boardHit;
	private Long userNum;
	private String boardUpdateDate;
	private String boardCategory;
	private String userId;
	private List<FileDto> files;
}


