package com.mulcam.demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

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

@Controller
@RequestMapping("/map")
public class MapController {
	
	@Value("${naver.accessId}")
	private String accessId;
	
	@Value("${naver.secretKey}")
	private String secretKey;
	
	@Value("${roadAddrKey}")
	private String roadAddrKey;
	
	
	@GetMapping("/staticMap")
	public String staticForm() {
		
		return "map/staticForm";
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
}
