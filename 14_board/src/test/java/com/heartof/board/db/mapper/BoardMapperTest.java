package com.heartof.board.db.mapper;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardCommentVO;
import com.heartof.board.vo.TB_BoardFileVO;
import com.heartof.board.vo.TB_BoardRecommendVO;
import com.heartof.board.vo.TB_BoardVO;
import com.heartof.board.vo.TB_UserVO;

public class BoardMapperTest {
	private static final SqlSession sqlMapper;
	static {
		try {
			String resource = "config/db/sqlMapConfig.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			SqlSessionFactory sqlFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlMapper = sqlFactory.openSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing MyAppSqlConfig class. Cause: " + e);
		}
	}

	public static SqlSession getSqlSessionInstance() {
		return sqlMapper;
	}

	public static void main(String[] args) {
		SqlSession session = BoardMapperTest.getSqlSessionInstance();
		BoardMapper mapper = session.getMapper(BoardMapper.class);
		selectTotalRecommend(mapper);
		session.commit();
	}
	
	public static void islogin(BoardMapper mapper) {
		TB_UserVO vo = new TB_UserVO();
		vo.setId("a");
		vo.setPass("1");
		System.out.println(mapper.islogin(vo));
	}
	
	public static  void selectList(BoardMapper mapper) {
		PageVO vo = new PageVO();
		vo.setLimit(10);
		vo.setStart(1);
		System.out.println(mapper.selectList(vo));
	}
	
	public static void insertBoard(BoardMapper mapper) {
		TB_BoardVO vo = new TB_BoardVO();
		vo.setContent("안녕하세요");
		vo.setWriter("d");
		vo.setTitle("가입인사");
		mapper.insertBoard(vo);
		System.out.println(vo.getNo());
	}

	public static void updateBoard(BoardMapper mapper) {
		TB_BoardVO vo = new TB_BoardVO();
		vo.setContent("안녕하세요!!!!");
		vo.setWriter("a");
		vo.setTitle("가입인사!!");
		vo.setNo(11);
		mapper.updateBoard(vo);		
	}

	public static void deleteBoard(BoardMapper mapper) {
		TB_BoardVO vo = new TB_BoardVO();
		vo.setWriter("a");
		vo.setNo(11);
		mapper.deleteBoard(vo);	
	}

	public static void selectTotalRecommend(BoardMapper mapper) {
		TB_BoardRecommendVO vo = new TB_BoardRecommendVO();
		vo.setId("c"); vo.setNo(4);
		mapper.insertRecommend(vo);
	}

	public static void selectComment(BoardMapper mapper) {
		System.out.println(mapper.selectComment(1));
	}

	public static void deleteAllComment(BoardMapper mapper) {
	}

	public static void deleteComment(TB_BoardCommentVO vo) {}
	
	public static void insertComment(BoardMapper mapper){
		TB_BoardCommentVO vo = new TB_BoardCommentVO();
		vo.setNo(4);
		vo.setContent("댓글입니다.");
		vo.setUser_id("c");		
		mapper.insertComment(vo);
	}
	
	public static void updateComment(BoardMapper mapper){
		TB_BoardCommentVO vo = new TB_BoardCommentVO();
		vo.setComment_no(7);
		vo.setNo(5);
		vo.setContent("댓글입니꽈");
		vo.setUser_id("a");		
		mapper.updateComment(vo);
	}
	
	public static void selectFile(BoardMapper mapper){
		System.out.println(mapper.selectFile(1));
	}
	
	public static void insertFile(BoardMapper mapper){
		TB_BoardFileVO vo = new TB_BoardFileVO();
		vo.setNo(2);
		vo.setOri_name("테스트파일1");
		vo.setSystem_name("시스템파일1");
		vo.setFile_path("c:/test/test");
		vo.setFile_size(1024);
		mapper.insertFile(vo);
		
		vo = new TB_BoardFileVO();
		vo.setNo(2);
		vo.setOri_name("테스트파일2");
		vo.setSystem_name("시스템파일2");
		vo.setFile_path("c:/test/test");
		vo.setFile_size(1024);
		mapper.insertFile(vo);
	}
	
	public static void deleteAllFile(BoardMapper mapper){
		mapper.deleteAllFile(1);
	}
}
