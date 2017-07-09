package com.heartof.board.service;

import java.util.List;
import java.util.Map;

import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardFileVO;
import com.heartof.board.vo.TB_BoardVO;

public interface BoardService {
	public List<TB_BoardVO> list(PageVO vo) throws Exception;

	public Map<String, Object> detail(int no) throws Exception ;
	
	public int update(TB_BoardVO vo)  throws Exception;
	
	public int insert(TB_BoardVO vo) throws Exception;
	
	public int insertFile(TB_BoardFileVO vo) throws Exception;

	public int delete(TB_BoardVO vo) throws Exception;

	public Map<String, String> recomend(int no, String id) throws Exception;
}
