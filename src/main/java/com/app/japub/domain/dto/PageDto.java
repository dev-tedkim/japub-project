package com.app.japub.domain.dto;

import lombok.Data;

@Data
public class PageDto {
	private Criteria criteria;
	private int startPage, endPage, realEndPage;
	private Long total;
	private boolean prev, next;

	public PageDto(Criteria criteria, Long total) {
		this.criteria = criteria;
		this.total = total;
		endPage = (int) Math.ceil(criteria.getPage() * 1.0 / criteria.getPageRange()) * criteria.getPageRange();
		startPage = endPage - (criteria.getPageRange() - 1);
		realEndPage = (int) Math.ceil(total * 1.0 / criteria.getAmount());
		prev = startPage > 1;
		next = endPage < realEndPage;
		endPage = endPage > realEndPage ? realEndPage : endPage;
	}

}
