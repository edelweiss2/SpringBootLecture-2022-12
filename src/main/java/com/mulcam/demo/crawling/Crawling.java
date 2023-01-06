package com.mulcam.demo.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawling {
	
	public List<Interpark> interpark() throws Exception {
		String url = "http://book.interpark.com/display/collectlist.do?_method=BestsellerHourNew201605&bestTp=1&dispNo=028#";
		Document doc = Jsoup.connect(url).get();
		Elements lis = doc.select(".rankBestContentList > ol > li");
		
		List<Interpark> list = new ArrayList<>();
		for (Element li : lis) {
			Elements spans = li.select(".rankNumber.digit2").select("span");
			String rank_ = "";
			for (Element span : spans) {
				String classes = span.attr("class").strip();  // 맨뒤에 항상 스페이스가 남는데 잘라줘야함. 클래스 가운데 공백은 구분자로 쓰임  
				rank_ += classes.substring(classes.length() - 1);
			}
			int rank = Integer.parseInt(rank_);
			String src = li.select(".coverImage img").attr("src");
			String title = li.select(".itemName").text().strip();
			String company = li.select(".company").text().strip();
			String author = li.select(".author").text().strip();
			String price_ = li.select(".price > em").text().strip();
			int price = Integer.parseInt(price_.replace(",",""));
			Interpark book = new Interpark(rank, src, title, author, company, price);
			list.add(book);
		}
		
		return list;
	}
}
