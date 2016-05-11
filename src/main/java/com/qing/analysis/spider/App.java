package com.qing.analysis.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.qing.analysis.spider.entity.Request;
import com.qing.analysis.spider.entity.Response;
import com.qing.analysis.spider.entity.Url;
import com.qing.analysis.spider.http.HttpService;
import com.qing.analysis.spider.http.OnResponseListener;
import com.qing.analysis.spider.http.Type;
import com.qing.analysis.spider.parse.Parser;
import com.qing.analysis.spider.parse.ParserAdapter;
import com.qing.utils.FileUtil;
import com.qing.utils.ThreadUtil;

/**
 * Hello world!
 *
 */
public class App implements OnResponseListener {
	private static Queue<Request> requestQueue;
	
	private HttpService httpService;
	private int threadCount = 10;
	
	// 当前正在执行的线程数
	private AtomicInteger countOfRunningThread = new AtomicInteger(0);
	
	// 解析文件的集合
	private List<Parser> parserList = new ArrayList<>();
	
	public App() {
		requestQueue = new ConcurrentLinkedQueue<>();
//		requestQueue = new LinkedBlockingQueue<>(1000);
		httpService = new HttpService();
		httpService.setOnResponseListener(this);
		httpService.setThreadCount(threadCount);
	}
	
	
	static long mid = 1727263;
	static List<String> lines = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		App main = new App();
		main.addParser(new ParserAdapter() {
			@Override
			public void parse(String str) throws Exception {
				try {
					lines.add(str);
					if(lines.size() > 10) {
						FileUtil.appendToFile(lines, "//mnt", "data", "bili", "upinfo1.txt");
						lines.clear();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
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
	
	void run() throws Exception {
		while(true) {
			if(countOfRunningThread.get() > threadCount) {
				ThreadUtil.sleep(100);
				continue;
			}
			
			Request request = requestQueue.poll();
			if(request == null) {
				for(int i=0; i<100; i++) {
					mid ++;
					Url url = new Url();
					url.setUrl("http://space.bilibili.com/ajax/member/GetInfo?mid="+mid);
					addUrl(url);
				}
				request = requestQueue.poll();
			}
			httpService.doRequest(request);
			countOfRunningThread.incrementAndGet();
			
		}
	}

	@Override
	public void onResponse(Response response, Type type) throws Exception {
		countOfRunningThread.decrementAndGet();
		for(Parser parser : parserList) {
			parser.parse(response.getData());
		}
	}
	
	/**
	 * 添加要请求的url
	 * @param urlList
	 */
	public static void addUrl(Url url) {
		Request request = new Request();
		request.setUrl(url);
		requestQueue.add(request);
	}
	
	/**
	 * 添加要请求的url list
	 * @param urlList
	 */
	public static void addUrl(List<Url> urlList) {
		for(Url url : urlList) {
			Request request = new Request();
			request.setUrl(url);
			requestQueue.add(request);
		}
	}
	
	/**
	 * 添加解析器
	 * @param parser
	 */
	public void addParser(Parser parser) {
		parserList.add(parser);
	}
}
