package univ;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import lecture.Lecture;
import lectureContent.Assignment;
import lectureContent.Content;
import lectureContent.Exam;
import lectureContent.Notice;
import lectureContent.Video;
import mgr.Factory;
import mgr.Manager;
import user.Admin;
import user.Professor;
import user.Student;
import user.User;
import userContent.Message;
import userContent.ProgramNotice;
import userContent.Todo;

public class University {
	Scanner scan = new Scanner(System.in);

	public static Manager<Lecture> lectureMgr = new Manager<>(); // 과목 담당 매니저

	static Manager<Admin> adminMgr = new Manager<>();
	static Manager<Professor> professorMgr = new Manager<>();
	public static Manager<Student> studentMgr = new Manager<>(); // 각 유저 담당 매니저
	public static Manager<User> userMgr = new Manager<>(); // 전체 유저 담당 매니저

	static Manager<Assignment> assignmentMgr = new Manager<>();
	static Manager<Exam> examMgr = new Manager<>();
	static Manager<Notice> noticeMgr = new Manager<>();
	static Manager<Video> videoMgr = new Manager<>(); // 각 컨텐츠 담당 매니저
	static Manager<Content> contentMgr = new Manager<>(); // 전체 컨텐츠 담당 매니저

	static Manager<Todo> todoMgr = new Manager<>(); // 할 일 담당 매니저
	static Manager<Message> messageMgr = new Manager<>(); // 메시지 담당 매니저
	static Manager<ProgramNotice> programNoticeMgr = new Manager<>(); // 프로그램 공지사항 담당 매니저

	void run() {
		// 데이터 입력
		readLecture();
		readUser();
		readContent();
		readTodo();
		readMessage();
		readProgramNotice();

		// 데이터 출력
		System.out.println("\n========== 과목 ==========\n");
		lectureMgr.printAll();
		System.out.println("\n========== 유저 ==========\n");
		printUser();
		printContent();

		// 로그인
		login();
	}

	void readLecture() {
		lectureMgr.readAll("Lectures.txt", new Factory<Lecture>() {
			public Lecture create() {
				return new Lecture();
			}
		});
	}

	void readUser() {
		adminMgr.readAll("Admins.txt", new Factory<Admin>() {
			public Admin create() {
				return new Admin();
			}
		});
		professorMgr.readAll("Professors.txt", new Factory<Professor>() {
			public Professor create() {
				return new Professor();
			}
		});
		studentMgr.readAll("Students.txt", new Factory<Student>() {
			public Student create() {
				return new Student();
			}
		});

		// 유저 매니저의 리스트에 모든 사용자 추가
		for (User m : adminMgr.mList) {
			userMgr.mList.add(m);
		}
		for (Professor m : professorMgr.mList) {
			userMgr.mList.add(m);
		}
		for (Student m : studentMgr.mList) {
			userMgr.mList.add(m);
		}
	}

	void printUser() {
		userMgr.printAll();
	}

	void readContent() {
		assignmentMgr.readAll("Assignments.txt", new Factory<Assignment>() {
			public Assignment create() {
				return new Assignment();
			}
		});
		examMgr.readAll("Exams.txt", new Factory<Exam>() {
			public Exam create() {
				return new Exam();
			}
		});
		noticeMgr.readAll("Notices.txt", new Factory<Notice>() {
			public Notice create() {
				return new Notice();
			}
		});
		videoMgr.readAll("Videos.txt", new Factory<Video>() {
			public Video create() {
				return new Video();
			}
		});

		// 컨텐츠 매니저의 리스트에 모든 컨텐츠 추가
		for (Assignment m : assignmentMgr.mList) {
			contentMgr.mList.add(m);
		}
		for (Exam m : examMgr.mList) {
			contentMgr.mList.add(m);
		}
		for (Notice m : noticeMgr.mList) {
			contentMgr.mList.add(m);
		}
		for (Video m : videoMgr.mList) {
			contentMgr.mList.add(m);
		}
	}

