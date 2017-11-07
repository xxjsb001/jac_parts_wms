package com.vtradex.wms.server.utils;

public class SortRule {
	private int sortIndex;
	private String sortKey;
	private String sortMethod;
	
	public SortRule(int sortIndex, String sortKey, String sortMethod) {
		super();
		this.sortIndex = sortIndex;
		this.sortKey = sortKey;
		this.sortMethod = sortMethod;
	}
	public int getSortIndex() {
		return sortIndex;
	}
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public String getSortMethod() {
		return sortMethod;
	}
	public void setSortMethod(String sortMethod) {
		this.sortMethod = sortMethod;
	}
}
