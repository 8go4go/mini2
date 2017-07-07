package com.heartof.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heartof.board.db.mapper.BoardMapper;
import com.heartof.board.vo.TB_UserVO;

@Service("member")
public class MemberServiceImpl implements MemberService {
	@Autowired
	BoardMapper boardMapper;
	
	public TB_UserVO isLogin(TB_UserVO vo) throws Exception {
		return boardMapper.islogin(vo);
	}
}