	void printContent() {
		contentMgr.printAll();
	}

	void readTodo() {
		todoMgr.readAll("Todo.txt", new Factory<Todo>() {
			public Todo create() {
				return new Todo();
			}
		});
	}

	void readMessage() {
		messageMgr.readAll("Message.txt", new Factory<Message>() {
			public Message create() {
				return new Message();
			}
		});
	}

	void readProgramNotice() {
		programNoticeMgr.readAll("ProgramNotice.txt", new Factory<ProgramNotice>() {
			public ProgramNotice create() {
				return new ProgramNotice();
			}
		});
	}

	// 로그인 단계
	void login() {
		GUIController frameEngine = new GUIController();
		frameEngine.mainFrame(); // GUI 프레임 생성

		System.out.println("\n==================== 로그인 ====================\n");
		String role = null;
		String id = null;
		String pw = null;
		User user = null;
		while (true) { // 직업 체크
			System.out.print("직업 (관리자:A / 교수:P / 학생:S) : ");
			role = scan.next();
			if (role.contentEquals("A") || role.contentEquals("P") || role.contentEquals("S") || role.contentEquals("a")
					|| role.contentEquals("p") || role.contentEquals("s"))
				break;
			System.out.println("잘못 입력하셨습니다.");
			continue;
		}
		while (true) { // 아이디, 패스워드 체크
			System.out.print("아이디 : ");
			id = scan.next();
			if (role.contentEquals("A") || role.contentEquals("a"))
				user = (Admin) University.adminMgr.find(id, false);
			else if (role.contentEquals("P") || role.contentEquals("p"))
				user = (Professor) University.professorMgr.find(id, false);
			else
				user = (Student) University.studentMgr.find(id, false);
			if (user == null) {
				System.out.println("해당하는 계정이 없습니다.");
				continue;
			}
			System.out.print("패스워드 : ");
			pw = scan.next();
			if (user.getPw().contentEquals(pw)) {
				System.out.println("로그인 성공");
				userMenu(role, user); // user의 사용자 메뉴
				break;
			} else {
				System.out.println("비밀번호가 틀렸습니다.");
				continue;
			}
		}
	}

