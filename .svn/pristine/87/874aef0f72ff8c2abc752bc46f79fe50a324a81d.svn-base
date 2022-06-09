package user;

import java.util.ArrayList;
import java.util.Scanner;

import mgr.Manageable;
import univ.UIData;
import userContent.Message;

public class User implements Manageable, UIData {
	String role;
	String id; // 기본키
	String pw;
	String name;
	int age;
	String phone;
	ArrayList<Message> receivedMessegeList = new ArrayList<>();
	ArrayList<Message> sentMessegeList = new ArrayList<>();

	public User() {

	}

	public User(String role, String id, String pw, String name, int age, String phone) {
		this.role = role;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.age = age;
		this.phone = phone;
	}

	@Override
	public void read(Scanner scan) {
		role = scan.next();
		id = scan.next();
		pw = scan.next();
		name = scan.next();
		age = scan.nextInt();
		phone = scan.next();
	}

	@Override
	public void print() {
		System.out.printf("%s %s %s %s %d %s", role, id, pw, name, age, phone);
	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		if (isSearch) {
			if (id.contains(kwd))
				return true;
			if (role.contentEquals(kwd)) // 역할로 검색
				return true;
			if (name.contains(kwd)) // 이름으로 검색
				return true;
			if (("" + age).contentEquals(kwd)) // 키워드로 검색
				return true;
			if (phone.contains(kwd)) // 연락처로 검색
				return true;
		}

		else {
			if (kwd.contentEquals(id))
				return true;
		}
		return false;
	}

	public String getRole() {
		return role;
	}

	public String getId() {
		return id;
	}

	public String getPw() {
		return pw;
	}

	public void setPW(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ArrayList<Message> getReceivedMessageList() {
		return receivedMessegeList;
	}

	public void addReceivedMessage(Message msg) {
		receivedMessegeList.add(msg);
	}

	public ArrayList<Message> getSentMessageList() {
		return sentMessegeList;
	}

	public void addSentMessage(Message msg) {
		sentMessegeList.add(msg);
	}

	@Override
	public void set(Object[] uitexts) {
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		return new String[] { role, id, name };
	}
}
