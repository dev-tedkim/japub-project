package com.app.japub.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class CommentsDto {
	private List<CommentDto> comments;
	private int nextCountPage;
}
