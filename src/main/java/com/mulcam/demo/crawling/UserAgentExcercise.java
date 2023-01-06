package com.mulcam.demo.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class UserAgentExcercise {

	public static void main(String[] args) throws Exception{
		String url = "https://www.naver.com/";
		Document doc = Jsoup.connect(url).get();
		
		// 크롤링 막는 사이트에서 기계가 아닌 사람이라고 알려주는 방법 userAgent 이용
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
		
	
	}

}
