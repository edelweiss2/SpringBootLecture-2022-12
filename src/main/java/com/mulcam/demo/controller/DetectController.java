package com.mulcam.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/detect")
public class DetectController {
	
	@Value("${naver.accessId}")
	private String accessId;
	
	@Value("${naver.secretKey}")
	private String secretKey;
	
	@GetMapping("/naver")
	public String naver(Model model) throws Exception {
		String apiURL = "https://naveropenapi.apigw.ntruss.com/vision-obj/v1/detect";
		File uploadFile = new File("c:/Temp/yolo-test2.jpg");
		
		URL url = new URL(apiURL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setUseCaches(false);
        
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");			//post로보냄 (생략해도 무방)
		
//        multipart request
        String boundary = "---" + System.currentTimeMillis() + "---";
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", accessId);
        conn.setRequestProperty("X-NCP-APIGW-API-KEY", secretKey);
        
        // 파일전송 준비
        OutputStream os = conn.getOutputStream();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);
        String LF = "\n";		//해도되고 안해도됨 (Line Feed)
        String fileName = uploadFile.getName();
        out.append("--" + boundary).append(LF);
        out.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + fileName + "\"").append(LF);
        out.append("Content-Type: "  + URLConnection.guessContentTypeFromName(fileName)).append(LF);
        out.append(LF);
        out.flush();	//파일전송 준비끝
        
        // 파일을 읽어서 전송
        FileInputStream fis = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096]; //4kb
        int bytesRead = -1;		// 파일 읽는데 -1이 나오면 다읽었다는 뜻
        while ((bytesRead = fis.read(buffer)) != -1 ) {
        	os.write(buffer,0,bytesRead);
        }
        os.flush();
        fis.close();
        out.append(LF).flush();
        out.append("--" + boundary + "--").append(LF);
        out.close();
        
        // 응답 결과 확인
        int responseCode = conn.getResponseCode();
        // 결과 확인
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
        	sb.append(line);
        }
        br.close();
        
        model.addAttribute("fileName", fileName);
        model.addAttribute("jsonResult", sb.toString());
		return "detect/naverResult";
	}
}
