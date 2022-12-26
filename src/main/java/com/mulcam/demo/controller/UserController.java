package com.mulcam.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mulcam.demo.entity.User;
import com.mulcam.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<User> list = service.getList();
		model.addAttribute("userList", list);
		return "user/list";
	}
}
