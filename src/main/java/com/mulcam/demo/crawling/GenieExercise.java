package com.mulcam.demo.crawling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GenieExercise {

	public static void main(String[] args) throws Exception {
		/** 
		String url = "https://www.genie.co.kr/chart/top200";
		Document doc = Jsoup.connect(url).get();

		Elements trs = doc.select("tr.list");
		Element tr = trs.get(0);
		
		String rank_ = tr.select(".number").text().strip().split(" ")[0];
		int rank = Integer.parseInt(rank_);
		
								// cover클래스 이하 img 태그의 src값
		String src = tr.select(".cover > img").attr("src");			// 앞에 https: 추가해줘야됨
		String title = tr.select(".title.ellipsis").text().strip();
		String artist = tr.select(".artist.ellipsis").text().strip();
		String album = tr.select(".albumtitle.ellipsis").text().strip();
		System.out.println(title + ", " + artist + ", " + album);
		
		*/
		
		// 2023-01-06T13:28:30.465271
		LocalDateTime now = LocalDateTime.now();
		String ymd = now.toString().substring(0,10).replace("-","");
		String hh = now.toString().substring(11,13);
		
		// https://www.genie.co.kr/chart/top200?ditc=D&ymd=20230106&hh=13&rtm=Y&pg=1
		List<Genie> list = new ArrayList<>();
		// genie 랭킹 200위의 페이지에 대한 loop
		for (int i = 1; i <= 4; i++) {
			String url = "https://www.genie.co.kr/chart/top200?ditc=D&ymd=" + ymd
					   + "&hh=" + hh
					   + "&rtm=Y&pg=" + i;
			Document doc = Jsoup.connect(url).get();

			Elements trs = doc.select("tr.list");	// 50개씩 추출됨
			
			for (Element tr : trs) {
				String rank_ = tr.select(".number").text().strip().split(" ")[0];
				int rank = Integer.parseInt(rank_);
				
				String src = "https:" + tr.select(".cover > img").attr("src");	
				String title = tr.select(".title.ellipsis").text().strip();
				String artist = tr.select(".artist.ellipsis").text().strip();
				String album = tr.select(".albumtitle.ellipsis").text().strip();
				Genie genie = new Genie(rank, src, title, artist, album);
				list.add(genie);
			}
		}
	}
}