	// 사용자 메뉴
	private void userMenu(String role, User user) {
		if (role.contentEquals("A") || role.contentEquals("a")) { // 관리자 메뉴
			Admin admUser = (Admin) user;
			while (true) {
				System.out.println("\n==================== 관리자 메뉴 ====================\n");
				System.out.println("1) 과목 관리   2) 회원 관리   3) 컨텐츠 관리   0) 종료");
				System.out.print(">> ");
				int choice = 0;
				while (true) {
					try {
						choice = scan.nextInt();
						break;
					} catch (Exception e) {
						scan = new Scanner(System.in);
						System.out.println("\n====== 정수가아닙니다. ======\n");
						System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
						System.out.print(">> ");
					}
				}
				Lecture lec = null;
				if (choice == 0)
					break;
				// 과목 관리
				else if (choice == 1) {
					System.out.println("\n========== 과목 관리 ==========\n");
					adminPrintLecture(admUser);
					System.out.println("1) 과목 추가   2) 과목 삭제   3) 과목 검색   0) 종료");
					int LecChoice;
					while (true) {
						try {
							LecChoice = scan.nextInt();
							break;
						} catch (Exception e) {
							scan = new Scanner(System.in);
							System.out.println("\n====== 정수가아닙니다. ======\n");
							System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
							System.out.print(">> ");
						}
					}
					if (LecChoice == 0)
						break;
					if (LecChoice == 1) {
						adminAddLecture(lec);
					} else if (LecChoice == 2) {
						adminDeleteLecture();
					} else if (LecChoice == 3) {
						adminSearchLecture(scan);
					}
				}
				// 회원 관리
				else if (choice == 2) {
					System.out.println("\n========== 회원 관리 ==========\n");
					adminPrintUser(admUser);
					System.out.println("1) 교수 관리   2) 학생 관리 ");
					int UserChoice;
					while (true) {
						try {
							UserChoice = scan.nextInt();
							break;
						} catch (Exception e) {
							scan = new Scanner(System.in);
							System.out.println("\n====== 정수가아닙니다. ======\n");
							System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
							System.out.print(">> ");
						}
					}
					// 교수 관리
					if (UserChoice == 1) {
						System.out.println("1) 교수 삭제   2) 교수 검색 ");
						int ProChoice;
						while (true) {
							try {
								ProChoice = scan.nextInt();
								break;
							} catch (Exception e) {
								scan = new Scanner(System.in);
								System.out.println("\n====== 정수가아닙니다. ======\n");
								System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
								System.out.print(">> ");
							}
						}
						if (ProChoice == 1) {
							adminDeleteProfessor();
						} else {
							adminSearchProfessor(scan);
						}
					}
					// 학생 관리
					else {
						System.out.println("1) 학생 정보 삭제   2) 학생 검색 ");
						int StuChoice;
						while (true) {
							try {
								StuChoice = scan.nextInt();
								break;
							} catch (Exception e) {
								scan = new Scanner(System.in);
								System.out.println("\n====== 정수가아닙니다. ======\n");
								System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
								System.out.print(">> ");
							}
						}
						if (StuChoice == 1) {
							adminDeleteStudent();
						} else {
							adminSearchStudent(scan);
						}
					}
				}
				// 컨텐츠 관리
				else if (choice == 3) {
					System.out.println("\n========== 컨텐츠 관리 ==========\n");
					adminPrintContent(admUser);
					System.out.println("");
				} else {
					System.out.println("잘못 입력하였습니다.");
				}
			}
		} else if (role.contentEquals("P") || role.contentEquals("p")) { // 교수 메뉴
			Professor pUser = (Professor) user;
			while (true) {
				System.out.println("\n==================== 교수 메뉴 ====================\n");
				System.out.println("\n========== 강의 목록 ==========\n");
				professorPrintLecture(pUser);
				System.out.println("\n과목 선택(과목 코드 입력)");
				System.out.print(">> ");
				String code = scan.next();
				Lecture lec = null;
				for (Lecture lecture : pUser.getProfessorLectureList())
					if (lecture.getCode().contentEquals(code)) {
						lec = lecture;
						break;
					}
				if (lec == null) {
					System.out.println("잘못 입력하셨습니다.");
					continue;
				}
				while (true) {
					System.out.println("\n========== 컨텐츠 선택 ==========\n");
					System.out.println("1) 강의 영상   2) 공지   3) 과제   4) 시험   0) 이전 메뉴");
					System.out.print(">> ");
					int choice;
					while (true) {
						try {
							choice = scan.nextInt();
							break;
						} catch (Exception e) {
							scan = new Scanner(System.in);
							System.out.println("\n====== 정수가아닙니다. ======\n");
							System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
							System.out.print(">> ");
						}
					}
					if (choice == 0)
						break;
					else if (choice == 1 || choice == 2 || choice == 3 || choice == 4) {
						while (true) {
							professorPrintLectureContent(lec, choice);
							System.out.println("\n========== 작업 선택 ==========\n");
							System.out.println("1) 등록   2) 수정   3) 삭제   0) 이전 메뉴");
							System.out.print(">> ");
							int second_choice;
							while (true) {
								try {
									second_choice = scan.nextInt();
									break;
								} catch (Exception e) {
									scan = new Scanner(System.in);
									System.out.println("\n====== 정수가아닙니다. ======\n");
									System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
									System.out.print(">> ");
								}
							}
							if (second_choice == 0)
								break;
							else if (second_choice == 1) {
								professorAddLectureContent(lec, choice);
							} else if (second_choice == 2) {
								professorEditLectureContent(lec, choice);
							} else if (second_choice == 3) {
								professorDeleteLectureContent(lec, choice);
							} else {
								System.out.println("잘못 입력하셨습니다.");
							}
						}
					} else
						System.out.println("잘못 입력하셨습니다.");
				}
			}
		} else { // 학생 메뉴
			Student stUser = (Student) user;
			while (true) {
				System.out.println("\n==================== 학생 메뉴 ====================\n");
				System.out.println("1) 과목 조회   2) 전체 일정");
				System.out.print(">> ");
				int choice;
				while (true) {
					try {
						choice = scan.nextInt();
						break;
					} catch (Exception e) {
						scan = new Scanner(System.in);
						System.out.println("\n====== 정수가아닙니다. ======\n");
						System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
						System.out.print(">> ");
					}
				}
				if (choice == 1) {
					System.out.println("\n========== 과목 조회 ==========\n");
					studentLecture(stUser);
				} else if (choice == 2) {
					System.out.println("\n========== 전체 일정 ==========\n");
					studentSchedule(stUser);
				} else if (choice == 0)
					break;
				else {
					System.out.println("잘못 입력하였습니다.");
				}
			}
		}
	}

