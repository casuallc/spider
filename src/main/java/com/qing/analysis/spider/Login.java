package com.qing.analysis.spider;

import java.security.KeyStore;

import com.qing.analysis.spider.entity.Response;
import com.qing.analysis.spider.http.HttpService;
import com.qing.analysis.spider.http.OnResponseListener;
import com.qing.analysis.spider.http.Type;

public class Login {

	public static void main(String[] args) {
		HttpService service = new HttpService();
		service.setOnResponseListener(new OnResponseListener() {
			
			@Override
			public void onResponse(Response reponse, Type type) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(reponse.getData());
			}
		});
		service.doRequest("https://www.bilibili.com");
	}
}
