package lectureContent;

import java.util.Date;
import java.util.Scanner;

import lecture.Lecture;
import mgr.Manageable;
import univ.UIData;
import univ.University;

public class Content implements Manageable, UIData {
	String code;
	public String contentKey; // 기본키
	Date date; // 작성일
	String body;
	Lecture lecture;

	public Content() {

	}

	public Content(String code, String contentKey, Date date, String body) {
		this.code = code;
		this.contentKey = contentKey;
		this.date = date;
		this.body = body;
	}

	@Override
	public void read(Scanner scan) {
		code = scan.next();
		contentKey = scan.next();
		lecture = University.lectureMgr.find(code, false); // 컨텐츠 데이터 파일에서 읽은 과목 코드와 일치하는 Lecture 찾기
		if (lecture == null) {
			System.out.println("해당 과목은 없습니다. " + code);
		}
		String strDate = scan.next();
		date = University.StringToDate(strDate);
		body = scan.nextLine();
		lecture.addContent(this);
	}

	void printContentType() {
		System.out.print("[컨텐츠]");
	};

	@Override
	public void print() {
		printContentType();
		System.out.printf(" %s %s %s %s\n", code, contentKey, date, body);
	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		if (isSearch) {
			if (contentKey.contains(kwd)) // 컨텐츠키로 검색
				return true;
			if (code.contains(kwd)) // 과목 코드로 검색
				return true;
			if (lecture.getTitle().contains(kwd)) // 과목 명으로 검색
				return true;
			String strDate = University.DateToString(date);
			if (strDate.contains(kwd)) // 날짜로 검색
				return true;
			if (body.contains(kwd)) // 내용으로 검색
				return true;
		}

		else {
			if (contentKey.contentEquals(kwd)) // 컨텐츠키로 검색
				return true;
		}
		return false;
	}

	public String getContentKey() {
		return contentKey;
	}

	public String getCode() {
		return code;
	}

	public String getLectureTitle() {
		return lecture.getTitle();
	}

	public Date getDate() {
		return date;
	}

	public String getBody() {
		return body;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContentCategory() {
		String contentCategory = null;
		if (contentKey.startsWith("A"))
			contentCategory = "과제";
		else if (contentKey.startsWith("E"))
			contentCategory = "시험";
		else if (contentKey.startsWith("N"))
			contentCategory = "공지";
		else
			contentCategory = "강의영상";

		return contentCategory;
	}

	@Override
	public void set(Object[] uitexts) {
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		String strDate = University.DateToString(date);
		if (isAdmin)
			return new String[] { code, contentKey, strDate, body };
		lecture = University.lectureMgr.find(code, false);
		String LectureTitle = lecture.getTitle();
		String contentCategory = getContentCategory();
		return new String[] { LectureTitle, contentCategory, strDate, body };
	}
}
