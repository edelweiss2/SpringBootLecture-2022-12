package com.mulcam.demo.crawling;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumExcercise {
	// driver
	private static WebDriver driver;
	// properties
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; 
	private static final String WEB_DRIVER_PATH = "C:/DevTools/chromedriver/chromedriver.exe";
		
	public static void main(String[] args) throws Exception {
		// driver setup
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		driver = new ChromeDriver();
		
		String url = "https://www.nfa.go.kr/nfa/introduce/status/firestationidfo";
		driver.get(url);
		Thread.sleep(2000);		//	2초 지연
		
		WebElement inputBox = driver.findElement(By.cssSelector("#searchKeyword"));
		inputBox.sendKeys("서울");
		WebElement searchBtn = driver.findElement(By.cssSelector("#fsSearchBtn"));
		searchBtn.click();
		Thread.sleep(1000);
		
		String xpath = "//*[@id=\"listForm\"]/div/section/div/p/strong[2]";
		String num_ = driver.findElement(By.xpath(xpath)).getText().strip();		// 100건
		int num = Integer.parseInt(num_.substring(0, num_.length()-1));				// 100
		int pages = (int) Math.ceil(num / 10.);
		System.out.println(pages);
		
//		// 페이지 이동 - 셀레니움, 페이지 내에서 작업 - > jsoup
//		Document doc = Jsoup.parse(driver.getPageSource());	// html 소스 가져오는 getpagesource
//		Elements lis = doc.select(".stations-list > li");
//		Element li = lis.get(0);
//		String name = li.select(".title").text().strip();
//		String addr = li.select("address").text().strip();			// 클래스가 아닌 태그라서 .이 없음
//		String tel = li.select(".tel").text().strip();
//		
//		FireStation fs = new FireStation(name, addr, tel);
		
		// 2페이지로 이동
		/**

		String page2 = "//*[@id=\"listForm\"]/div/section/ul/li[1]/div/div/ul/li[4]/a";
		driver.findElement(By.xpath(page2)).click();
		Thread.sleep(1000);
		
		Document doc = Jsoup.parse(driver.getPageSource());	// html 소스 가져오는 getpagesource
		Elements lis = doc.select(".stations-list > li");
		Element li = lis.get(0);
		String name = li.select(".title").text().strip();
		String addr = li.select("address").text().strip();			// 클래스가 아닌 태그라서 .이 없음
		String tel = li.select(".tel").text().strip();
		
		FireStation fs = new FireStation(name, addr, tel);
		System.out.println(fs);
		
		// 다음페이지로 이동
		driver.findElement(By.cssSelector(".next_page")).click();
		Thread.sleep(1000);
		*/
		
		// 한꺼번에 모든 소방서 페이지를 가져오는 코드
		List<FireStation> list = new ArrayList<>();
		for (int page=1; page <= pages; page++) {
			if (page > 1 && page % 2 == 0) {
				driver.findElement(By.xpath("//*[@id=\"listForm\"]/div/section/ul/li[1]/div/div/ul/li[4]/a")).click();
				Thread.sleep(1000);
			}
			if (page > 1 && page % 2 == 1) {
				driver.findElement(By.cssSelector(".next_page")).click();
				Thread.sleep(1000);
			}
			Document doc = Jsoup.parse(driver.getPageSource());	// html 소스 가져오는 getpagesource
			Elements lis = doc.select(".stations-list > li");
			for (Element li:lis) {
				String name = li.select(".title").text().strip();
				String addr = li.select("address").text().strip();			
				String tel = li.select(".tel").text().strip();
				FireStation fs = new FireStation(name, addr, tel);
				list.add(fs);
			}
		}
		System.out.println(list.size());
	}

}
