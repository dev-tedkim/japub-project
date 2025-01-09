package com.example.spring61.domain.dto;

import lombok.Data;

@Data
public class PageDto {
	private Criteria criteria;
	private int startPage, endPage, realEndPage, total;
	private boolean prev, next;

	public PageDto(Criteria criteria, int total) {
		this.criteria = criteria;
		this.total = total;
		endPage = (int)Math.ceil(criteria.getPage() * 1.0 / criteria.getPageRange()) * criteria.getPageRange();
		realEndPage = (int)Math.ceil(total * 1.0 / criteria.getAmount());
		startPage = endPage - (criteria.getPageRange() - 1);
		prev = startPage > 1;
		next = endPage < realEndPage;
		endPage = endPage  > realEndPage ? realEndPage : endPage;
	}

}
