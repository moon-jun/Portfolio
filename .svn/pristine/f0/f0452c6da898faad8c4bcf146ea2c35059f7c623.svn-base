package userContent;

import java.util.Date;
import java.util.Scanner;

import mgr.Manageable;
import univ.UIData;
import univ.University;
import user.Student;

public class Todo implements Manageable, UIData {
	Date date;
	String text;
	boolean isFinished;

	public Todo() {
	}

	public Todo(Date date, String text) {
		this.date = date;
		this.text = text;
		this.isFinished = false;
	}

	@Override
	public void read(Scanner scan) {
		String id = scan.next();
		Student st = (Student) University.studentMgr.find(id, false);
		String tmp = scan.next();
		if (tmp.contentEquals("T"))
			isFinished = true;
		else
			isFinished = false;
		String strDate = scan.next();
		date = University.StringToDate(strDate);
		text = scan.nextLine();
		st.addTodo(this);
	}

	@Override
	public void print() {

	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		return false;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String showFinished() {
		if (isFinished) {
			return "Y";
		}
		return "N";
	}

	public boolean getFinished() {
		return isFinished;
	}

	public void setFinished() {
		this.isFinished = true;
	}

	public void setUnfinished() {
		this.isFinished = false;
	}

	@Override
	public void set(Object[] uitexts) {
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		String strDate = University.DateToString(date);
		if (isFinished) {
			return new String[] { strDate, text, "Y" };
		}
		return new String[] { strDate, text, "N" };
	}
}
