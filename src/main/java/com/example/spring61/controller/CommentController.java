package com.example.spring61.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring61.domain.dto.CommentDto;
import com.example.spring61.domain.dto.Criteria;
import com.example.spring61.domain.service.comment.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
	@Autowired
	private CommentService commentService;
	private final ResponseEntity<String> SUCCESS_CODE = new ResponseEntity<String>(HttpStatus.OK);
	private final ResponseEntity<String> ERROR_CODE = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

	@GetMapping(value = "/{boardNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long boardNum, Criteria criteria) {
		List<CommentDto> comments = commentService.findByBoardNumAndCriteria(boardNum, criteria);
		if (comments == null) {
			return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<CommentDto>>(comments, HttpStatus.OK);
	}

	@PostMapping(value = "/{boardNum}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> insert(@PathVariable Long boardNum, @RequestBody CommentDto commentDto,
			HttpSession session) {
		Long userNum = (Long) session.getAttribute("userNum");
		commentDto.setUserNum(userNum);
		commentDto.setBoardNum(boardNum);
		if (!commentService.insert(commentDto)) {
			return ERROR_CODE;
		}
		return SUCCESS_CODE;
	}

	@PatchMapping(value = "/{cno}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> update(@RequestBody CommentDto commentDto, @PathVariable Long cno) {
		commentDto.setCno(cno);
		if (!commentService.update(commentDto)) {
			return ERROR_CODE;
		}
		return SUCCESS_CODE;
	}

	@DeleteMapping(value = "/{cno}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> delete(@PathVariable Long cno, HttpSession session) {
		Long userNum = (Long) session.getAttribute("userNum");
		if (!commentService.delete(cno, userNum)) {
			return ERROR_CODE;
		}
		return SUCCESS_CODE;
	}
}
