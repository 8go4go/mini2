package com.heartof.board.db.mapper;

import java.util.List;

import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardCommentVO;
import com.heartof.board.vo.TB_BoardFileVO;
import com.heartof.board.vo.TB_BoardRecommendVO;
import com.heartof.board.vo.TB_BoardVO;
import com.heartof.board.vo.TB_UserVO;

public interface BoardMapper {
	public TB_UserVO islogin(TB_UserVO vo);
	
	public List<TB_BoardVO> selectList(PageVO vo);
	
	public TB_BoardVO selectDetail(int no);
	
	public int selectTotalRecommend(int no);
	
	public int insertBoard(TB_BoardVO vo);
	
	public int updateBoard(TB_BoardVO vo);
	
	public int deleteBoard(TB_BoardVO vo);
	
	public int insertRecommend(TB_BoardRecommendVO vo);
	
	public int deleteRecommend(TB_BoardRecommendVO vo);
	
	public List<TB_BoardCommentVO> selectComment(int no);
	
	public int deleteAllComment(int no);
	
	public int deleteComment(TB_BoardCommentVO vo);
	
	public int insertComment(TB_BoardCommentVO vo);
	
	public int updateComment(TB_BoardCommentVO vo);
	
	public List<TB_BoardFileVO> selectFile(int no);
	
	public int insertFile(TB_BoardFileVO vo);
	
	public int deleteAllFile(int no);
}
