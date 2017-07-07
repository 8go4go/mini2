package com.heartof.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.heartof.board.service.BoardService;
import com.heartof.board.vo.PageVO;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService board;
	
	@RequestMapping("/{limit}/{start}/list.do")
	@ResponseBody
	public Object listProcess(@PathVariable String limit, @PathVariable String start) throws Exception {
		PageVO vo = new PageVO();
		
		if(limit == null && start == null) {
			vo.setLimit(10);
			vo.setStart(1);
		} else {
			if(Integer.parseInt(limit) < 10)
				vo.setLimit(10);
			else
				vo.setLimit(Integer.parseInt(limit));
			
			if(Integer.parseInt(start) < 1)
				vo.setStart(1);
			else
				vo.setStart(Integer.parseInt(start));
		}
		return board.list(vo);
	}
	
	@RequestMapping("/{no}/detail.do")
	@ResponseBody
	public Object detailProcess(@PathVariable String no) throws Exception {
		return board.detail(Integer.parseInt(no));
	}
}
