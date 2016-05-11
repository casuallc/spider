package com.qing.analysis.spider.entity;

public class Url {

	private String url;
	private int depth;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void depthAdd(int n) {
		depth += n;
		if(n == -1)
			depth = 0;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
}