	// ========== 관리자 관련 ==========

	public void adminPrintLecture(Admin admin) {
		lectureMgr.printAll();
	}

	public void adminPrintUser(Admin admin) {
		System.out.println("\n========== 관리자 목록 ==========\n");
		adminMgr.printAll();
		System.out.println("\n========== 교수 목록 ==========\n");
		professorMgr.printAll();
		System.out.println("\n========== 학생 목록 ==========\n");
		studentMgr.printAll();
	}

	public void adminPrintContent(Admin admin) {
		System.out.println("\n========== 공지 목록 ==========\n");
		noticeMgr.printAll();
		System.out.println("\n========== 강의 영상 목록 ==========\n");
		videoMgr.printAll();
		System.out.println("\n========== 과제 목록 ==========\n");
		assignmentMgr.printAll();
		System.out.println("\n========== 시험 목록 ==========\n");
		examMgr.printAll();
	}

	public void adminAddLecture(Lecture lec) {
		System.out.println("과목 이름 : ");
		String title = scan.next();
		System.out.println("과목 코드 ( ex)aa111 ) : ");
		String code = scan.next();
		System.out.print("요일 ( ex)월 ) : ");
		String day = scan.next();
		System.out.print("시간 ( ex)123 ) : ");
		String time = scan.next();

		System.out.print("담당 교수 ID : ");
		String professorID = scan.next();
		Professor professor = null;
		professor = (Professor) professorMgr.find(professorID, false);
		if (professor == null) {
			System.out.println("해당하는 교수가 없습니다.");
			return;
		}

		lec = new Lecture(title, code, day, time, professor);
		lectureMgr.read(lec);
		professor.addLecture(lec);
		lectureMgr.printAll();
	}

	public void adminDeleteLecture() {
		int cnt = 1;
		for (Lecture c : lectureMgr.mList) {
			System.out.printf("[%d]", cnt);
			c.print();
			cnt++;
		}
		while (true) {
			System.out.println("삭제하고 싶은 번호를 입력해주세요(종료:0): ");
			int deleteIndex;
			while (true) {
				try {
					deleteIndex = scan.nextInt();
					break;
				} catch (Exception e) {
					scan = new Scanner(System.in);
					System.out.println("\n====== 정수가아닙니다. ======\n");
					System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
					System.out.print(">> ");
				}
			}
			if (deleteIndex == 0)
				break;

			if (1 <= deleteIndex && deleteIndex <= lectureMgr.mList.size()) { // 매니저 클래스에 size 리턴해주는 메소드 만들어서..
				lectureMgr.mList.remove(deleteIndex - 1); // 과목 매니저, 교수 담당 과목리스트, 수강 학생 과목리스트
				System.out.println("성공적으로 삭제되었습니다.");
			} else {
				System.out.println("잘못된 번호 입니다.");
			}

		}
	}

