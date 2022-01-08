package com.weaver.rpa.tender.search.util;

import java.io.Serializable;

/**
 * 分页工具
 *
 * @author pengyonglei
 * @version 1.0.0
 */
public class Pagination implements Serializable{

	private static final long serialVersionUID = -3797791064278676394L;
	/**
	 * 页数
	 */
	private int pageNum;

	/**
	 * 每页大小
	 */
	private int pageSize;

	/**
	 * 总数
	 */
	private long totalCount;

	/**
	 * 总页数
	 */
	private int totalPages;

	public Pagination(int pageNum, int pageSize) {
		this.pageNum = pageNum <= 0 ? 1 : pageNum;
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	public Pagination() {
		this.pageNum = 1;
		this.pageSize = 10;
	}

	public Pagination(int pageNum, int pageSize, int totalCount) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		calcTotalPages();
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		// 计算总页数
		calcTotalPages();
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	private void calcTotalPages() {
		long pages = 0;
		if (this.totalCount > 0) {
			pages = this.totalCount / this.pageSize;
			if (totalCount % pageSize != 0) {
				pages++;
			}
		}
		this.totalPages = (int) pages;
	}

	@Override
	public String toString() {
		return "Pagination: {" +
				"pageNum=" + pageNum +
				", pageSize=" + pageSize +
				", totalCount=" + totalCount +
				", totalPages=" + totalPages +
				'}';
	}

	public static void main(String[] args) {
		Pagination pagination = new Pagination(1, 10, 2);
		System.out.println(pagination.getTotalPages());
	}

}
