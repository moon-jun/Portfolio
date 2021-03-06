package user;

import java.util.ArrayList;

import java.util.Scanner;

import lecture.Lecture;
import univ.University;
import userContent.Todo;

public class Student extends User {
	int grade;
	ArrayList<Lecture> studentLectureList = new ArrayList<>();
	ArrayList<Todo> todoList = new ArrayList<>();

	public Student() {

	}

	public Student(String role, String id, String pw, String name, int age, String phone) {
		this.role = role;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.age = age;
		this.phone = phone;
	}

	@Override
	public void read(Scanner scan) {
		super.read(scan);
		grade = scan.nextInt();
		String code = null;
		Lecture lec = null;
		while (true) {
			code = scan.next();
			if (code.equals("0"))
				break;
			lec = (Lecture) University.lectureMgr.find(code, false); // Students.txt 에서 읽은 과목 코드와 일치하는 Lecture 찾기
			if (lec == null) {
				System.out.println("해당 과목은 없습니다. " + code);
				continue;
			}
			getStudentLectureList().add(lec); // 학생의 수강 과목 리스트에 찾은 Lecture를 저장
			lec.registerStudent(this); // 찾은 Lecture의 수강자 리스트에 이 학생을 저장
		}
	}

	@Override
	public void print() {
		super.print();
		System.out.printf(" %d", grade);
		System.out.println();
	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		if (super.matches(kwd, isSearch))
			return true;
		if (isSearch) {
			if (kwd.contentEquals("" + grade))
				return true;
		}
		return false;
	}

	public ArrayList<Lecture> getStudentLectureList() {
		return studentLectureList;
	}

	public int getStudentGrade() {
		return grade;
	}

	public void deleteStudentLecture(Lecture lec) {
		studentLectureList.remove(lec);
	}

	public Lecture getLecturebyindex(int code) { // index로 과목 리스트에서 과목 가져오기
		return getStudentLectureList().get(code - 1);

	}

	public ArrayList<Todo> getTodoList() {
		return todoList;
	}

	public void addTodo(Todo todo) {
		todoList.add(todo);
	}

	public void deleteTodo(Todo todo) {
		todoList.remove(todo);
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		if (isAdmin)
			return new String[] { role, id, name }; 

		return new String[] { name };
	}
}