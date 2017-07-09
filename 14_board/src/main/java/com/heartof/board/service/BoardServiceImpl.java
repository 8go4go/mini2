package com.heartof.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heartof.board.db.mapper.BoardMapper;
import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardFileVO;
import com.heartof.board.vo.TB_BoardRecommendVO;
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
	public Map<String, Object> detail(int no) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> board = new HashMap<>();
		board.put("board", boardMapper.selectDetail(no));
		board.put("files", boardMapper.selectFile(no));
		board.put("recommend", boardMapper.selectTotalRecommend(no));
		board.put("comments", boardMapper.selectComment(no));
		return board;
	}

	@Override
	public int update(TB_BoardVO vo) throws Exception {
		return boardMapper.updateBoard(vo);
	}

	@Override
	public int insert(TB_BoardVO vo) throws Exception {
		return boardMapper.insertBoard(vo);
	}

	@Override
	public int insertFile(TB_BoardFileVO vo) throws Exception {
		return boardMapper.insertFile(vo);
	}

	@Override
	public int delete(TB_BoardVO vo) throws Exception {
		System.out.println(boardMapper.deleteAllComment(vo.getNo()));
		System.out.println(boardMapper.deleteAllFile(vo.getNo()));
		int result = boardMapper.deleteBoard(vo);
		return result;
	}
	
	@Override
	public Map<String, String> recomend(int no, String id) throws Exception {
		TB_BoardRecommendVO vo = new TB_BoardRecommendVO();
		vo.setId(id);
		vo.setNo(no);
		int result = boardMapper.selectRecommend(vo);
		Map<String, String> resultMap = new HashMap<> ();
		if(result == 1) {
			result = boardMapper.deleteRecommend(vo);
			if(result == 1) resultMap.put("D", "1");
			else resultMap.put("D", "0");
		} else {
			result = boardMapper.insertRecommend(vo);
			if(result == 1) resultMap.put("I", "1");
			else resultMap.put("I", "0");
		}
		return resultMap;
	}
}