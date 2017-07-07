package com.heartof.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heartof.board.service.MemberService;
import com.heartof.board.vo.TB_UserVO;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService member;
	
	@RequestMapping("/loginForm.do")
	public void loginForm() {}
	
	@RequestMapping("/loginProcess.do")
	@ResponseBody
	public Object loginProcess(TB_UserVO vo, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		TB_UserVO user = member.isLogin(vo);
		
		if(user == null)
			return new String("error");
		session.setAttribute("user", user);
		return user;
	}
	
	@RequestMapping("/logoutProcess.do")
	@ResponseBody
	public Object logoutProcess(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		TB_UserVO user = (TB_UserVO)session.getAttribute("user");
		if(user == null)
			return new String("already logout");
		
		session.removeAttribute("user");
		return new String("logout");
	}
}
