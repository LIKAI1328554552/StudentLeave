package com.likai.activiti5.utils;

import java.util.List;

/**
 * 返回给前台带分页信息的数据列表
 * @author likai
 * @since 2018-03-19
 */
public class PageList {
	
	private long total ;
	private List<Object> rows ;
	
	public PageList() {
		super() ;
	}

	public PageList(long total, List<Object> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<Object> getRows() {
		return rows;
	}

	public void setRows(List<Object> rows) {
		this.rows = rows;
	}
	
}
