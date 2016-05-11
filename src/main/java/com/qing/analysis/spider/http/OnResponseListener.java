package com.qing.analysis.spider.http;

import com.qing.analysis.spider.entity.Response;

public interface OnResponseListener {

	public void onResponse(Response reponse, Type type) throws Exception;
}
