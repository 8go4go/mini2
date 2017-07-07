package com.heartof.board.service;

import com.heartof.board.vo.TB_UserVO;

public interface MemberService {
	public TB_UserVO isLogin(TB_UserVO vo) throws Exception;
}
