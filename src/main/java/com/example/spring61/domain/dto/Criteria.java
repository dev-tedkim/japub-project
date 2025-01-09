package com.example.spring61.domain.dto;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class Criteria {
	private int page, amount, pageRange;
	private String type, keyword, category;

	public Criteria() {
		this(1, 10, 5);
	}

	public Criteria(int page, int amount, int pageRange) {
		this.page = page;
		this.amount = amount;
		this.pageRange = pageRange;
	}

	public int getStartRow() {
		return getEndRow() - (amount - 1);
	}

	public int getEndRow() {
		return page * amount;
	}

	public String[] getTypes() {
		return type == null ? new String[] {} : type.split("");
	}

	public String getParams() {
		return UriComponentsBuilder.newInstance().queryParam("page", page).queryParam("type", type)
				.queryParam("keyword", keyword).queryParam("category", category).toUriString();
	}

}
