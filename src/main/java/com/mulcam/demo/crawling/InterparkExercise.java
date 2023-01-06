package com.mulcam.demo.crawling;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InterparkExercise {

	public static void main(String[] args) throws Exception {
		// 크롤링 할 url
		String url = "http://book.interpark.com/display/collectlist.do?_method=BestsellerHourNew201605&bestTp=1&dispNo=028#";
		
		// site에 접속해서 html 데이터를 가져온 후 parsing
		Document doc = Jsoup.connect(url).get();
		
		// 찾고자 하는 항목들
		Elements lis = doc.select(".rankBestContentList > ol > li");
//		System.out.println(lis.size());
		
/**
 		// 찾는 값 구하기
		Element li = lis.get(12);
		
		String title = li.select(".itemName").text().strip();
		String company = li.select(".company").text().strip();
		String author = li.select(".author").text().strip();
		
		
		// 이미지 주소 알아내기
//		Element img = li.selectFirst(".coverImage").selectFirst("img");
//		String src = img.attr("src");
		
		String src = li.select(".coverImage img").attr("src");		//자손 셀렉터 
		// 스트링 조작 
		String price_ = li.select(".price > em").text().strip();
		int price = Integer.parseInt(price_.replace(",",""));
		System.out.println(price);
		
		// 순위
		Elements spans = li.select(".rankNumber.digit2").select("span");
		String rank_ = "";
		for (Element span : spans) {
			String classes = span.attr("class").strip();  // 맨뒤에 항상 스페이스가 남는데 잘라줘야함. 클래스 가운데 공백은 구분자로 쓰임  
			System.out.println(classes);
			rank_ += classes.substring(classes.length() - 1);
		}
		int rank = Integer.parseInt(rank_);
		System.out.println(rank);
		
		// 데이터 정리
		Interpark book = new Interpark(rank, src, title, author, company, price);
		System.out.println(book);
*/
		
		// 반복문으로 데이터 정리
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
		list.forEach(x->System.out.println(x));
	}

}
