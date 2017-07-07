package com.heartof.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heartof.board.db.mapper.BoardMapper;
import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardVO;

@Service("board")
public class BoardServiceImpl implements BoardService {
	@Autowired
	BoardMapper boardMapper;
	
	@Override
	public List<TB_BoardVO> list(PageVO vo) throws Exception {
		return boardMapper.selectList(vo);
	}

	@Override
	public TB_BoardVO detail(int no) throws Exception {
		// TODO Auto-generated method stub
		return boardMapper.selectDetail(no);
	}
}