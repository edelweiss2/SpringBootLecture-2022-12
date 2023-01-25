package com.mulcam.demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mulcam.demo.entity.StaticMap;
import com.mulcam.demo.service.CsvUtil2;
import com.mulcam.demo.service.MapUtil;

@Controller
@RequestMapping("/map")
public class MapController {
	
	@Value("${naver.accessId}")
	private String accessId;
	
	@Value("${naver.secretKey}")
	private String secretKey;
	
	@Value("${roadAddrKey}")
	private String roadAddrKey;
	
	@GetMapping("/kakaoMap")
	public String kakaoForm() {
		return "map/kakaoMap";
	}
	
	@GetMapping("/naverMap")
	public String naverForm() {
		return "map/naverMap";
	}
	
	@PostMapping("/staticMap")
	public String staticMap(StaticMap map, Model model) throws Exception {
		String url = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster"
				   + "?w=" + map.getWidth()
				   + "&h=" + map.getHeight()
				   + "&center=" + map.getLng() + "," + map.getLat()
				   + "&level=" + map.getLevel()
				   + "&maptype=" + map.getMaptype()
				   + "&format=" + map.getFormat()
				   + "&scale=" + map.getScale()
				   + "&lang=" + map.getLang()
				   + "&X-NCP-APIGW-API-KEY-ID=" + accessId
				   + "&X-NCP-APIGW-API-KEY=" + secretKey;
//		markers=type:d|size:tiny|pos:127.1054221%2037.3591614
		String marker = "type:d|size:mid|pos:127.0724 37.5383"; 
		marker = URLEncoder.encode(marker, "utf-8");
		url += "&markers=" + marker;
		
		marker = "type:t|size:tiny|pos:127.0824 37.5383|label:광진구청|color:red"; 
		marker = URLEncoder.encode(marker, "utf-8");
		url += "&markers=" + marker;
		
		model.addAttribute("url", url);
		return "map/staticResult";
	}
	
	@ResponseBody
	@GetMapping("/roadAddr/{keyword}")
	public String roadAddr(@PathVariable String keyword) throws Exception {
		int currentPage = 1;
		int countPerPage = 10;
		String resultType = "json";
		keyword = URLEncoder.encode(keyword, "utf-8");
		String apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do"
				   + "?confmKey=" + roadAddrKey
				   + "&currentPage=" + currentPage
				   + "&countPerPage=" + countPerPage
				   + "&keyword=" + keyword
				   + "&resultType=" + resultType;
		
		URL url = new URL(apiUrl);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
		StringBuffer sb = new StringBuffer();
		String line = null;
		
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		
		// JSON 데이터에서 원하는값 추출하기
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(sb.toString());
		JSONObject results = (JSONObject) object.get("results");
		JSONArray juso = (JSONArray) results.get("juso");
		JSONObject jusoItem = (JSONObject) juso.get(0);
		String roadAddr = (String) jusoItem.get("roadAddr");
		
		return sb.toString() + "<br>" + roadAddr;
	}
	
	@ResponseBody
	@GetMapping("/geocode")
	public String geocode() throws Exception {
		String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
		String query = "";
		query = URLEncoder.encode(query, "utf-8");
		apiUrl += "?query=" + query;
		
		URL url = new URL(apiUrl);
		// 헤더 설정
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", accessId);
		conn.setRequestProperty("X-NCP-APIGW-API-KEY", secretKey);
		conn.setDoInput(true);
		
		// 응답 결과 확인
		int responseCode = conn.getResponseCode();
		
		// 데이터 수신
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer();
		String line = null;
		
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(sb.toString());
		JSONArray addresses = (JSONArray) object.get("addresses");
		JSONObject address = (JSONObject) addresses.get(0);
		
		String lng_ = (String) address.get("x");
		String lat_ = (String) address.get("y");
		Double lng = Double.parseDouble(lng_);
		Double lat = Double.parseDouble(lat_);
		
		return "경도: " + lng_ + ", 위도: " + lat_;
	}
		
	@GetMapping("/hotPlaces")
	public String hotPlaces() throws Exception {
		String[] hotPlaces = {"대구오페라하우스","대구사격장","어울아트센터","대구엑스코"};
		String filename = "c:/Temp/지역명소.csv";
		MapUtil mu = new MapUtil();
		
		String output = "";
		List<List<String>> dataList = new ArrayList<>();
		for (String place: hotPlaces) {
			List<String> row = new ArrayList<>();
			String roadAddr = mu.getRoadAddr(place, roadAddrKey);
			output += roadAddr + "<br>";
			List<String> geocode = mu.getGeocode(roadAddr, accessId, secretKey);
			row.add(place);
			row.add(roadAddr);
			row.add(geocode.get(0));		// Longitude(경도)
			row.add(geocode.get(1)); 		// Latitude(위도)
			dataList.add(row);
		}
		
		CsvUtil2 cu = new CsvUtil2();
		cu.writeCsv(filename, dataList);
		return "redirect:/map/hotPlacesResult";
	}
	
	@GetMapping("/hotPlacesResult")
	public String hotPlacesResult(Model model) throws Exception {
		CsvUtil2 cu = new CsvUtil2();
		List<List<String>> dataList = cu.readCsv("c:/Temp/지역명소.csv");
		String marker = "";
		int i = 0;
		String[] color = {"blue","brown","green","purple"};
		double lngSum = 0.0, latSum = 0.0;
		// "type:t|size:tiny|pos:127.0824 37.5383|label:광진구청|color:red"
		for (List<String> list : dataList)  {
			double lng = Double.parseDouble(list.get(2));
			double lat = Double.parseDouble(list.get(3));
			lngSum += lng; latSum += lat;
			marker += "&markers=type:t|size:tiny|pos:" + lng + "%20" + lat + "|label:"
					+ URLEncoder.encode(list.get(0), "utf-8")+ "|color:" + color[i];
			i++;
		}
		
		double lngCenter = lngSum / dataList.size();
		double latCenter = latSum / dataList.size();
		String url = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster"
				+ "?w=" + 600 + "&h=" + 400
				+ "&center=" + lngCenter + "," + latCenter
				+ "&level=" + 11 + "&scale=" + 2
				+ "&X-NCP-APIGW-API-KEY-ID=" + accessId
				+ "&X-NCP-APIGW-API-KEY=" + secretKey;
		
		model.addAttribute("url", url+marker);
		return "map/staticResult";
	}
	
	@ResponseBody
	@GetMapping("/mindPlaces")
	public String mindPlaces() throws Exception {
		String apiUrl = "https://api.odcloud.kr/api/3049990/v1/uddi:14a6ea21-af95-4440-bb05-81698f7a1987?page=1&perPage=10";
		String query = "";
		String apiKey = "data-portal-test-key";
		query = URLEncoder.encode(query, "utf-8");
		apiUrl += "?query=" + query;
		
		URL url = new URL(apiUrl);
		// 헤더 설정
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", accessId);
		conn.setRequestProperty("X-NCP-APIGW-API-KEY", secretKey);
		conn.setDoInput(true);
		
		// 응답 결과 확인
		int responseCode = conn.getResponseCode();
		
		// 데이터 수신
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer();
		String line = null;
		
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();		
		
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(sb.toString());
		JSONArray addresses = (JSONArray) object.get("addresses");
		JSONObject address = (JSONObject) addresses.get(0);
		
		String lng_ = (String) address.get("x");
		String lat_ = (String) address.get("y");
		Double lng = Double.parseDouble(lng_);
		Double lat = Double.parseDouble(lat_);
		
		return "경도: " + lng_ + ", 위도: " + lat_;
		
	}
	
}
