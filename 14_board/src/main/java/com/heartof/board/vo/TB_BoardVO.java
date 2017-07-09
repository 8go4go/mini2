package com.heartof.board.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TB_BoardVO {
	private int no;
	private String title;
	private String writer;
	private String content;
	private Date reg_date;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReg_date() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		return format.format(reg_date);
	}

	public void setReg_date(Date reg_date) {
		this.reg_date = reg_date;
	}

	@Override
	public String toString() {
		return "TB_BoardVO [no=" + no + ", title=" + title + ", writer=" + writer + ", content=" + content
				+ ", reg_date=" + reg_date + "]";
	}
}
