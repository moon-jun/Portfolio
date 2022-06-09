package userContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import mgr.Manageable;
import univ.UIData;
import univ.University;

public class ProgramNotice implements Manageable, UIData {
	Date date;
	String body;
	ArrayList<ProgramNotice> proNtc = new ArrayList<>();

	public ProgramNotice() {

	}

	public ProgramNotice(Date date, String body) {
		this.date = date;
		this.body = body;
	}

	@Override
	public void read(Scanner scan) {
		String strDate = scan.next();
		date = University.StringToDate(strDate);
		body = scan.nextLine();
	}

	@Override
	public void print() {
	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		if (isSearch) {
			String strDate = University.DateToString(date);
			if (strDate.contains(kwd)) // 날짜로 검색
				return true;
			if (body.contains(kwd)) // 내용으로 검색
				return true;
		}
		return false;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public void set(Object[] uitexts) {
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		String strDate = University.DateToString(date);
		return new String[] { strDate, body };
	}

}
