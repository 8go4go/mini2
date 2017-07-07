package com.heartof.board.service;

import java.util.List;

import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardVO;

public interface BoardService {
	public List<TB_BoardVO> list(PageVO vo) throws Exception;

	public TB_BoardVO detail(int parseInt)  throws Exception;
}
