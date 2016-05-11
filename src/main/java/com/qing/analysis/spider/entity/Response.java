package com.qing.analysis.spider.entity;

import java.io.File;

public class Response {

	private Url url;
	private String data;
	private File dataTemp;

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public File getDataTemp() {
		return dataTemp;
	}

	public void setDataTemp(File dataTemp) {
		this.dataTemp = dataTemp;
	}

}
