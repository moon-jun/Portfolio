package userContent;

import java.util.Date;
import java.util.Scanner;

import mgr.Manageable;
import univ.UIData;
import univ.University;
import user.User;

public class Message implements Manageable, UIData {
	String from;
	String to;
	Date date;
	String title;
	String text;

	public Message() {

	}

	public Message(String from, String to, Date date, String title, String text) {
		this.from = from;
		this.to = to;
		this.date = date;
		this.title = title;
		this.text = text;
	}

	@Override
	public void read(Scanner scan) {
		from = scan.next();
		to = scan.next();
		String strDate = scan.next();
		date = University.StringToDate(strDate);
		title = scan.next();
		text = scan.nextLine();

		User fromUser = University.userMgr.find(from, false);
		fromUser.addSentMessage(this);

		User toUser = University.userMgr.find(to, false);
		toUser.addReceivedMessage(this);
	}

	@Override
	public void print() {
	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		return false;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getTitle() {
		return title;
	}

	public Date getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	@Override
	public void set(Object[] uitexts) {
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		String strDate = University.DateToString(date);
		return new String[] { from, to, strDate, title };
	}
}