	public void adminSearchLecture(Scanner scan) {
		String kwd;
		while (true) {
			System.out.println("검색 키워드(종료:end): ");
			kwd = scan.next();
			for (Lecture c : lectureMgr.mList) {
				if (c.matches(kwd, true)) {
					c.print();
				}
			}
			if (kwd.equals("end"))
				break;
		}
	}

	public void adminDeleteStudent() {
		int cnt = 1;
		for (Student st : studentMgr.mList) {
			System.out.printf("[%d]", cnt);
			st.print();
			cnt++;
		}
		while (true) {
			System.out.println("삭제하고 싶은 번호를 입력해주세요(종료:0): ");
			int delcnt;
			while (true) {
				try {
					delcnt = scan.nextInt();
					break;
				} catch (Exception e) {
					scan = new Scanner(System.in);
					System.out.println("\n====== 정수가아닙니다. ======\n");
					System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
					System.out.print(">> ");
				}
			}
			if (delcnt == 0)
				break;
			if (1 <= delcnt && delcnt <= studentMgr.mList.size()) {
				studentMgr.mList.remove(delcnt - 1);
				System.out.println("성공적으로 삭제되었습니다.");
			} else {
				System.out.println("잘못된 번호 입니다.");
			}

		}
	}

	public void adminSearchStudent(Scanner scankey) {
		String kwd;
		while (true) {
			System.out.println("검색 키워드(종료:end): ");
			kwd = scan.next();
			for (Student st : studentMgr.mList) {
				if (st.matches(kwd, true)) {
					st.print();
				}
			}
			if (kwd.equals("end"))
				break;
		}
	}

	public void adminDeleteProfessor() {
		int cnt = 1;
		for (Professor pf : professorMgr.mList) {
			System.out.printf("[%d]", cnt);
			pf.print();
			cnt++;
		}
		while (true) {
			System.out.println("삭제하고 싶은 번호를 입력해주세요(종료:0): ");
			int delcnt;
			while (true) {
				try {
					delcnt = scan.nextInt();
					break;
				} catch (Exception e) {
					scan = new Scanner(System.in);
					System.out.println("\n====== 정수가아닙니다. ======\n");
					System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
					System.out.print(">> ");
				}
			}

			if (delcnt == 0)
				break;
			if (1 <= delcnt && delcnt <= professorMgr.mList.size()) {
				professorMgr.mList.remove(delcnt - 1);
				System.out.println("성공적으로 삭제되었습니다.");
			} else {
				System.out.println("잘못된 번호 입니다.");
			}
		}
	}

	public void adminSearchProfessor(Scanner scankey) {
		String kwd;
		while (true) {
			System.out.println("검색 키워드(종료:end): ");
			kwd = scan.next();
			for (Professor pf : professorMgr.mList) {
				if (pf.matches(kwd, true)) {
					pf.print();
				}
			}
			if (kwd.equals("end"))
				break;
		}
	}

	// ========== 교수 관련 ==========

	public void professorPrintLecture(Professor professor) { // 교수의 과목 리스트 출력
		for (Lecture lec : professor.getProfessorLectureList()) {
			lec.printWithoutName();
		}
	}

