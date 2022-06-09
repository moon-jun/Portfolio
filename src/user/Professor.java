package user;

import java.util.ArrayList;
import java.util.Scanner;

import lecture.Lecture;
import univ.University;

public class Professor extends User {

	ArrayList<Lecture> professorLectureList = new ArrayList<>();

	public Professor() {

	}

	public Professor(String role, String id, String pw, String name, int age, String phone) {
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
		String code = null;
		Lecture lec = null;
		while (true) {
			code = scan.next();
			if (code.equals("0"))
				break;
			lec = (Lecture) University.lectureMgr.find(code, false); // 과목 코드와 일치하는 Lecture 찾기
			if (lec == null) {
				System.out.println("해당 과목은 없습니다. " + code);
				continue;
			}
			getProfessorLectureList().add(lec); // 교수의 수강 과목 리스트에 찾은 Lecture를 저장
			lec.registerProfessor(this); // 찾은 Lecture의 담당 교수에 이 교수
		}
	}

	@Override
	public void print() {
		super.print();
		System.out.println();
	}

	public ArrayList<Lecture> getProfessorLectureList() {
		return professorLectureList;
	}

	public void addLecture(Lecture lec) {
		professorLectureList.add(lec);
	}

	public void deleteLecture(Lecture lec) {
		professorLectureList.remove(lec);
	}
}
