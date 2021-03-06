package lectureContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import user.Student;

public class Assignment extends Content {
	HashMap<String, String> submittedAssignment = new HashMap<String, String>();
	ArrayList<Student> submittedStudentList = new ArrayList<>();

	public Assignment() {

	}

	public Assignment(String code, String contentKey, Date date, String tmp) {
		this.code = code;
		this.contentKey = contentKey;
		this.date = date;
		this.body = tmp;
	}

	@Override
	void printContentType() {
		System.out.print("[과제]");
	}

	public void submitAssignment(String text, Student st) {
		submittedAssignment.put(st.getId(), text);
		submittedStudentList.add(st);
	}

	public ArrayList<Student> getSubmittedStudentList() {
		return submittedStudentList;
	}

	public ArrayList<Student> getUnsubmittedStudentList() {
		ArrayList<Student> list = (ArrayList<Student>) lecture.getRegisterStudentList().clone();
		for (Student m : submittedStudentList) {
			list.remove(m);
		}

		return list;
	}

}