	public void professorPrintLectureContent(Lecture lec, int choice) { // 과목의 컨텐츠 리스트 출력
		if (choice == 1) {
			System.out.println("\n========== 강의 영상 목록 ==========\n");
			for (Content c : lec.getlectureContentList()) {
				if (c.getContentKey().startsWith("V")) {
					c.print();
				}
			}
		} else if (choice == 2) {
			System.out.println("\n========== 공지 목록 ==========\n");
			for (Content c : lec.getlectureContentList()) {
				if (c.getContentKey().startsWith("N")) {
					c.print();
				}
			}
		} else if (choice == 3) {
			System.out.println("\n========== 과제 목록 ==========\n");
			for (Content c : lec.getlectureContentList()) {
				if (c.getContentKey().startsWith("A")) {
					c.print();
				}
			}
		} else if (choice == 4) {
			System.out.println("\n========== 시험 목록 ==========\n");
			for (Content c : lec.getlectureContentList()) {
				if (c.getContentKey().startsWith("E")) {
					c.print();
				}
			}
		} else
			System.out.println("잘못 입력하셨습니다.");
	}

	public void professorAddLectureContent(Lecture lec, int choice) { // 과목이 갖는 새로운 컨텐츠 작성
		String contentKey = null;
		if (choice == 1) {
			System.out.println("\n========== 강의 영상 작성 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("V")) {
					System.out.println("강의 영상의 컨텐츠 키는 'V'로 시작해야 합니다."); // 컨텐츠 키의 형식 검사
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) { // 입력 받은 컨텐츠 키를 갖는 기존 객체가 있는지 검사
							System.out.println("컨텐츠 키가 중복됩니다.");
							return;
						}
					}
					break;
				}
			}
		} else if (choice == 2) {
			System.out.println("\n========== 공지 작성 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("N")) {
					System.out.println("공지의 컨텐츠 키는 'N'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							System.out.println("컨텐츠 키가 중복됩니다.");
							return;
						}
					}
					break;
				}
			}
		} else if (choice == 3) {
			System.out.println("\n========== 과제 작성 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("A")) {
					System.out.println("과제의 컨텐츠 키는 'A'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							System.out.println("컨텐츠 키가 중복됩니다.");
							return;
						}
					}
					break;
				}
			}
		} else {
			System.out.println("\n========== 시험 작성 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("E")) {
					System.out.println("시험의 컨텐츠 키는 'E'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							System.out.println("컨텐츠 키가 중복됩니다.");
							return;
						}
					}
					break;
				}
			}
		}
		scan.nextLine();
		String code = lec.getCode();
		System.out.print("<날짜> : ");
		String strDate = scan.next();
		Date date = StringToDate(strDate);
		scan.nextLine();
		System.out.print("<내용> : ");
		String body = scan.nextLine();

		System.out.println("\n========== 작성 완료 ==========\n");
		if (choice == 1) {
			Video c = new Video(code, contentKey, date, body); // 입력 받은 정보들을 바탕으로 새로운 객체 생성
			videoMgr.read(c); // 해당 객체를 매니저의 mList에 추가
			videoMgr.printAll();
			lec.addContent(c);
			// (데이터 파일 수정)
		} else if (choice == 2) {
			Notice c = new Notice(code, contentKey, date, body);
			noticeMgr.read(c);
			noticeMgr.printAll();
			lec.addContent(c);
			// (데이터 파일 수정)
		} else if (choice == 3) {
			Assignment c = new Assignment(code, contentKey, date, body);
			assignmentMgr.read(c);
			assignmentMgr.printAll();
			lec.addContent(c);
			// (데이터 파일 수정)
		} else {
			Exam c = new Exam(code, contentKey, date, body);
			examMgr.read(c);
			examMgr.printAll();
			lec.addContent(c);
			// (데이터 파일 수정)
		}
	}

	public void professorEditLectureContent(Lecture lec, int choice) { // 과목이 갖고 있는 기존 컨텐츠 정보 수정
		String contentKey = null;
		Content editContent = null;
		if (choice == 1) {
			System.out.println("\n========== 강의 영상 수정 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("V")) {
					System.out.println("강의 영상의 컨텐츠 키는 'V'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							editContent = ct; // 과목의 lectureContentList에서 contentKey가 같은 객체를 찾아 editContent에 저장
						}
					}
					if (editContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 강의 영상이 없습니다.");
						return;
					}
				}
				break;
			}
		} else if (choice == 2) {
			System.out.println("\n========== 공지 수정 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("N")) {
					System.out.println("공지의 컨텐츠 키는 'N'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							editContent = ct;
						}
					}
					if (editContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 공지가 없습니다.");
						return;
					}
				}
				break;
			}
		} else if (choice == 3) {
			System.out.println("\n========== 과제 수정 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("A")) {
					System.out.println("과제의 컨텐츠 키는 'A'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							editContent = ct;
						}
					}
					if (editContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 과제가 없습니다.");
						return;
					}
				}
				break;
			}
		} else {
			System.out.println("\n========== 시험 수정 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("E")) {
					System.out.println("시험의 컨텐츠 키는 'E'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							editContent = ct;
						}
					}
					if (editContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 시험이 없습니다.");
						return;
					}
				}
				break;
			}
		}
		scan.nextLine();
		System.out.print("<날짜> : ");
		String strDate = scan.next();
		Date date = StringToDate(strDate);
		scan.nextLine();
		System.out.print("<내용> : ");
		String body = scan.nextLine();

		System.out.println("\n========== 수정 완료 ==========\n");
		editContent.setDate(date);
		editContent.setBody(body); // 객체 정보 수정
		if (choice == 1) {
			videoMgr.printAll();
			// (데이터 파일 수정)
		} else if (choice == 2) {
			noticeMgr.printAll();
			// (데이터 파일 수정)
		} else if (choice == 3) {
			assignmentMgr.printAll();
			// (데이터 파일 수정)
		} else {
			examMgr.printAll();
			// (데이터 파일 수정)
		}
	}

	public void professorDeleteLectureContent(Lecture lec, int choice) { // 과목이 갖고 있는 기존 컨텐츠 삭제
		String contentKey = null;
		Content deleteContent = null;
		if (choice == 1) {
			System.out.println("\n========== 강의 영상 삭제 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("V")) {
					System.out.println("강의 영상의 컨텐츠 키는 'V'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							deleteContent = ct; // 과목의 lectureContentList에서 contentKey가 같은 객체를 찾아 deleteContent에 저장
						}
					}
					if (deleteContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 강의 영상이 없습니다.");
						return;
					}
				}
				break;
			}
		}

		else if (choice == 2) {
			System.out.println("\n========== 공지 삭제 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("N")) {
					System.out.println("공지의 컨텐츠 키는 'N'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							deleteContent = ct;
						}
					}
					if (deleteContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 공지가 없습니다.");
						return;
					}
				}
				break;
			}
		} else if (choice == 3) {
			System.out.println("\n========== 과제 삭제 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("A")) {
					System.out.println("과제의 컨텐츠 키는 'A'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							deleteContent = ct;
						}
					}
					if (deleteContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 과제가 없습니다.");
						return;
					}
				}
				break;
			}
		} else {
			System.out.println("\n========== 시험 삭제 ==========\n");
			while (true) {
				System.out.print("<컨텐츠 키> (강의 영상:V, 공지:N, 과제:A, 시험:E) : ");
				contentKey = scan.next();
				if (!contentKey.startsWith("E")) {
					System.out.println("시험의 컨텐츠 키는 'E'로 시작해야 합니다.");
					return;
				} else {
					for (Content ct : lec.getlectureContentList()) {
						if (ct.getContentKey().contentEquals(contentKey)) {
							deleteContent = ct;
						}
					}
					if (deleteContent == null) {
						System.out.println("해당하는 컨텐츠 키를 갖는 시험이 없습니다.");
						return;
					}
				}
				break;
			}
		}
		System.out.println("\n========== 삭제 완료 ==========\n");
		lec.deleteContent(deleteContent); // 과목의 lectureContentList에서 해당 객체 삭제
		if (choice == 1) {
			Video dc = (Video) deleteContent;
			videoMgr.delete(dc); // 매니저의 mList에서 해당 객체 삭제
			videoMgr.printAll();
			// (데이터 파일 수정)
		} else if (choice == 2) {
			Notice dc = (Notice) deleteContent;
			noticeMgr.delete(dc);
			noticeMgr.printAll();
			// (데이터 파일 수정)
		} else if (choice == 3) {
			Assignment dc = (Assignment) deleteContent;
			assignmentMgr.delete(dc);
			assignmentMgr.printAll();
			// (데이터 파일 수정)
		} else {
			Exam dc = (Exam) deleteContent;
			examMgr.delete(dc);
			examMgr.printAll();
			// (데이터 파일 수정)
		}
	}

	// ========== 학생 관련 ==========

	public void studentLecture(Student student) { // 과목조회 메뉴
		int i = 0;
		int choice = 0;
		for (Lecture lec : student.getStudentLectureList()) {
			System.out.printf("(%d) <%s>\n", ++i, lec.getTitle());
		}
		System.out.println("========== 원하는 과목번호를 입력하세요 ==========");
		while (true) {
			int choice1;
			while (true) {
				try {
					choice1 = scan.nextInt();
					break;
				} catch (Exception e) {
					scan = new Scanner(System.in);
					System.out.println("\n====== 정수가아닙니다. ======\n");
					System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
					System.out.print(">> ");
				}
			}
			if (choice1 > student.getStudentLectureList().size())
				System.out.println("잘못입력하셨습니다 , 다시입력하세요 .");
			else {
				choice = choice1;
				break;
			}
		}
		System.out.println("========== 원하는 메뉴를 입력하세요 ==========\n");
		System.out.println("(0)이전메뉴 (1)강의 (2)공지 (3)과제 (4)시험\n");
		int choice2;
		while (true) {
			try {
				choice2 = scan.nextInt();
				break;
			} catch (Exception e) {
				scan = new Scanner(System.in);
				System.out.println("\n====== 정수가아닙니다. ======\n");
				System.out.println("\n====== 정수만 다시 입력하세요 ======\n");
				System.out.print(">> ");
			}
		}
		Lecture lec = student.getLecturebyindex(choice);
		String code1 = lec.getCode();
		switch (choice2) {
		case (0):
			studentLecture(student);
			break;
		case (1):
			for (Content c : lec.getlectureContentList()) {
				if (c.matches(code1, false) && c.contentKey.contains("V")) {
					System.out.println("\n========== 강의 영상 목록 ==========\n");
					c.print();
				}
			}
			break;
		case (2):
			for (Content c : lec.getlectureContentList()) {
				if (c.matches(code1, false) && c.contentKey.contains("N")) {
					System.out.println("\n========== 공지 목록 ==========\n");
					c.print();
				}
			}
			break;
		case (3):
			for (Content c : lec.getlectureContentList()) {
				if (c.matches(code1, false) && c.contentKey.contains("A")) {
					System.out.println("\n========== 과제 목록 ==========\n");
					c.print();
				}
			}
			break;
		case (4):
			for (Content c : lec.getlectureContentList()) {
				if (c.matches(code1, false) && c.contentKey.contains("E")) {
					System.out.println("\n========== 시험 목록 ==========\n");
					c.print();
				}
			}
			break;
		default:
			System.out.println("잘못입력하셨습니다.");
			break;
		}
	}

	public void studentSchedule(Student student) {
		for (Lecture lec : student.getStudentLectureList()) {
			System.out.printf("<%s>\n", lec.getTitle());
			for (Content c : lec.getlectureContentList())
				c.print();
		}
	}

	public static boolean isNotDateType(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			return true;
		}
		return false;
	}

	// TODO
	public static Date StringToDate(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public static String DateToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String strDate = dateFormat.format(date);

		return strDate;
	}

	public static void main(String[] args) {
		University univ = new University();
		univ.run();
	}

}
