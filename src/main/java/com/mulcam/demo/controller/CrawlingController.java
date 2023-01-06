package com.mulcam.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mulcam.demo.crawling.Crawling;
import com.mulcam.demo.crawling.Interpark;

@Controller
@RequestMapping("/crawling")
public class CrawlingController {
	
	@GetMapping("/interpark")
	public String interpark(Model model) throws Exception {
		Crawling crawling = new Crawling();
		List<Interpark> list = crawling.interpark();
		model.addAttribute("bookList", list);
		return "crawling/interpark";
	}
}
