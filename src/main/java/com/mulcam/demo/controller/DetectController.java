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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mulcam.demo.entity.FileEntity;


@Controller
@RequestMapping("/detect")
public class DetectController {
	
	@Value("${naver.accessId}")
	private String accessId;
	
	@Value("${naver.secretKey}")
	private String secretKey;
	
	@Value("${spring.servlet.multipart.location}")
	String uploadDir;
	
	@GetMapping("/naver")
	public String naverForm() {
		return "detect/naverForm";
	}	
		
	@PostMapping("/naver")			// naverForm.jsp의 upload name으로 보낸 파일의 타입 = MultipartFile
	public String naver(Model model, MultipartFile upload) throws Exception {
		File uploadFile = new File(upload.getOriginalFilename());
		upload.transferTo(uploadFile);		//  uploadDir로 파일 저장
		 
		String apiURL = "https://naveropenapi.apigw.ntruss.com/vision-obj/v1/detect";
		URL url = new URL(apiURL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");			//post로보냄 (생략해도 무방)
		
        // multipart request
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
        
        // 파일을 읽어서 전송					// 추가한 부분 (경로)
        FileInputStream fis = new FileInputStream(uploadDir + "/" + uploadFile);
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
