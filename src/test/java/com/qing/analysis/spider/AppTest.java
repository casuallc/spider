package com.qing.analysis.spider;

import java.util.ArrayList;
import java.util.List;

import com.qing.analysis.spider.entity.Url;
import com.qing.analysis.spider.parse.ParserAdapter;
import com.qing.utils.FileUtil;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	long mid = 1;
	public void testRun() throws Exception {
		App main = new App();
		main.addParser(new ParserAdapter() {
			@Override
			public void parse(String str) throws Exception {
				mid ++;
				Url url = new Url();
				url.setUrl("http://space.bilibili.com/ajax/member/GetInfo?mid="+mid);
				App.addUrl(url);
			}
		});
		Url url = new Url();
		//http://space.bilibili.com/ajax/member/GetInfo?mid=10002057
		//http://space.bilibili.com/ajax/member/getTags?mids=10002057
		//http://space.bilibili.com/ajax/topphoto/getlist
		//http://space.bilibili.com/ajax/top/showTop?mid=10002057
//		url.setUrl("http://space.bilibili.com/10002057");
		url.setUrl("http://space.bilibili.com/ajax/member/GetInfo?mid="+mid);
		App.addUrl(url);
		
		main.run();
	}
	
	public void test2() {
		System.out.println(new String("\u690e\u540d\u82b1\u5948"));
	}
	
	public void testFileUtil() throws Exception {
		List<String> lines = new ArrayList<>();
		lines.add("1");
		FileUtil.appendToFile(lines, "G:", "test", "");
	}
}
