package com.qing.analysis.spider.http;

import java.net.URI;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.qing.analysis.spider.entity.Request;
import com.qing.analysis.spider.entity.Response;
import com.qing.analysis.spider.entity.Url;
import com.qing.utils.FileUtil;

public class HttpService {

	private OnResponseListener onResponseListener;
	private CloseableHttpClient httpClient;

	private Executor executor;
	private int threadCount = 1;

	private static final String USER_AGENT[] = {
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36",

			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
			"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E) ",
			"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16", };
	private static Random random = new Random(System.currentTimeMillis());

	public void setOnResponseListener(OnResponseListener onResponseListener) {
		this.onResponseListener = onResponseListener;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public HttpService() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		CookieStore cookieStore = new AcCookieStore();
		builder.setDefaultCookieStore(cookieStore);

		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return false;
				}
			}).loadKeyMaterial(KeyStore.getInstance("RSA"), new char[] { 1 }).build();
			builder.setSSLContext(sslContext);
			// builder.setSSLSocketFactory(fac);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpClient = builder.build();
		// httpClient = HttpClients.createDefault();
		//

		executor = Executors.newFixedThreadPool(threadCount);
	}

	public void setHeader(HttpRequestBase request) {
		// request.setHeader("Host", "space.bilibili.com");
		request.setHeader("User-Agent", USER_AGENT[random.nextInt(USER_AGENT.length)]);
		request.setHeader("Accept", "*/*");
		request.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		request.setHeader("Accept-Encoding", "gzip;deflate");
		request.setHeader("Cache-Control", "no-cache");
		request.setHeader("Program", "no-cache");
	}

	public void doRequest(final Request request) {
		executor.execute(() -> {
			try {
				HttpGet get = new HttpGet();
				get.setURI(URI.create(request.getUrl().getUrl()));
				setHeader(get);
				HttpResponse response = httpClient.execute(get);

				request.getUrl().depthAdd(1);
				Response r = new Response();
				r.setData(EntityUtils.toString(response.getEntity()));
				r.setUrl(request.getUrl());

				onResponseListener.onResponse(r, Type.HTML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void doRequest(final Request request, Cookie cookies[]) {
		executor.execute(() -> {
			try {
				HttpGet get = new HttpGet();
				get.setURI(URI.create(request.getUrl().getUrl()));
				setHeader(get);
				HttpResponse response = httpClient.execute(get);

				request.getUrl().depthAdd(1);
				Response r = new Response();
				r.setData(EntityUtils.toString(response.getEntity()));
				r.setUrl(request.getUrl());

				onResponseListener.onResponse(r, Type.HTML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void doRequest(String url) {
		Url u = new Url();
		u.setUrl(url);
		Request r = new Request();
		r.setUrl(u);
		doRequest(r);
	}

	public HttpService setCookie(String name, String value) {

		return this;
	}

	class AcCookieStore implements CookieStore {
		private List<Cookie> cookieList = new ArrayList<Cookie>();

		public AcCookieStore() {
			try {
				List<String> list = FileUtil
						.inputStreamToStringList(getClass().getClassLoader().getResourceAsStream("temp.txt"));
				for (String s : list) {
					String ss[] = s.split(":");
					BasicClientCookie e = new BasicClientCookie(ss[0], ss[1].replaceAll("\"", ""));
					e.setDomain("/");
					// cookieList.add(e);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		@Override
		public void addCookie(Cookie cookie) {
			cookieList.add(cookie);
		}

		@Override
		public List<Cookie> getCookies() {
			return cookieList;
		}

		@Override
		public boolean clearExpired(Date date) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			cookieList.clear();
		}
	}

}
