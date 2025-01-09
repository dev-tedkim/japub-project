package com.example.spring61.domain.dto;

import lombok.Data;

@Data
public class CommentDto {
	private Long cno;
	private Long pcno;
	private String commentContent;
	private String commentRegisterDate;
	private Long userNum;
	private Long boardNum;
	private String pid;
	private String userId;
	private String commentUpdateDate;
}
