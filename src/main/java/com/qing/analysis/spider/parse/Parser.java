package com.qing.analysis.spider.parse;

import java.io.File;

public interface Parser {

	public void parse(String data) throws Exception;
	
	public void parse(File dataFile) throws Exception;
}
