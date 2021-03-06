package lecture;

import java.util.ArrayList;
import java.util.Scanner;

import lectureContent.Content;
import mgr.Manageable;
import univ.UIData;
import user.Professor;
import user.Student;

public class Lecture implements Manageable, UIData {
	String title;
	String code; // 기본키
	String day;
	String time;
	Professor professor;
	ArrayList<Student> registeredStudentList = new ArrayList<>();
	ArrayList<Content> lectureContentList = new ArrayList<>();

	public Lecture() {

	}

	public Lecture(String title, String code, String day, String time, Professor professor) {
		this.title = title;
		this.code = code;
		this.day = day;
		this.time = time;
		this.professor = professor;
	}

	@Override
	public void read(Scanner scan) {
		title = scan.next();
		code = scan.next();
		day = scan.next();
		time = scan.next();
	}

	@Override
	public void print() {
		System.out.printf("%s %s %s %s %s\n", title, code, day, time, professor.getName());
	}

	public void printWithoutName() {
		System.out.printf("%s %s %s %s\n", title, code, day, time);
	}

	@Override
	public boolean matches(String kwd, boolean isSearch) {
		if (isSearch) {
			if (code.contains(kwd)) // 과목 코드로 검색
				return true;
			if (title.contains(kwd)) // 과목 명으로 검색
				return true;
			if (day.contentEquals(kwd)) // 수업 일로 검색
				return true;
			if (time.contentEquals(kwd)) // 수업 시간으로 검색
				return true;
			if (professor != null) {
				if ((professor.getName()).contains(kwd)) // 담당 교수 이름으로 검색
					return true;
			}
		} else {
			if (code.contentEquals(kwd)) // 과목 코드로 검색
				return true;
		}
		return false;
	}

	public void registerStudent(Student student) {
		registeredStudentList.add(student);
	}

	public ArrayList<Student> getRegisterStudentList() {
		return registeredStudentList;
	}

	public void registerProfessor(Professor professor) {
		this.professor = professor;
	}

	public void addContent(Content content) {
		lectureContentList.add(content);
	}

	public void deleteContent(Content content) {
		lectureContentList.remove(content);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<Content> getlectureContentList() {
		return lectureContentList;
	}

	public Professor getProfessor() {
		return professor;
	}

	public String getNextContentsKey(Lecture lecture, int type) {
		String ContentsKey = null;

		ArrayList<Content> list = new ArrayList<>();
		switch (type) {
		case 1: {
			for (Content m : lecture.getlectureContentList()) { // 과제들을 다찾아 리스트에 넣고 리스트에 사이즈에 1을추가해 다음 컨텐츠키를 찾음
				if (m.getCode().contentEquals(lecture.getCode()) && m.getContentKey().startsWith("A"))
					list.add(m);
			}
			int i = list.size() + 1;
			ContentsKey = "A" + i;
		}
			break;
		case 2: {
			for (Content m : lecture.getlectureContentList()) {
				if (m.getCode().contentEquals(lecture.getCode()) && m.getContentKey().startsWith("N"))
					list.add(m);
			}
			int i = list.size() + 1;
			ContentsKey = "N" + i;
		}
			break;
		case 3: {
			for (Content m : lecture.getlectureContentList()) {
				if (m.getCode().contentEquals(lecture.getCode()) && m.getContentKey().startsWith("E"))
					list.add(m);
			}
			int i = list.size() + 1;
			ContentsKey = "E" + i;
		}
			break;
		case 4: {
			for (Content m : lecture.getlectureContentList()) {
				if (m.getCode().contentEquals(lecture.getCode()) && m.getContentKey().startsWith("V"))
					list.add(m);
			}
			int i = list.size() + 1;
			ContentsKey = "V" + i;
		}
			break;
		default:
			break;
		}
		return ContentsKey;
	}

	@Override
	public void set(Object[] uitexts) {
	}

	@Override
	public String[] getUiTexts(boolean isAdmin) {
		if (isAdmin)
			return new String[] { title, code, day, time };

		return new String[] { title, day, time };
	}
}
