package com.heartof.board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.heartof.board.service.BoardService;
import com.heartof.board.vo.PageVO;
import com.heartof.board.vo.TB_BoardFileVO;
import com.heartof.board.vo.TB_BoardVO;
import com.heartof.board.vo.TB_UserVO;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService board;
	
	@Autowired
	ServletContext servletContext;
	
	@RequestMapping("/{limit}/{start}/list.do")
	@ResponseBody
	public Object listProcess(@PathVariable String limit, @PathVariable String start) throws Exception {
		PageVO vo = new PageVO();
		
		if(limit == null && start == null) {
			vo.setLimit(10);
			vo.setStart(0);
		} else {
			vo.setLimit(10);
			
			if(Integer.parseInt(start) < 1)
				vo.setStart(0);
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
	
	@RequestMapping("/update.do")
	@ResponseBody
	public Object updateProcess(TB_BoardVO vo, HttpServletRequest req) throws Exception {
		TB_UserVO user = (TB_UserVO)req.getSession().getAttribute("user");
		vo.setWriter(user.getId());
		int returnVal = board.update(vo);
		return returnVal;
	}
	
	@RequestMapping("/{no}/recommend.do")
	@ResponseBody
	@Transactional(rollbackFor=Exception.class)
	public Object recommendProcess(@PathVariable String no, HttpServletRequest req) throws Exception {
		TB_UserVO user = (TB_UserVO)req.getSession().getAttribute("user");
		return board.recomend(Integer.parseInt(no), user.getId());
	}
	
	@RequestMapping("/recommend.do")
	@ResponseBody
	@Transactional(rollbackFor=Exception.class)
	public Object addCommentProcess(@PathVariable String no, HttpServletRequest req) throws Exception {
		TB_UserVO user = (TB_UserVO)req.getSession().getAttribute("user");
		return board.recomend(Integer.parseInt(no), user.getId());
	}
	
	@RequestMapping("/{no}/{id}/delete.do")
	@ResponseBody
	@Transactional(rollbackFor=Exception.class)
	public Object deleteProcess(@PathVariable String no, @PathVariable String id,
			HttpServletRequest req) throws Exception {
		TB_UserVO user = (TB_UserVO)req.getSession().getAttribute("user");
		TB_BoardVO vo = new TB_BoardVO();
		vo.setNo(Integer.parseInt(no));
		 
		if(!id.equals(user.getId())) return 1000;
		
		vo.setWriter(user.getId());
		int returnVal = board.delete(vo);
		return returnVal;
	}
	
	@RequestMapping(value="/insert.do", method=RequestMethod.POST)
	@ResponseBody
	@Transactional(rollbackFor=Exception.class)
	public Object fileUpload(
			MultipartHttpServletRequest mRequest) throws Exception {
		TB_UserVO user = (TB_UserVO)mRequest.getSession().getAttribute("user");
		// 실행되는 웹어플리케이션의 실제 경로 가져오기
		String uploadDir = servletContext.getRealPath("/upload/");
		System.out.println(uploadDir);
		
		TB_BoardVO vo = new TB_BoardVO();
		vo.setTitle(mRequest.getParameter("title"));
		vo.setContent(mRequest.getParameter("content"));
		vo.setWriter(user.getId());
		int returnVal = board.insert(vo);
		if(returnVal != 1) throw new Exception("0");
		
		Iterator<String> iter = mRequest.getFileNames();
		while(iter.hasNext()) {
			String formFileName = iter.next();
			// 폼에서 파일을 선택하지 않아도 객체 생성됨
			MultipartFile mFile = mRequest.getFile(formFileName);
			
			// 원본 파일명
			String oriFileName = mFile.getOriginalFilename();
			System.out.println("원본 파일명 : " + oriFileName);
			
			if(oriFileName != null && !oriFileName.equals("")) {
				// 확장자 처리
				String ext = "";
				// 뒤쪽에 있는 . 의 위치 
				int index = oriFileName.lastIndexOf(".");
				if (index != -1) {
					// 파일명에서 확장자명(.포함)을 추출
					ext = oriFileName.substring(index);
				}
				
				// 파일 사이즈
				long fileSize = mFile.getSize();
				System.out.println("파일 사이즈 : " + fileSize);
				
				// 고유한 파일명 만들기	
				String saveFileName = "mlec-" + UUID.randomUUID().toString() + ext;
				System.out.println("저장할 파일명 : " + saveFileName);
			
				// 임시저장된 파일을 원하는 경로에 저장
				mFile.transferTo(
					new File(uploadDir + "/" + saveFileName));
				
				TB_BoardFileVO fileVO = new TB_BoardFileVO();
				fileVO.setNo(vo.getNo());
				fileVO.setFile_path(uploadDir);
				fileVO.setFile_size((int)fileSize);
				fileVO.setOri_name(oriFileName);
				fileVO.setSystem_name(saveFileName);
				returnVal = board.insertFile(fileVO);
				if(returnVal != 1) throw new Exception("0");
			} 
		} 
		return 1;
	}
	
	@RequestMapping("/download.do")
	@ResponseBody
	public void downloadProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 어떤 경로의 어떤 파일을 어떤 이름으로 다운로드 할지
		// 사용자가 파라미터로 넘겨야 한다.
		ServletContext context = request.getServletContext();
		String uploadPath = context.getRealPath("/upload");
		String sName = request.getParameter("sName");
		// 파일을 읽기 위한 파일 객체 생성
		File f = new File(uploadPath, sName);
		// dName : 사용자 컴퓨터에 저장할 파일명
		String dName = request.getParameter("oName");
		// 다운로드할 이름을 주지 않은 경우 사용자 브라우져에 이미지 출력	
		if (dName == null) {
			response.setHeader("Content-Type", "image/jpg");
		} else {
			response.setHeader("Content-Type","application/octet-stream");
			dName = new String(dName.getBytes("utf-8"), "8859_1");
			response.setHeader("Content-Disposition", "attachment;filename=" + dName);
			response.setHeader("Content-Transfer-Encoding","binary");
			response.setHeader("Content-Length",String.valueOf(f.length()));
		}
		// 특정 위치의 파일을 읽어서 사용자 브라우져로 출력
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		OutputStream out = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(out);
		
		while (true) {
			int ch = bis.read();
			if (ch == -1) break;
			bos.write(ch);
		}
		bis.close();
		fis.close();
		bos.close();
		out.close();
	}
}
