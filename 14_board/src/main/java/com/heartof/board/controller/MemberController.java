package com.heartof.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/member")
public class MemberController {
	@RequestMapping("/loginForm.do")
	public void loginForm() {}
	
	@RequestMapping("/loginProcess.do")
	@ResponseBody
	public String loginProcess() {
		return "ok";
	}
}
