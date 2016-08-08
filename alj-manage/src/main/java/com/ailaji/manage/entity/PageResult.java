package com.ailaji.manage.entity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class PageResult<T> {

	private Integer total;
	private Integer page_count;
	private Integer page_size;
	private Integer page_no;
	private List<T> page_list;

	/**
	 * @param page_list page页list
	 * @param page_no
	 * @param page_size
	 * @param total
	 */
	public PageResult(List<T> page_list, int page_no, int page_size,
			int total) {
		this.total = total;
		this.page_size = page_size;
		this.page_no = page_no;
		this.page_list = page_list;

		page_count = (int) Math.ceil((double)total/page_size);
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getPage_count() {
		return page_count;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public Integer getPage_no() {
		return page_no;
	}

	public List<T> getPage_list() {
		return page_list;
	}
}

