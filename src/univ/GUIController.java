package univ;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import lecture.Lecture;
import lectureContent.Assignment;
import lectureContent.Content;
import lectureContent.Exam;
import lectureContent.Notice;
import lectureContent.Video;
import mgr.Manageable;

import java.util.ArrayList;

import user.Professor;
import user.Student;
import user.User;
import userContent.Message;
import userContent.ProgramNotice;
import userContent.Todo;

public class GUIController {

	// 기본 프레임 설정
	public JFrame initFrame() {
		JFrame frame = new JFrame("대학생알림e");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 800);
		ImageIcon icon = new ImageIcon("image/icon.png");
		frame.setIconImage(icon.getImage());

		return frame;
	}

	// 기본 프레임 생성 후 login 패널로 이동
	public void mainFrame() {
		JFrame frame = initFrame(); /// 테스트입니다
		login(frame); /// 테스트입니다
	}

	public void paint(Graphics g, Image background) {// 그리는 함수
		g.drawImage(background, 0, 0, null);// background를 그려줌
	}

// TODO
	// 로그인
	public JPanel login(JFrame frame) {

		JPanel loginPanel = makePanel("image/login.png");
		loginPanel.setLayout(null);

		makeLabel(400, 350, 50, 20, "ID", loginPanel);
		makeLabel(400, 400, 50, 20, "PW", loginPanel);

		JTextField textfieldID = makeTextfield(450, 350, 150, 20, loginPanel, null);

		JPasswordField textfieldPW = new JPasswordField();
		textfieldPW.setBounds(450, 400, 150, 20);
		loginPanel.add(textfieldPW);

		JButton loginBtn = makeButton(620, 350, 80, 70, "Login", loginPanel);
		JButton joinBtn = makeButton(500, 550, 100, 20, "회원가입", loginPanel);

		showProgramNotice(frame, loginPanel);
		showPanel(frame, loginPanel);

		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = textfieldID.getText(); // id fetch
				String pw = new String(textfieldPW.getPassword()); // pw fetch
				User user = null;

				if (University.userMgr.find(id, false) != null)
					user = University.userMgr.find(id, false);

				if (user == null)
					JOptionPane.showMessageDialog(loginPanel, "로그인 실패", "login failed",
							JOptionPane.INFORMATION_MESSAGE);
				else if (user.getPw().contentEquals(pw)) {
					JOptionPane.showMessageDialog(loginPanel, "로그인 성공", "login successed",
							JOptionPane.INFORMATION_MESSAGE);
					if (University.adminMgr.find(id, false) != null)
						adminBasicMenu(frame, user); // 관리자 메뉴로 전환
					else if (University.professorMgr.find(id, false) != null)
						professorBasicMenu(frame, user); // 교수 메뉴로 전환
					else if (University.studentMgr.find(id, false) != null)
						studentBasicMenu(frame, user); // 학생 메뉴로 전환
				} else
					JOptionPane.showMessageDialog(loginPanel, "로그인 실패", "login failed",
							JOptionPane.INFORMATION_MESSAGE);
			}
		});

		joinBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				join(frame);
			}
		});

		return loginPanel;
	}

	// 회원가입
	private JPanel join(JFrame frame) {
		JPanel joinPanel = makePanel("image/signup.png");
		joinPanel.setLayout(null);

		makeLabel(350, 250, 120, 20, "직업(교수:P, 학생:S)", joinPanel);
		makeLabel(350, 280, 120, 20, "ID", joinPanel);
		makeLabel(350, 310, 120, 20, "PW", joinPanel);
		makeLabel(350, 340, 120, 20, "이름", joinPanel);
		makeLabel(350, 370, 120, 20, "나이", joinPanel);
		makeLabel(350, 400, 120, 20, "전화번호", joinPanel);

		JTextField textfieldRole = makeTextfield(500, 250, 150, 20, joinPanel, null);
		JTextField textfieldID = makeTextfield(500, 280, 150, 20, joinPanel, null);
		JTextField textfieldPW = makeTextfield(500, 310, 150, 20, joinPanel, null);
		JTextField textfieldName = makeTextfield(500, 340, 150, 20, joinPanel, null);
		JTextField textfieldAge = makeTextfield(500, 370, 150, 20, joinPanel, null);
		JTextField textfieldPhone = makeTextfield(500, 400, 150, 20, joinPanel, null);

		changePanel(frame, joinPanel, login(frame));

		JButton addUserBtn = makeButton(500, 450, 100, 20, "등록", joinPanel);
		showProgramNotice(frame, joinPanel);
		showPanel(frame, joinPanel);
		addUserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String role = textfieldRole.getText();
				String id = textfieldID.getText();
				String pw = textfieldPW.getText();
				String name = textfieldName.getText();
				String stringAge = textfieldAge.getText();
				String phone = textfieldPhone.getText();

				if (role.contentEquals("")) {
					JOptionPane.showMessageDialog(joinPanel, "직업을 입력해주세요.", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (!role.contentEquals("P") && !role.contentEquals("S")) {
					JOptionPane.showMessageDialog(joinPanel, "직업을 제대로 입력해주세요. (P 또는 S)", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (id.contentEquals("")) {
					JOptionPane.showMessageDialog(joinPanel, "아이디를 입력해주세요.", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (pw.contentEquals("")) {
					JOptionPane.showMessageDialog(joinPanel, "비밀번호를 입력해주세요.", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (name.contentEquals("")) {
					JOptionPane.showMessageDialog(joinPanel, "이름을 입력해주세요.", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (stringAge.contentEquals("")) {
					JOptionPane.showMessageDialog(joinPanel, "나이를 입력해주세요.", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (phone.contentEquals("")) {
					JOptionPane.showMessageDialog(joinPanel, "전화번호를 입력해주세요.", "Join failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					int age = -1;
					try {
						age = Integer.parseInt(stringAge);
					} catch (Exception e1) {

					}
					if (age == -1) {
						JOptionPane.showMessageDialog(joinPanel, "나이는 정수만 입력해주세요.", "Join failed",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						User user = null;
						user = University.userMgr.find(id, false);
						if (user != null) {
							JOptionPane.showMessageDialog(joinPanel, "해당 ID를 갖는 유저가 이미 존재합니다.", "Join failed",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							if (role.contentEquals("P")) {
								Professor professor = new Professor(role, id, pw, name, age, phone);
								University.userMgr.read(professor);
								University.professorMgr.read(professor);
							} else {
								Student student = new Student(role, id, pw, name, age, phone);
								University.userMgr.read(student);
								University.studentMgr.read(student);
							}
							JOptionPane.showMessageDialog(joinPanel, "회원가입 성공", "Join succeed",
									JOptionPane.INFORMATION_MESSAGE);
							login(frame);
						}
					}
				}
			}
		});

		return joinPanel;
	}

//TODO
	// ========== 관리자 관련 ==========

	// 관리자 - 기본 메뉴
	private JPanel adminBasicMenu(JFrame frame, User user) {
		JPanel adminBasicMenuPanel = makePanel("image/admin.png");
		adminBasicMenuPanel.setLayout(null);

		JButton manageDataBtn = makeButton(320, 300, 180, 40, "데이터 관리", adminBasicMenuPanel);
		JButton NoticeBtn = makeButton(600, 300, 180, 40, "프로그램 공지사항 관리", adminBasicMenuPanel);

		manageDataBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adminManageData(frame, user);
			}
		});
		NoticeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adminManageNotice(frame, user);
			}
		});

		makeLabel(385, 550, 100, 30, user.getName() + " 님 ", adminBasicMenuPanel);
		logout(frame, adminBasicMenuPanel);
		myPageButton(frame, adminBasicMenuPanel, user);
		showProgramNotice(frame, adminBasicMenuPanel);
		showPanel(frame, adminBasicMenuPanel);

		return adminBasicMenuPanel;
	}

	// 관리자 - 데이터 관리
	private JPanel adminManageData(JFrame frame, User user) {
		JPanel adminManageDataPanel = makePanel("image/admindata.png");
		adminManageDataPanel.setLayout(null);

		JTabbedPane jtab = makeTab(adminManageDataPanel);
		jtab.addTab("과목", adminManageLectureTab(frame, user));
		jtab.addTab("유저", adminManageUserTab(frame, user));
		jtab.addTab("컨텐츠", adminManageContentTab(frame, user));
		jtab.setBackground(new Color(255, 150, 150));

		changePanel(frame, adminManageDataPanel, adminBasicMenu(frame, user));
		showProgramNotice(frame, adminManageDataPanel);
		showPanel(frame, adminManageDataPanel);

		return adminManageDataPanel;
	}

	// 관리자 - 데이터 관리 - 과목 탭
	public JPanel adminManageLectureTab(JFrame frame, User user) {
		JPanel adminManageLectureTabPanel = makePanel("image/null.png");

		ArrayList<Lecture> list = University.lectureMgr.mList;
		TableController tc = new TableController();
		String field[] = { "과목명", "과목코드", "요일", "시간" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(adminManageLectureTabPanel);
		tc.createTable(list, true);
		tc.setList(list);

		JTable table = tc.getTable();

		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Lecture lecture = (Lecture) tc.getList().get(row);
					String[] answer = { "수정", "삭제" };
					int result = JOptionPane.showOptionDialog(adminManageLectureTabPanel,
							lecture.getTitle() + "  " + lecture.getCode() + "  " + lecture.getDay() + "  "
									+ lecture.getTime(),
							"choose menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.YES_OPTION) {
						adminEditLecture(frame, lecture, user);
					} else if (result == JOptionPane.NO_OPTION) {
						Professor pf = lecture.getProfessor();
						University.lectureMgr.delete(lecture); // 과목 매니저
						pf.deleteLecture(lecture); // 담당 교수
						for (Student st : lecture.getRegisterStudentList()) { // 수강 학생
							st.deleteStudentLecture(lecture);
						}
						System.out.println(pf.getId());
						JOptionPane.showMessageDialog(adminManageLectureTabPanel, "과목이 삭제되었습니다.",
								"Delete lecture successed", JOptionPane.INFORMATION_MESSAGE);
						adminManageData(frame, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		adminAddLectureBtn(frame, adminManageLectureTabPanel, user);
		searchTable(adminManageLectureTabPanel, tc, tableModel, list, true);
		showPanel(frame, adminManageLectureTabPanel);

		return adminManageLectureTabPanel;
	}

	// 관리자 - 데이터 관리 - 과목 수정
	private JPanel adminEditLecture(JFrame frame, Lecture lecture, User user) {
		JPanel adminEditLecturePanel = makePanel("image/adminlecture.png");
		adminEditLecturePanel.setLayout(null);

		makeLabel(500, 240, 100, 20, "과목 변경", adminEditLecturePanel);

		makeLabel(400, 290, 60, 20, "과목명", adminEditLecturePanel);
		makeLabel(400, 320, 60, 20, "과목코드", adminEditLecturePanel);
		makeLabel(400, 350, 60, 20, "요일", adminEditLecturePanel);
		makeLabel(400, 380, 60, 20, "시간", adminEditLecturePanel);

		JTextField textfieldTitle = makeTextfield(500, 290, 150, 20, adminEditLecturePanel, lecture.getTitle());
		JTextField textfieldCode = makeTextfield(500, 320, 150, 20, adminEditLecturePanel, lecture.getCode());
		JTextField textfieldDay = makeTextfield(500, 350, 150, 20, adminEditLecturePanel, lecture.getDay());
		JTextField textfieldTime = makeTextfield(500, 380, 150, 20, adminEditLecturePanel, lecture.getTime());

		changePanel(frame, adminEditLecturePanel, adminManageData(frame, user));

		JButton editLectureBtn = makeButton(510, 470, 80, 20, "변경", adminEditLecturePanel);
		showProgramNotice(frame, adminEditLecturePanel);
		showPanel(frame, adminEditLecturePanel);
		editLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = textfieldTitle.getText();
				String code = textfieldCode.getText();
				String day = textfieldDay.getText();
				String time = textfieldTime.getText();

				if (title.contentEquals("")) {
					JOptionPane.showMessageDialog(adminEditLecturePanel, "과목명을 입력해주세요.", "Edit lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (code.contentEquals("")) {
					JOptionPane.showMessageDialog(adminEditLecturePanel, "과목코드를 입력해주세요.", "Edit lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(adminEditLecturePanel, "요일을 입력해주세요.", "Edit lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (time.contentEquals("")) {
					JOptionPane.showMessageDialog(adminEditLecturePanel, "시간을 입력해주세요.", "Edit lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 과목코드를 변경하였고, 해당 과목코드를 갖는 과목이 이미 있는 경우
					if (!lecture.getCode().contentEquals(code) && University.lectureMgr.find(code, false) != null) {
						JOptionPane.showMessageDialog(adminEditLecturePanel, "해당 코드를 갖는 과목이 이미 존재합니다.",
								"Edit lecture failed", JOptionPane.INFORMATION_MESSAGE);
					} else {
						lecture.setTitle(title);
						lecture.setCode(code);
						lecture.setDay(day);
						lecture.setTime(time);
						JOptionPane.showMessageDialog(adminEditLecturePanel, "변경 성공", "Edit lecture successed",
								JOptionPane.INFORMATION_MESSAGE);
						adminManageData(frame, user);

					}
				}
			}
		});

		return adminEditLecturePanel;
	}

	// 관리자 - 데이터 관리 - 과목 추가 버튼
	private void adminAddLectureBtn(JFrame frame, JPanel panel, User user) {
		JButton adminAddLectureBtn = new JButton("과목 추가");
		adminAddLectureBtn.setBounds(100, 100, 100, 20);
		adminAddLectureBtn.setBackground(new Color(232, 232, 232));
		panel.add(adminAddLectureBtn);
		adminAddLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adminAddLecture(frame, user);
			}
		});
	}

	// 관리자 - 데이터 관리 - 과목 추가
	private JPanel adminAddLecture(JFrame frame, User user) {
		JPanel adminAddLecturePanel = makePanel("image/adminlecture.png");
		adminAddLecturePanel.setLayout(null);

		makeLabel(500, 240, 100, 20, "과목 추가", adminAddLecturePanel);

		makeLabel(400, 290, 90, 20, "과목명", adminAddLecturePanel);
		makeLabel(400, 320, 90, 20, "과목코드", adminAddLecturePanel);
		makeLabel(400, 350, 90, 20, "요일", adminAddLecturePanel);
		makeLabel(400, 380, 90, 20, "시간", adminAddLecturePanel);
		makeLabel(400, 410, 90, 20, "담당 교수 id", adminAddLecturePanel);

		JTextField textfieldTitle = makeTextfield(500, 290, 150, 20, adminAddLecturePanel, null);
		JTextField textfieldCode = makeTextfield(500, 320, 150, 20, adminAddLecturePanel, null);
		JTextField textfieldDay = makeTextfield(500, 350, 150, 20, adminAddLecturePanel, null);
		JTextField textfieldTime = makeTextfield(500, 380, 150, 20, adminAddLecturePanel, null);
		JTextField textfieldProfessorName = makeTextfield(500, 410, 150, 20, adminAddLecturePanel, null);

		changePanel(frame, adminAddLecturePanel, adminManageData(frame, user));

		JButton addLectureBtn = makeButton(510, 470, 80, 20, "추가", adminAddLecturePanel);
		showProgramNotice(frame, adminAddLecturePanel);
		showPanel(frame, adminAddLecturePanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = textfieldTitle.getText();
				String code = textfieldCode.getText();
				String day = textfieldDay.getText();
				String time = textfieldTime.getText();
				String professorID = textfieldProfessorName.getText();

				if (title.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddLecturePanel, "과목명을 입력해주세요.", "Add lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (code.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddLecturePanel, "과목코드를 입력해주세요.", "Add lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddLecturePanel, "요일을 입력해주세요.", "Add lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (time.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddLecturePanel, "시간을 입력해주세요.", "Add lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (professorID.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddLecturePanel, "교수 이름을 입력해주세요.", "Add lecture failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					Professor professor = null;
					professor = University.professorMgr.find(professorID, false);
					if (professor == null) {
						JOptionPane.showMessageDialog(adminAddLecturePanel, "해당하는 교수가 없습니다.", "Add lecture failed",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						Lecture lec = new Lecture(title, code, day, time, professor); // 위의 조건 통과 시 객체 생성
						if (University.lectureMgr.find(code, false) != null) {
							JOptionPane.showMessageDialog(adminAddLecturePanel, "해당 코드를 갖는 과목이 이미 존재합니다.",
									"Add lecture failed", JOptionPane.INFORMATION_MESSAGE);
						} else {
							University.lectureMgr.read(lec);
							professor.addLecture(lec);
							JOptionPane.showMessageDialog(adminAddLecturePanel, "추가 성공", "Add lecture successed",
									JOptionPane.INFORMATION_MESSAGE);
							adminManageData(frame, user);
						}
					}
				}
			}
		});

		return adminAddLecturePanel;
	}

	// 관리자 - 데이터 관리 - 유저 탭
	private JPanel adminManageUserTab(JFrame frame, User user) {
		JPanel adminManageUserTabPanel = makePanel("image/null.png");

		ArrayList<User> list = University.userMgr.mList;
		TableController tc = new TableController();
		String field[] = { "직업", "아이디", "이름" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(adminManageUserTabPanel);
		tc.createTable(list, true);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 이벤트
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					User deleteUser = (User) tc.getList().get(row);
					String[] answer = { "삭제" };
					int result = JOptionPane.showOptionDialog(adminManageUserTabPanel,
							deleteUser.getRole() + "  " + deleteUser.getId() + "  " + deleteUser.getName() + "  "
									+ deleteUser.getAge() + "  " + deleteUser.getPhone(),
							"choose menu", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.OK_OPTION) {
						University.userMgr.delete(deleteUser); // 관리자 메뉴 - 유저 삭제
						JOptionPane.showMessageDialog(adminManageUserTabPanel, "유저가 삭제되었습니다.", "Delete user successed",
								JOptionPane.INFORMATION_MESSAGE);
						adminManageData(frame, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(adminManageUserTabPanel, tc, tableModel, list, true);
		showPanel(frame, adminManageUserTabPanel);

		return adminManageUserTabPanel;
	}

	// 관리자 - 데이터 관리 - 컨텐츠 탭
	private JPanel adminManageContentTab(JFrame frame, User user) {
		JPanel adminManageContentTabPanel = makePanel("image/null.png");

		ArrayList<Content> list = University.contentMgr.mList;

		TableController tc = new TableController();
		String field[] = { "과목코드", "컨텐츠 키", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(adminManageContentTabPanel);
		tc.createTable(list, true);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content content = (Content) tc.getList().get(row);
					String[] answer = { "삭제" };
					int result = JOptionPane.showOptionDialog(adminManageContentTabPanel,
							content.getLectureTitle() + "  " + content.getCode() + "  " + content.getContentKey() + "  "
									+ University.DateToString(content.getDate()) + "  " + content.getBody(),
							"choose menu", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.OK_OPTION) {
						University.contentMgr.delete(content); // 관리자 메뉴 - 컨텐츠 삭제
						JOptionPane.showMessageDialog(adminManageContentTabPanel, "컨텐츠가 삭제되었습니다.",
								"Delete content successed", JOptionPane.INFORMATION_MESSAGE);
						adminManageData(frame, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(adminManageContentTabPanel, tc, tableModel, list, true);
		showPanel(frame, adminManageContentTabPanel);

		return adminManageContentTabPanel;
	}

	// 관리자 - 프로그램 공지사항
	private JPanel adminManageNotice(JFrame frame, User user) {
		JPanel adminManageNoticePanel = makePanel("image/adminnotice.png");
		adminManageNoticePanel.setLayout(null);

		JTabbedPane jtab = makeTab(adminManageNoticePanel);
		jtab.addTab("공지사항", adminManageNoticeTab(frame, user));
		jtab.setBackground(new Color(255, 150, 150));

		changePanel(frame, adminManageNoticePanel, adminBasicMenu(frame, user));
		showProgramNotice(frame, adminManageNoticePanel);
		showPanel(frame, adminManageNoticePanel);

		return adminManageNoticePanel;
	}

	// 관리자 - 프로그램 공지사항 탭
	private JPanel adminManageNoticeTab(JFrame frame, User user) {
		JPanel adminManageNoticeTabPanel = makePanel("image/null.png");

		ArrayList<ProgramNotice> list = University.programNoticeMgr.mList;
		TableController tc = new TableController();
		String field[] = { "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(adminManageNoticeTabPanel);
		tc.createTable(list, true);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					ProgramNotice proNtc = (ProgramNotice) tc.getList().get(row);
					String[] answer = { "수정", "삭제" };
					int result = JOptionPane.showOptionDialog(adminManageNoticeTabPanel,
							University.DateToString(proNtc.getDate()) + "  " + proNtc.getBody() + "  ", "choose menu",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer, answer[0]);
					if (result == JOptionPane.YES_OPTION) {
						adminEditNotice(frame, proNtc, user);
					} else if (result == JOptionPane.NO_OPTION) {
						University.programNoticeMgr.delete(proNtc);
						JOptionPane.showMessageDialog(adminManageNoticeTabPanel, "공지사항이 삭제되었습니다.",
								"Delete notice successed", JOptionPane.INFORMATION_MESSAGE);
						adminManageNotice(frame, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		adminAddNoticeBtn(frame, adminManageNoticeTabPanel, user);
		searchTable(adminManageNoticeTabPanel, tc, tableModel, list, true);
		showPanel(frame, adminManageNoticeTabPanel);

		return adminManageNoticeTabPanel;
	}

	// 관리자 - 프로그램 공지사항 - 공지사항 수정
	private JPanel adminEditNotice(JFrame frame, ProgramNotice notice, User user) {
		JPanel adminEditNoticePanel = makePanel("image/adminnoticemng.png");
		adminEditNoticePanel.setLayout(null);

		makeLabel(500, 270, 100, 20, "공지 변경", adminEditNoticePanel);

		makeLabel(400, 320, 60, 20, "작성일", adminEditNoticePanel);
		makeLabel(400, 360, 60, 20, "내용", adminEditNoticePanel);

		JTextField textfieldDate = makeTextfield(500, 320, 150, 20, adminEditNoticePanel,
				University.DateToString(notice.getDate()));
		JTextArea textareaBody = makeTextarea(500, 360, 250, 60, adminEditNoticePanel, notice.getBody());

		changePanel(frame, adminEditNoticePanel, adminManageNotice(frame, user));

		JButton editNoticeBtn = makeButton(510, 450, 80, 20, "변경", adminEditNoticePanel);
		showProgramNotice(frame, adminEditNoticePanel);
		showPanel(frame, adminEditNoticePanel);
		editNoticeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String date = textfieldDate.getText();
				String body = textareaBody.getText();

				if (date.contentEquals("")) {
					JOptionPane.showMessageDialog(adminEditNoticePanel, "작성일를 입력해주세요.", "Edit notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (body.contentEquals("")) {
					JOptionPane.showMessageDialog(adminEditNoticePanel, "내용을 입력해주세요.", "Edit notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(date)) {
					JOptionPane.showMessageDialog(adminEditNoticePanel, "날짜 형식을 확인해주세요.", "Edit notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					notice.setDate(University.StringToDate(date));
					notice.setBody(body);
					JOptionPane.showMessageDialog(adminEditNoticePanel, "변경 성공", "Edit notice successed",
							JOptionPane.INFORMATION_MESSAGE);
					adminManageNotice(frame, user);
				}
			}
		});

		return adminEditNoticePanel;
	}

	// 관리자 - 프로그램 공지사항 - 공지사항 추가 버튼
	private void adminAddNoticeBtn(JFrame frame, JPanel panel, User user) {
		JButton adminAddNoticeBtn = new JButton("공지 추가");
		adminAddNoticeBtn.setBounds(100, 100, 100, 20);
		adminAddNoticeBtn.setBackground(new Color(232, 232, 232));
		panel.add(adminAddNoticeBtn);
		adminAddNoticeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adminAddNotice(frame, user);
			}
		});
	}

	// 관리자 - 프로그램 공지사항 - 공지사항 추가
	private JPanel adminAddNotice(JFrame frame, User user) {
		JPanel adminAddNoticePanel = makePanel("image/adminnoticemng.png");
		adminAddNoticePanel.setLayout(null);

		makeLabel(500, 270, 100, 20, "공지 추가 ", adminAddNoticePanel);

		makeLabel(400, 320, 90, 20, "작성일", adminAddNoticePanel);
		makeLabel(400, 360, 90, 20, "내용", adminAddNoticePanel);

		JTextField textfieldDate = makeTextfield(500, 320, 150, 20, adminAddNoticePanel, null);
		JTextArea textareaBody = makeTextarea(500, 360, 150, 60, adminAddNoticePanel, null);

		changePanel(frame, adminAddNoticePanel, adminManageNotice(frame, user));

		JButton addNoitceBtn = makeButton(510, 450, 80, 20, "추가", adminAddNoticePanel);
		showProgramNotice(frame, adminAddNoticePanel);
		showPanel(frame, adminAddNoticePanel);
		addNoitceBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String date = textfieldDate.getText();
				String body = textareaBody.getText();

				if (date.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddNoticePanel, "작성일를 입력해주세요.", "Add Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (body.contentEquals("")) {
					JOptionPane.showMessageDialog(adminAddNoticePanel, "내용을 입력해주세요.", "Add Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(date)) {
					JOptionPane.showMessageDialog(adminAddNoticePanel, "날짜 형식을 확인해주세요.", "Add Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					ProgramNotice proNtc = new ProgramNotice(University.StringToDate(date), body);
					University.programNoticeMgr.read(proNtc);
					JOptionPane.showMessageDialog(adminAddNoticePanel, "추가 성공", "Add Notice successed",
							JOptionPane.INFORMATION_MESSAGE);
					adminManageNotice(frame, user);

				}
			}
		});

		return adminAddNoticePanel;
	}

//TODO
	// ========== 교수 관련 ==========

	// 교수 - 기본 메뉴
	private JPanel professorBasicMenu(JFrame frame, User user) {
		JPanel professorBasicMenuPanel = makePanel("image/professor.png");
		professorBasicMenuPanel.setLayout(null);

		JButton lectureBtn = makeButton(475, 300, 150, 50, "과목조회", professorBasicMenuPanel);
		logout(frame, professorBasicMenuPanel);
		lectureBtn.addActionListener(new ActionListener() { // 과목조회 버튼
			@Override
			public void actionPerformed(ActionEvent e) {
				professorLecture(frame, user);
			}
		});

		makeLabel(385, 550, 100, 30, user.getName() + " 님 ", professorBasicMenuPanel);
		logout(frame, professorBasicMenuPanel);
		myPageButton(frame, professorBasicMenuPanel, user);
		showProgramNotice(frame, professorBasicMenuPanel);
		showPanel(frame, professorBasicMenuPanel);
		return professorBasicMenuPanel;
	}

	// 교수 - 과목 조회
	private JPanel professorLecture(JFrame frame, User user) {
		JPanel professorLecturePanel = makePanel("image/professorlecture.png");
		professorLecturePanel.setLayout(null);

		JTabbedPane jtab = makeTab(professorLecturePanel);
		jtab.addTab("과목", professorLectureTab(frame, user));

		changePanel(frame, professorLecturePanel, professorBasicMenu(frame, user));
		showProgramNotice(frame, professorLecturePanel);
		showPanel(frame, professorLecturePanel);

		return professorLecturePanel;
	}

	// 교수 - 과목 조회 탭
	private JPanel professorLectureTab(JFrame frame, User user) {
		JPanel professorLectureTabPanel = makePanel("image/null.png");

		Professor p = (Professor) user;

		TableController tc = new TableController();
		String field[] = { "과목명", "과목코드", "요일", "시간" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorLectureTabPanel);
		tc.createTable(p.getProfessorLectureList(), true);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Lecture lec = p.getProfessorLectureList().get(row); // 클릭한 과목에 대한 세부 항목 표시
					String[] answer = { "과목 컨텐츠", "수강자 확인" };
					int result = JOptionPane.showOptionDialog(professorLectureTabPanel, lec.getTitle(), "choose menu",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer, answer[0]);
					if (result == JOptionPane.YES_OPTION) {
						professorLectureContents(frame, lec, user);
					} else if (result == JOptionPane.NO_OPTION) {
						professorCheckRegisteredStudent(frame, lec, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		showPanel(frame, professorLectureTabPanel);

		return professorLectureTabPanel;
	}

	// 교수 - 과목 조회 - 수강자 확인
	private JPanel professorCheckRegisteredStudent(JFrame frame, Lecture lecture, User user) {
		JPanel professorCheckRegisteredStudentPanel = makePanel("image/professorstudents.png"); // 패널을 만듬
		professorCheckRegisteredStudentPanel.setLayout(null);

		JTabbedPane jtab = makeTab(professorCheckRegisteredStudentPanel);
		jtab.addTab("수강자", professorCheckRegisteredStudentTab(frame, lecture, user));

		changePanel(frame, professorCheckRegisteredStudentPanel, professorLecture(frame, user));
		showProgramNotice(frame, professorCheckRegisteredStudentPanel);
		showPanel(frame, professorCheckRegisteredStudentPanel);

		return professorCheckRegisteredStudentPanel;
	}

	// 교수 - 과목 조회 - 수강자 확인 탭
	private JPanel professorCheckRegisteredStudentTab(JFrame frame, Lecture lecture, User user) {
		JPanel professorCheckRegisteredStudentTabPanel = makePanel("image/null.png");

		ArrayList<Student> list = new ArrayList<>();
		list = lecture.getRegisterStudentList();

		TableController tc = new TableController();
		String field[] = { "수강자" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorCheckRegisteredStudentTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();

		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 이벤트
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Student st = (Student) tc.getList().get(row);
					JOptionPane.showMessageDialog(frame, "이름 : " + st.getName() + "\n\n" + "학년 : "
							+ st.getStudentGrade() + "\n\n" + "[번호] : " + st.getPhone(), "학생 정보",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		showPanel(frame, professorCheckRegisteredStudentTabPanel);

		return professorCheckRegisteredStudentTabPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠
	private JPanel professorLectureContents(JFrame frame, Lecture lecture, User user) {
		JPanel professorLectureContentsPanel = makePanel("image/lecturecontent.png"); // 패널을 만듬
		professorLectureContentsPanel.setLayout(null);

		makeLabel(658, 247, 150, 20, lecture.getTitle(), professorLectureContentsPanel);

		JTabbedPane jtab = makeTab(professorLectureContentsPanel);
		jtab.addTab("과제", professorManageAssignmentTab(frame, lecture, user));
		jtab.addTab("공지", professorManageNoticeTab(frame, lecture, user));
		jtab.addTab("시험", professorManageExamTab(frame, lecture, user));
		jtab.addTab("영상", professorManageVideoTab(frame, lecture, user));
		jtab.setBackground(new Color(255, 150, 150));

		changePanel(frame, professorLectureContentsPanel, professorLecture(frame, user));
		showProgramNotice(frame, professorLectureContentsPanel);
		showPanel(frame, professorLectureContentsPanel);

		return professorLectureContentsPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 과제 탭
	private JPanel professorManageAssignmentTab(JFrame frame, Lecture lecture, User user) {
		JPanel professorManageAssignmentTabPanel = makePanel("image/null.png");

		ArrayList<Content> list = new ArrayList<>();
		for (Manageable m : University.assignmentMgr.mList) {
			Content c = (Content) m;
			if (c.getCode().contentEquals(lecture.getCode()))
				list.add(c);
		}

		TableController tc = new TableController();
		String field[] = { "과목이름", "컨텐츠 유형", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorManageAssignmentTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content content = (Content) tc.getList().get(row);
					String[] answer = { "수정", "삭제", "제출자 확인" };
					int result = JOptionPane.showOptionDialog(professorManageAssignmentTabPanel,
							"[과목이름] : " + content.getLectureTitle() + "\n\n" + "[컨텐츠 유형] : "
									+ content.getContentCategory() + "\n\n" + "[작성일] : "
									+ University.DateToString(content.getDate()) + "\n\n" + "[내용]\n" + content.getBody()
									+ "\n\n\n",
							"choose menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					Assignment as = (Assignment) content;
					if (result == JOptionPane.YES_OPTION) {
						professorEditAssignment(frame, lecture, user, content);
					} else if (result == JOptionPane.NO_OPTION) {
						University.contentMgr.delete(content);
						University.assignmentMgr.delete(as);
						lecture.deleteContent(content);
						JOptionPane.showMessageDialog(professorManageAssignmentTabPanel, "과제가 삭제되었습니다.",
								"Delete lecture successed", JOptionPane.INFORMATION_MESSAGE);
						professorLectureContents(frame, lecture, user);
					} else if (result == JOptionPane.CANCEL_OPTION) {
						professorCheckAssignmentSubmitter(frame, lecture, as, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		JButton assignmentAddBtn = makeButton(500, 450, 100, 20, "과제 추가", professorManageAssignmentTabPanel);

		assignmentAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				professorAddAssignment(frame, lecture, user);
			}
		});
		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(professorManageAssignmentTabPanel, tc, tableModel, list, false);
		showPanel(frame, professorManageAssignmentTabPanel);
		

		return professorManageAssignmentTabPanel;

	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 과제 추가
	private JPanel professorAddAssignment(JFrame frame, Lecture lecture, User user) {
		JPanel professorAddAssignmentPanel = makePanel("image/professorassignment.png");
		professorAddAssignmentPanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "과제 추가", professorAddAssignmentPanel);

		makeLabel(400, 300, 90, 20, "과목명", professorAddAssignmentPanel);
		makeLabel(400, 330, 90, 20, "작성일", professorAddAssignmentPanel);
		makeLabel(400, 360, 90, 20, "내용", professorAddAssignmentPanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorAddAssignmentPanel,
				lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorAddAssignmentPanel, null);
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorAddAssignmentPanel, null);

		changePanel(frame, professorAddAssignmentPanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "추가", professorAddAssignmentPanel);

		showProgramNotice(frame, professorAddAssignmentPanel);
		showPanel(frame, professorAddAssignmentPanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddAssignmentPanel, "요일을 입력해주세요.", "Add Assignment failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddAssignmentPanel, "내용을 입력해주세요.", "Add Assignment failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorAddAssignmentPanel, "날짜 형식을 확인해주세요.",
							"Add Assignment failed", JOptionPane.INFORMATION_MESSAGE);
				} else {
					Assignment k = new Assignment(lecture.getCode(), lecture.getNextContentsKey(lecture, 1),
							University.StringToDate(day), text);
					lecture.addContent(k);
					University.assignmentMgr.read(k);
					JOptionPane.showMessageDialog(professorAddAssignmentPanel, "추가 성공", "Add Assignment successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}
		});

		return professorAddAssignmentPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 과제 수정
	private JPanel professorEditAssignment(JFrame frame, Lecture lecture, User user, Content content) {
		JPanel professorEditAssignmentPanel = makePanel("image/professorassignment.png");
		professorEditAssignmentPanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "과제 수정", professorEditAssignmentPanel);

		makeLabel(400, 300, 90, 20, "과목명", professorEditAssignmentPanel);
		makeLabel(400, 330, 90, 20, "작성일", professorEditAssignmentPanel);
		makeLabel(400, 360, 90, 20, "내용", professorEditAssignmentPanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorEditAssignmentPanel,
				lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorEditAssignmentPanel,
				University.DateToString(content.getDate()));
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorEditAssignmentPanel, content.getBody());

		changePanel(frame, professorEditAssignmentPanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "수정", professorEditAssignmentPanel);

		showProgramNotice(frame, professorEditAssignmentPanel);
		showPanel(frame, professorEditAssignmentPanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditAssignmentPanel, "요일을 입력해주세요.", "Edit Assignment failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditAssignmentPanel, "내용을 입력해주세요.", "Edit Assignment failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorEditAssignmentPanel, "날짜 형식을 확인해주세요.",
							"Edit Assignment failed", JOptionPane.INFORMATION_MESSAGE);
				} else {
					content.setDate(University.StringToDate(day));
					content.setBody(text);
					JOptionPane.showMessageDialog(professorEditAssignmentPanel, "변경 성공", "Edit Assignment successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}

		});

		return professorEditAssignmentPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 과제 제출자 확인
	private JPanel professorCheckAssignmentSubmitter(JFrame frame, Lecture lecture, Assignment as, User user) {
		JPanel professorCheckAssignmentSubmitterPanel = makePanel("image/professorcheck.png"); // 패널을 만듬
		professorCheckAssignmentSubmitterPanel.setLayout(null);

		JTabbedPane jtab = makeTab(professorCheckAssignmentSubmitterPanel);
		jtab.addTab("제출자", professorCheckAssignmentSubmitterTab(frame, lecture, as, user));
		jtab.addTab("미제출자", professorCheckAssignmentUnsubmitterTab(frame, lecture, as, user));
		jtab.setBackground(new Color(255, 150, 150));

		changePanel(frame, professorCheckAssignmentSubmitterPanel, professorLectureContents(frame, lecture, user));
		showProgramNotice(frame, professorCheckAssignmentSubmitterPanel);
		showPanel(frame, professorCheckAssignmentSubmitterPanel);

		return professorCheckAssignmentSubmitterPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 과제 제출자 확인 - 제출자 탭
	private JPanel professorCheckAssignmentSubmitterTab(JFrame frame, Lecture lecture, Assignment as, User user) {
		JPanel professorCheckAssignmentSubmitterTabPanel = makePanel("image/null.png");

		ArrayList<Student> list = new ArrayList<>();
		list = as.getSubmittedStudentList();

		TableController tc = new TableController();
		String field[] = { "제출자" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorCheckAssignmentSubmitterTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();

		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 이벤트
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Student st = (Student) tc.getList().get(row);
					JOptionPane.showMessageDialog(frame, "이름 : " + st.getName() + "\n\n" + "학년 : "
							+ st.getStudentGrade() + "\n\n" + "[번호] : " + st.getPhone(), "학생 정보",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		showPanel(frame, professorCheckAssignmentSubmitterTabPanel);

		return professorCheckAssignmentSubmitterTabPanel;

	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 과제 제출자 확인 - 미제출자 탭
	private JPanel professorCheckAssignmentUnsubmitterTab(JFrame frame, Lecture lecture, Assignment as, User user) {
		JPanel professorCheckAssignmentUnsubmitterTabPanel = makePanel("image/null.png");

		ArrayList<Student> list = new ArrayList<>();
		list = as.getUnsubmittedStudentList();

		TableController tc = new TableController();
		String field[] = { "미제출자" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorCheckAssignmentUnsubmitterTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();

		table.setAutoCreateRowSorter(true);

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 이벤트
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Student st = (Student) tc.getList().get(row);
					JOptionPane.showMessageDialog(frame, "이름 : " + st.getName() + "\n\n" + "학년 : "
							+ st.getStudentGrade() + "\n\n" + "[번호] : " + st.getPhone(), "학생 정보",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		showPanel(frame, professorCheckAssignmentUnsubmitterTabPanel);

		return professorCheckAssignmentUnsubmitterTabPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 공지 탭
	private JPanel professorManageNoticeTab(JFrame frame, Lecture lecture, User user) {
		JPanel professorManageNoticeTabPanel = makePanel("image/null.png");

		ArrayList<Content> list = new ArrayList<>();
		for (Manageable m : University.noticeMgr.mList) {
			Content c = (Content) m;
			if (c.getCode().contentEquals(lecture.getCode()))
				list.add(c);
		}

		TableController tc = new TableController();
		String field[] = { "과목이름", "컨텐츠 유형", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorManageNoticeTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content content = (Content) tc.getList().get(row);
					String[] answer = { "수정", "삭제" };
					int result = JOptionPane.showOptionDialog(professorManageNoticeTabPanel,
							"[과목이름] : " + content.getLectureTitle() + "\n\n" + "[컨텐츠 유형] : "
									+ content.getContentCategory() + "\n\n" + "[작성일] : "
									+ University.DateToString(content.getDate()) + "\n\n" + "[내용]\n" + content.getBody()
									+ "\n\n\n",
							"choose menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.YES_OPTION) {
						professorEditNotice(frame, lecture, user, content);
					} else if (result == JOptionPane.NO_OPTION) {
						University.contentMgr.delete(content);
						Notice as = (Notice) content;
						University.noticeMgr.delete(as);
						lecture.deleteContent(content);
						JOptionPane.showMessageDialog(professorManageNoticeTabPanel, "공지가 삭제되었습니다.",
								"Delete lecture successed", JOptionPane.INFORMATION_MESSAGE);
						professorLectureContents(frame, lecture, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		JButton noticeAddBtn = makeButton(500, 450, 100, 20, "공지 추가", professorManageNoticeTabPanel);

		noticeAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				professorAddNotice(frame, lecture, user);
			}
		});
		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(professorManageNoticeTabPanel, tc, tableModel, list, false);
		showPanel(frame, professorManageNoticeTabPanel);

		return professorManageNoticeTabPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 공지 추가
	private JPanel professorAddNotice(JFrame frame, Lecture lecture, User user) {
		JPanel professorAddNoticePanel = makePanel("image/professornotice.png");
		professorAddNoticePanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "공지 추가", professorAddNoticePanel);

		makeLabel(400, 300, 90, 20, "과목명", professorAddNoticePanel);
		makeLabel(400, 330, 90, 20, "작성일", professorAddNoticePanel);
		makeLabel(400, 360, 90, 20, "내용", professorAddNoticePanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorAddNoticePanel, lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorAddNoticePanel, null);
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorAddNoticePanel, null);

		changePanel(frame, professorAddNoticePanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "추가", professorAddNoticePanel);
		showProgramNotice(frame, professorAddNoticePanel);
		showPanel(frame, professorAddNoticePanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddNoticePanel, "요일을 입력해주세요.", "Add Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddNoticePanel, "내용을 입력해주세요.", "Add Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorAddNoticePanel, "날짜 형식을 확인해주세요.", "Add Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					Notice k = new Notice(lecture.getCode(), lecture.getNextContentsKey(lecture, 2),
							University.StringToDate(day), text);
					University.contentMgr.read(k);
					University.noticeMgr.read(k);
					lecture.addContent(k);
					JOptionPane.showMessageDialog(professorAddNoticePanel, "추가 성공", "Add Notice successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}
		});

		return professorAddNoticePanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 공지 수정
	private JPanel professorEditNotice(JFrame frame, Lecture lecture, User user, Content content) {
		JPanel professorEditNoticePanel = makePanel("image/professornotice.png");
		professorEditNoticePanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "공지 수정", professorEditNoticePanel);

		makeLabel(400, 300, 90, 20, "과목명", professorEditNoticePanel);
		makeLabel(400, 330, 90, 20, "작성일", professorEditNoticePanel);
		makeLabel(400, 360, 90, 20, "내용", professorEditNoticePanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorEditNoticePanel,
				lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorEditNoticePanel,
				University.DateToString(content.getDate()));
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorEditNoticePanel, content.getBody());

		changePanel(frame, professorEditNoticePanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "수정", professorEditNoticePanel);
		showProgramNotice(frame, professorEditNoticePanel);
		showPanel(frame, professorEditNoticePanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditNoticePanel, "요일을 입력해주세요.", "Edit Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditNoticePanel, "내용을 입력해주세요.", "Edit Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorEditNoticePanel, "날짜 형식을 확인해주세요.", "Edit Notice failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					content.setDate(University.StringToDate(day));
					content.setBody(text);
					JOptionPane.showMessageDialog(professorEditNoticePanel, "변경 성공", "Edit Notice successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);
				}
			}
		});

		return professorEditNoticePanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 시험 탭
	private JPanel professorManageExamTab(JFrame frame, Lecture lecture, User user) {
		JPanel professorManageExamTabPanel = makePanel("image/null.png");

		ArrayList<Content> list = new ArrayList<>();
		for (Manageable m : University.examMgr.mList) {
			Content c = (Content) m;
			if (c.getCode().contentEquals(lecture.getCode()))
				list.add(c);
		}

		TableController tc = new TableController();
		String field[] = { "과목이름", "컨텐츠 유형", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorManageExamTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content content = (Content) tc.getList().get(row);
					String[] answer = { "수정", "삭제" };
					int result = JOptionPane.showOptionDialog(professorManageExamTabPanel,
							"[과목이름] : " + content.getLectureTitle() + "\n\n" + "[컨텐츠 유형] : "
									+ content.getContentCategory() + "\n\n" + "[작성일] : "
									+ University.DateToString(content.getDate()) + "\n\n" + "[내용]\n" + content.getBody()
									+ "\n\n\n",
							"choose menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.YES_OPTION) {
						professorEditExam(frame, lecture, user, content);
					} else if (result == JOptionPane.NO_OPTION) {
						University.contentMgr.delete(content);
						Exam as = (Exam) content;
						University.examMgr.delete(as);
						lecture.deleteContent(content);
						JOptionPane.showMessageDialog(professorManageExamTabPanel, "시험이 삭제되었습니다.",
								"Delete lecture successed", JOptionPane.INFORMATION_MESSAGE);
						professorLectureContents(frame, lecture, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		JButton examAddBtn = makeButton(500, 450, 100, 20, "시험 추가", professorManageExamTabPanel);
		examAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				professorAddExam(frame, lecture, user);
			}
		});
		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(professorManageExamTabPanel, tc, tableModel, list, false);
		showPanel(frame, professorManageExamTabPanel);

		return professorManageExamTabPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 시험 추가
	private JPanel professorAddExam(JFrame frame, Lecture lecture, User user) {
		JPanel professorAddExamPanel = makePanel("image/professorexam.png");
		professorAddExamPanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "시험 추가", professorAddExamPanel);

		makeLabel(400, 300, 90, 20, "과목명", professorAddExamPanel);
		makeLabel(400, 330, 90, 20, "작성일", professorAddExamPanel);
		makeLabel(400, 360, 90, 20, "내용", professorAddExamPanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorAddExamPanel, lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorAddExamPanel, null);
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorAddExamPanel, null);

		changePanel(frame, professorAddExamPanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "추가", professorAddExamPanel);
		showProgramNotice(frame, professorAddExamPanel);
		showPanel(frame, professorAddExamPanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddExamPanel, "요일을 입력해주세요.", "Add Exam failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddExamPanel, "내용을 입력해주세요.", "Add Exam failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorAddExamPanel, "날짜 형식을 확인해주세요.", "Add Exam failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					Exam k = new Exam(lecture.getCode(), lecture.getNextContentsKey(lecture, 3),
							University.StringToDate(day), text);
					University.contentMgr.read(k);
					University.examMgr.read(k);
					lecture.addContent(k);
					JOptionPane.showMessageDialog(professorAddExamPanel, "추가 성공", "Add Exam successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}
		});

		return professorAddExamPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 시험 수정
	private JPanel professorEditExam(JFrame frame, Lecture lecture, User user, Content content) {
		JPanel professorEditExamPanel = makePanel("image/professorexam.png");
		professorEditExamPanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "시험 수정", professorEditExamPanel);

		makeLabel(400, 300, 90, 20, "과목명", professorEditExamPanel);
		makeLabel(400, 330, 90, 20, "작성일", professorEditExamPanel);
		makeLabel(400, 360, 90, 20, "내용", professorEditExamPanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorEditExamPanel, lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorEditExamPanel,
				University.DateToString(content.getDate()));
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorEditExamPanel, content.getBody());

		changePanel(frame, professorEditExamPanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "수정", professorEditExamPanel);
		showProgramNotice(frame, professorEditExamPanel);
		showPanel(frame, professorEditExamPanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditExamPanel, "요일을 입력해주세요.", "Edit Exam failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditExamPanel, "내용을 입력해주세요.", "Edit Exam failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorEditExamPanel, "날짜 형식을 확인해주세요.", "Edit Exam failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					content.setDate(University.StringToDate(day));
					content.setBody(text);
					JOptionPane.showMessageDialog(professorEditExamPanel, "변경 성공", "Edit Exam successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}
		});

		return professorEditExamPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 영상 탭
	private JPanel professorManageVideoTab(JFrame frame, Lecture lecture, User user) {
		JPanel professorManageVideoTabPanel = makePanel("image/null.png");

		ArrayList<Content> list = new ArrayList<>();
		for (Manageable m : University.videoMgr.mList) {
			Content c = (Content) m;
			if (c.getCode().contentEquals(lecture.getCode()))
				list.add(c);
		}

		TableController tc = new TableController();
		String field[] = { "과목이름", "컨텐츠 유형", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(professorManageVideoTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content content = (Content) tc.getList().get(row);
					String[] answer = { "수정", "삭제" };
					int result = JOptionPane.showOptionDialog(professorManageVideoTabPanel,
							"[과목이름] : " + content.getLectureTitle() + "\n\n" + "[컨텐츠 유형] : "
									+ content.getContentCategory() + "\n\n" + "[작성일] : "
									+ University.DateToString(content.getDate()) + "\n\n" + "[내용]\n" + content.getBody()
									+ "\n\n\n",
							"choose menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.YES_OPTION) {
						professorEditVideo(frame, lecture, user, content);
					} else if (result == JOptionPane.NO_OPTION) {
						University.contentMgr.delete(content);
						Video as = (Video) content;
						University.videoMgr.delete(as);
						lecture.deleteContent(content);
						JOptionPane.showMessageDialog(professorManageVideoTabPanel, "강의 영상이 삭제되었습니다.",
								"Delete lecture successed", JOptionPane.INFORMATION_MESSAGE);
						professorLectureContents(frame, lecture, user);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		JButton videoAddBtn = makeButton(500, 450, 100, 20, "영상 추가", professorManageVideoTabPanel);
		videoAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				professorAddVideo(frame, lecture, user);
			}
		});
		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(professorManageVideoTabPanel, tc, tableModel, list, false);
		showPanel(frame, professorManageVideoTabPanel);

		return professorManageVideoTabPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 영상 추가
	private JPanel professorAddVideo(JFrame frame, Lecture lecture, User user) {
		JPanel professorAddVideoPanel = makePanel("image/professorvideo.png");
		professorAddVideoPanel.setLayout(null);

		makeLabel(485, 250, 130, 20, "강의영상 추가", professorAddVideoPanel);

		makeLabel(400, 300, 90, 20, "과목명", professorAddVideoPanel);
		makeLabel(400, 330, 90, 20, "작성일", professorAddVideoPanel);
		makeLabel(400, 360, 90, 20, "내용", professorAddVideoPanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorAddVideoPanel, lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorAddVideoPanel, null);
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorAddVideoPanel, null);

		changePanel(frame, professorAddVideoPanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "추가", professorAddVideoPanel);
		showProgramNotice(frame, professorAddVideoPanel);
		showPanel(frame, professorAddVideoPanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddVideoPanel, "요일을 입력해주세요.", "Add Video failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorAddVideoPanel, "내용을 입력해주세요.", "Add Video failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorAddVideoPanel, "날짜 형식을 확인해주세요.", "Add Video failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					Video k = new Video(lecture.getCode(), lecture.getNextContentsKey(lecture, 4),
							University.StringToDate(day), text);
					University.contentMgr.read(k);
					University.videoMgr.read(k);
					lecture.addContent(k);
					JOptionPane.showMessageDialog(professorAddVideoPanel, "추가 성공", "Add Video successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}
		});

		return professorAddVideoPanel;
	}

	// 교수 - 과목 조회 - 과목 컨텐츠 - 영상 수정
	private JPanel professorEditVideo(JFrame frame, Lecture lecture, User user, Content content) {
		JPanel professorEditVideoPanel = makePanel("image/professorvideo.png");
		professorEditVideoPanel.setLayout(null);

		makeLabel(485, 250, 130, 20, "강의영상 수정", professorEditVideoPanel);

		makeLabel(400, 300, 90, 20, "과목명", professorEditVideoPanel);
		makeLabel(400, 330, 90, 20, "작성일", professorEditVideoPanel);
		makeLabel(400, 360, 90, 20, "내용", professorEditVideoPanel);

		JTextField textfieldLectureName = makeTextfield(500, 300, 150, 20, professorEditVideoPanel, lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 330, 150, 20, professorEditVideoPanel,
				University.DateToString(content.getDate()));
		JTextArea textareaText = makeTextarea(500, 360, 150, 60, professorEditVideoPanel, content.getBody());

		changePanel(frame, professorEditVideoPanel, professorLectureContents(frame, lecture, user));

		JButton addLectureBtn = makeButton(510, 450, 80, 20, "수정", professorEditVideoPanel);
		showProgramNotice(frame, professorEditVideoPanel);
		showPanel(frame, professorEditVideoPanel);
		addLectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String day = textfieldDay.getText();
				String text = textareaText.getText();

				if (day.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditVideoPanel, "요일을 입력해주세요.", "Edit Video failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(professorEditVideoPanel, "내용을 입력해주세요.", "Edit Video failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(day)) {
					JOptionPane.showMessageDialog(professorEditVideoPanel, "날짜 형식을 확인해주세요.", "Edit Video failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					content.setDate(University.StringToDate(day));
					content.setBody(text);
					JOptionPane.showMessageDialog(professorEditVideoPanel, "변경 성공", "Edit Video successed",
							JOptionPane.INFORMATION_MESSAGE);
					professorLectureContents(frame, lecture, user);

				}

			}

		});

		return professorEditVideoPanel;
	}

//TODO
	// ========== 학생 관련 ==========

	// 학생 - 기본 메뉴
	private JPanel studentBasicMenu(JFrame frame, User user) {
		JPanel studentPanel = makePanel("image/student.png");
		studentPanel.setLayout(null);

		makeLabel(385, 550, 100, 30, user.getName() + " 님 ", studentPanel);
		JButton lectureBtn = makeButton(325, 300, 150, 50, "과목 조회", studentPanel);
		JButton calendarBtn = makeButton(625, 300, 150, 50, "스케쥴 관리", studentPanel);

		lectureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentLecture(frame, user);
			}
		});
		calendarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentCalendar(frame, user);
			}
		});

		logout(frame, studentPanel);
		myPageButton(frame, studentPanel, user);
		showProgramNotice(frame, studentPanel);
		showPanel(frame, studentPanel);

		return studentPanel;
	}

	// 학생 - 과목 조회
	private JPanel studentLecture(JFrame frame, User user) {
		JPanel studentLecturePanel = makePanel("image/studentlecture.png");
		studentLecturePanel.setLayout(null);

		JTabbedPane jtab = makeTab(studentLecturePanel);
		jtab.addTab("과목", studentLectureTab(frame, user));

		// 시간표
		JButton timeTableBtn = makeButton(665, 245, 110, 25, "시간표 조회", studentLecturePanel);
		timeTableBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Student st = (Student) user;

				String[] dayList = { "", "월", "화", "수", "목", "금" };
				String[][] data = { { "", "월", "화", "수", "목", "금" }, { "1", "", "", "", "", "" },
						{ "2", "", "", "", "", "" }, { "3", "", "", "", "", "" }, { "4", "", "", "", "", "" },
						{ "5", "", "", "", "", "" }, { "6", "", "", "", "", "" }, { "7", "", "", "", "", "" },
						{ "8", "", "", "", "", "" }, { "9", "", "", "", "", "" }, };

				for (Lecture lec : st.getStudentLectureList()) {
					String lectureTitle = lec.getTitle();
					String lectureDay = lec.getDay();
					String lectureTime = lec.getTime();
					int col = -1;

					for (String day : dayList) {
						col++;
						if (lectureDay.contentEquals(day)) {
							if (lectureTime.contentEquals("123")) {
								for (int row = 1; row < 4; row++) {
									data[row][col] = lectureTitle;
								}
							}

							else if (lectureTime.contentEquals("45")) {
								for (int row = 4; row < 6; row++) {
									data[row][col] = lectureTitle;
								}
							}

							else if (lectureTime.contentEquals("678")) {
								for (int row = 6; row < 9; row++) {
									data[row][col] = lectureTitle;
								}
							}
						}
					}
				}

				JTable timeTable = new JTable(data, dayList);

				DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
				dtcr.setHorizontalAlignment(SwingConstants.CENTER);
				TableColumnModel tcm = timeTable.getColumnModel();

				for (int i = 0; i < tcm.getColumnCount(); i++) {
					tcm.getColumn(i).setCellRenderer(dtcr);
				}
				JOptionPane.showMessageDialog(studentLecturePanel, timeTable, "Show TimeTable",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		changePanel(frame, studentLecturePanel, studentBasicMenu(frame, user));
		showProgramNotice(frame, studentLecturePanel);
		showPanel(frame, studentLecturePanel);

		return studentLecturePanel;
	}

	// 학생 - 과목 탭
	private JPanel studentLectureTab(JFrame frame, User user) {
		JPanel studentLectureTabPanel = makePanel("image/null.png");

		Student st = (Student) user;

		TableController tc = new TableController();
		String field[] = { "과목명", "요일", "시간" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(studentLectureTabPanel);
		tc.createTable(st.getStudentLectureList(), false);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 이벤트
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Lecture lec = st.getStudentLectureList().get(row); // 클릭한 과목에 대한 세부 항목 표시
					studentLectureContent(frame, lec, st);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
		showPanel(frame, studentLectureTabPanel);

		return studentLectureTabPanel;
	}

	// 학생 - 과목 조회 - 컨텐츠 조회
	private JPanel studentLectureContent(JFrame frame, Lecture lecture, User user) {
		JPanel studentLectureContentPanel = makePanel("image/lecturecontent.png"); // 패널을 만듬
		studentLectureContentPanel.setLayout(null);

		makeLabel(658, 247, 150, 20, lecture.getTitle(), studentLectureContentPanel);

		ArrayList<Assignment> assignList = University.assignmentMgr.mList;
		ArrayList<Notice> noticeList = University.noticeMgr.mList;
		ArrayList<Exam> examList = University.examMgr.mList;
		ArrayList<Video> videoList = University.videoMgr.mList;

		JTabbedPane jtab = makeTab(studentLectureContentPanel);
		jtab.addTab("과제", studentLectureAssignmentTab(frame, lecture, user, assignList));
		jtab.addTab("공지", studentLectureContentTab(frame, lecture, user, noticeList));
		jtab.addTab("시험", studentLectureContentTab(frame, lecture, user, examList));
		jtab.addTab("영상", studentLectureContentTab(frame, lecture, user, videoList));
		jtab.setBackground(new Color(255, 150, 150));

		changePanel(frame, studentLectureContentPanel, studentLecture(frame, user));
		showProgramNotice(frame, studentLectureContentPanel);
		showPanel(frame, studentLectureContentPanel);

		return studentLectureContentPanel;
	}

	// 학생 - 과목 조회 - 컨텐츠 조회 - 과제 탭
	private JPanel studentLectureAssignmentTab(JFrame frame, Lecture lecture, User user,
			ArrayList<? extends Manageable> mList) {
		JPanel studentLectureAssignmentPanel = makePanel("image/null.png");

		ArrayList<Content> list = new ArrayList<>();
		for (Manageable m : mList) {
			Content c = (Content) m;
			if (c.getCode().contentEquals(lecture.getCode()))
				list.add(c);
		}

		TableController tc = new TableController();
		String field[] = { "과목명", "컨텐츠 종류", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(studentLectureAssignmentPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content content = (Content) tc.getList().get(row);
					String[] answer = { "확인", "작성" };
					int result = JOptionPane.showOptionDialog(studentLectureAssignmentPanel,
							"[과목이름] : " + content.getLectureTitle() + "\n\n" + "[컨텐츠 유형] : "
									+ content.getContentCategory() + "\n\n" + "[작성일] : "
									+ University.DateToString(content.getDate()) + "\n\n" + "[내용]\n" + content.getBody()
									+ "\n\n\n",
							"choose menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
							answer[0]);
					if (result == JOptionPane.YES_OPTION) {

					} else if (result == JOptionPane.NO_OPTION) {
						Assignment B = (Assignment) content;
						ArrayList<Student> list = B.getSubmittedStudentList();
						for (Student st : list) {
							Student stu = (Student) user;
							if (st.equals(stu))
								JOptionPane.showMessageDialog(studentLectureAssignmentPanel, "제출을 이미 하셨습니다.",
										"Submit Assignment failed", JOptionPane.INFORMATION_MESSAGE);
							studentLectureContent(frame, lecture, user);
							return;
						}
						studentAssignmentSubmit(frame, lecture, user, content);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});
		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(studentLectureAssignmentPanel, tc, tableModel, list, false);
		showPanel(frame, studentLectureAssignmentPanel);

		return studentLectureAssignmentPanel;
	}

	// 학생 - 과목 조회 - 컨텐츠 조회 - 공지, 시험, 영상 탭
	private JPanel studentLectureContentTab(JFrame frame, Lecture lecture, User user,
			ArrayList<? extends Manageable> mList) {
		JPanel studentLectureContentPanel = makePanel("image/null.png");

		ArrayList<Content> list = new ArrayList<>();
		for (Manageable m : mList) {
			Content c = (Content) m;
			if (c.getCode().contentEquals(lecture.getCode()))
				list.add(c);
		}

		TableController tc = new TableController();
		String field[] = { "과목명", "컨텐츠 종류", "작성일", "내용" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(studentLectureContentPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 이벤트
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Content ct = (Content) tc.getList().get(row);
					JOptionPane.showMessageDialog(frame,
							"[과목이름] : " + ct.getLectureTitle() + "\n\n" + "[컨텐츠 유형] : " + ct.getContentCategory()
									+ "\n\n" + "[작성일] : " + University.DateToString(ct.getDate()) + "\n\n" + "[내용]\n"
									+ ct.getBody() + "\n\n\n",
							ct.getContentCategory(), JOptionPane.INFORMATION_MESSAGE);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(studentLectureContentPanel, tc, tableModel, list, false);
		showPanel(frame, studentLectureContentPanel);

		return studentLectureContentPanel;
	}

	// 학생 - 과목 조회 - 컨텐츠 조회 - 과제 탭 - 과제 작성
	private JPanel studentAssignmentSubmit(JFrame frame, Lecture lecture, User user, Content content) {
		JPanel studentAssignmentSubmitPanel = makePanel("image/studentsubmit.png");
		studentAssignmentSubmitPanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "과제 작성", studentAssignmentSubmitPanel);

		makeLabel(400, 310, 90, 20, "과목명", studentAssignmentSubmitPanel);
		makeLabel(400, 340, 90, 20, "과제명", studentAssignmentSubmitPanel);
		makeLabel(400, 370, 90, 20, "내용", studentAssignmentSubmitPanel);

		JTextField textfieldLectureName = makeTextfield(500, 310, 150, 20, studentAssignmentSubmitPanel,
				lecture.getTitle());
		textfieldLectureName.setEditable(false);
		JTextField textfieldDay = makeTextfield(500, 340, 150, 20, studentAssignmentSubmitPanel, content.getBody());
		textfieldDay.setEditable(false);
		JTextArea textareaText = makeTextarea(500, 370, 150, 60, studentAssignmentSubmitPanel, null);

		changePanel(frame, studentAssignmentSubmitPanel, studentLectureContent(frame, lecture, user));

		JButton submitAssignmentBtn = makeButton(500, 480, 100, 20, "제출", studentAssignmentSubmitPanel);

		showProgramNotice(frame, studentAssignmentSubmitPanel);
		showPanel(frame, studentAssignmentSubmitPanel);
		submitAssignmentBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textareaText.getText();
				Assignment B = (Assignment) content;

				if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(studentAssignmentSubmitPanel, "내용을 입력해주세요.",
							"Submit Assignment failed", JOptionPane.INFORMATION_MESSAGE);
				} else {
					Student stu = (Student) user;
					B.submitAssignment(text, stu);
					JOptionPane.showMessageDialog(studentAssignmentSubmitPanel, "제출 성공", "Submit Assignment successed",
							JOptionPane.INFORMATION_MESSAGE);
					studentLectureContent(frame, lecture, user);
				}
			}
		});
		return studentAssignmentSubmitPanel;
	}

	// 학생 - 스케쥴 관리
	private void studentCalendar(JFrame frame, User user) {
		frame.getContentPane().removeAll();

		JPanel panel = makePanel("image/calendar.png");
		frame.setContentPane(panel);

		SchedulerController sc = new SchedulerController();
		sc.setFrame(frame);
		sc.setUser(user);
		sc.setPane(panel);
		sc.setCalendar();

		JPanel SouthPanel = new JPanel();
		SouthPanel.setOpaque(false);
		JButton backBtn = new JButton("뒤로가기");
		backBtn.setBackground(new Color(232, 232, 232));
		backBtn.setBounds(500, 625, 100, 20);
		SouthPanel.add(backBtn);
		panel.add(SouthPanel, BorderLayout.SOUTH);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				frame.setVisible(true);
				studentBasicMenu(frame, user);
			}
		});

		frame.setVisible(true);
	}

	// 학생 - 스케쥴 관리 - 할 일 추가
	private JPanel studentAddTodo(JFrame frame, User user) {
		JPanel studentAddTodoPanel = makePanel("image/todo.png");
		studentAddTodoPanel.setLayout(null);

		Student st = (Student) user;

		makeLabel(500, 250, 100, 20, "할 일 추가", studentAddTodoPanel);

		makeLabel(400, 300, 60, 20, "작성일", studentAddTodoPanel);

		makeLabel(400, 330, 60, 20, "내용", studentAddTodoPanel);

		JTextField textfieldDate = makeTextfield(500, 300, 150, 20, studentAddTodoPanel, "yyyyMMdd");
		JTextArea textareaText = makeTextarea(500, 330, 150, 60, studentAddTodoPanel, null);

		JButton addTodoBtn = makeButton(510, 480, 80, 20, "추가", studentAddTodoPanel);

		JButton backBtn = new JButton("뒤로가기");
		backBtn.setBackground(new Color(232, 232, 232));
		backBtn.setBounds(500, 625, 100, 20);
		studentAddTodoPanel.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentCalendar(frame, user);
			}
		});
		showProgramNotice(frame, studentAddTodoPanel);
		showPanel(frame, studentAddTodoPanel);

		addTodoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String strDate = textfieldDate.getText();
				String text = textareaText.getText();

				if (strDate == null) {
					JOptionPane.showMessageDialog(studentAddTodoPanel, "작성일 형식을 확인해주세요. \n ex) 20201103",
							"Add Todo failed", JOptionPane.INFORMATION_MESSAGE);
					studentAddTodo(frame, user);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(studentAddTodoPanel, "내용을 입력해주세요.", "Add Todo failed",
							JOptionPane.INFORMATION_MESSAGE);
					studentAddTodo(frame, user);
				} else if (University.isNotDateType(strDate)) {
					JOptionPane.showMessageDialog(studentAddTodoPanel, "날짜 형식을 확인해주세요.", "Add Todo failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else { // 위의 두 가지 조건 통과 시 할 일
					Todo todo = new Todo(University.StringToDate(strDate), text); // 객체 생성
					st.addTodo(todo); // 학생의 TodoList에 할 일 객체 추가
					JOptionPane.showMessageDialog(studentAddTodoPanel, "추가 성공", "Add Todo successed",
							JOptionPane.INFORMATION_MESSAGE);

					studentCalendar(frame, user);
				}
			}
		});

		return studentAddTodoPanel;
	}

	// 학생 - 스케쥴 관리 - 할 일 수정
	private JPanel studentEditTodo(JFrame frame, Todo td, User user) {
		JPanel studentEditTodoPanel = makePanel("image/todo.png");
		studentEditTodoPanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "할 일 수정", studentEditTodoPanel);

		makeLabel(400, 300, 60, 20, "작성일", studentEditTodoPanel);
		makeLabel(400, 330, 60, 20, "내용", studentEditTodoPanel);
		makeLabel(400, 400, 60, 20, "완료 여부", studentEditTodoPanel);

		JTextField textfieldDate = makeTextfield(500, 300, 150, 20, studentEditTodoPanel,
				University.DateToString(td.getDate()));
		JTextArea textareaText = makeTextarea(500, 330, 150, 60, studentEditTodoPanel, td.getText());
		JTextField textfieldIsFinished = makeTextfield(500, 400, 150, 20, studentEditTodoPanel, td.showFinished());

		JButton backBtn = new JButton("뒤로가기");
		backBtn.setBackground(new Color(232, 232, 232));
		backBtn.setBounds(500, 625, 100, 20);
		studentEditTodoPanel.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentCalendar(frame, user);
			}
		});

		JButton editTodoBtn = makeButton(510, 480, 80, 20, "수정", studentEditTodoPanel);
		showProgramNotice(frame, studentEditTodoPanel);
		showPanel(frame, studentEditTodoPanel);
		editTodoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String date = textfieldDate.getText();
				String text = textareaText.getText();
				String isFinished = textfieldIsFinished.getText();

				if (date.contentEquals("")) {
					JOptionPane.showMessageDialog(studentEditTodoPanel, "작성일를 입력해주세요.", "Edit todo failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(studentEditTodoPanel, "내용을 입력해주세요.", "Edit todo failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (isFinished.contentEquals("")) {
					JOptionPane.showMessageDialog(studentEditTodoPanel, "완료 여부를 입력해주세요.", "Edit todo failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (!isFinished.contentEquals("Y") && !isFinished.contentEquals("N")) {
					JOptionPane.showMessageDialog(studentEditTodoPanel, "완료 여부는 완료 시 'Y' 미완료 시 'N'으로 입력해주세요.",
							"Edit todo failed", JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(date)) {
					JOptionPane.showMessageDialog(studentEditTodoPanel, "날짜 형식을 확인해주세요.", "Edit todo failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					td.setDate(University.StringToDate(date));
					td.setText(text);
					if (isFinished.contentEquals("Y")) {
						td.setFinished();
					} else {
						td.setUnfinished();
					}

					JOptionPane.showMessageDialog(studentEditTodoPanel, "변경 성공", "Edit todo successed",
							JOptionPane.INFORMATION_MESSAGE);
					studentCalendar(frame, user);
				}
			}
		});

		return studentEditTodoPanel;
	}

	// ========== 공통 기능 관련 ==========

	// 프로그램 공지사항
	private void showProgramNotice(JFrame frame, JPanel panel) {
		JTextArea textarea = makeTextarea(300, 50, 500, 55, panel, null);
		for (ProgramNotice proNtc : University.programNoticeMgr.mList) {
			textarea.append(" [ " + University.DateToString(proNtc.getDate()) + " ]  " + proNtc.getBody() + "\n");
		}
		textarea.setEditable(false);
	}

	// 마이페이지 버튼
	private void myPageButton(JFrame frame, JPanel firstPanel, User user) {
		JButton mypageBtn = makeButton(580, 625, 100, 20, "마이페이지", firstPanel);
		mypageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myPageMenu(frame, firstPanel, user);
			}
		});
	}

	// 마이페이지
	private JPanel myPageMenu(JFrame frame, JPanel firstPanel, User user) {
		JPanel myPageMenuPanel = makePanel("image/mypage.png");
		myPageMenuPanel.setLayout(null);

		makeLabel(390, 200, 400, 20, user.getName() + " (" + user.getId() + ")" + "  님의 마이페이지 ", myPageMenuPanel);

		JButton editUserInfoBtn = makeButton(310, 350, 130, 20, "개인정보 수정", myPageMenuPanel);
		JButton editUserPWBtn = makeButton(650, 350, 130, 20, "비밀번호 수정", myPageMenuPanel);
		JButton messegeBoxBtn = makeButton(310, 480, 130, 20, "메시지 함", myPageMenuPanel);
		JButton sendMessageBtn = makeButton(650, 480, 130, 20, "메시지 전송", myPageMenuPanel);

		changePanel(frame, myPageMenuPanel, firstPanel);
		showProgramNotice(frame, myPageMenuPanel);
		showPanel(frame, myPageMenuPanel);

		editUserInfoBtn.addActionListener(new ActionListener() { // 개인정보 수정
			@Override
			public void actionPerformed(ActionEvent e) {
				editUserInfo(frame, firstPanel, myPageMenuPanel, user);
			}

		});
		editUserPWBtn.addActionListener(new ActionListener() { // 비밀번호 수정
			@Override
			public void actionPerformed(ActionEvent e) {
				editUserPW(frame, firstPanel, myPageMenuPanel, user);
			}

		});
		messegeBoxBtn.addActionListener(new ActionListener() { // 메시지 함
			@Override
			public void actionPerformed(ActionEvent e) {
				messegeBox(frame, firstPanel, myPageMenuPanel, user);
			}

		});
		sendMessageBtn.addActionListener(new ActionListener() { // 메시지 전송
			@Override
			public void actionPerformed(ActionEvent e) {
				writeMessage(frame, firstPanel, myPageMenuPanel, user);
			}

		});

		return myPageMenuPanel;
	}

	// 마이페이지 - 개인정보 수정
	private JPanel editUserInfo(JFrame frame, JPanel firstPanel, JPanel secondPanel, User user) {
		JPanel editUserInfoPanel = makePanel("image/userinfo.png");
		editUserInfoPanel.setLayout(null);

		makeLabel(450, 255, 200, 20, "개인정보 수정", editUserInfoPanel);

		makeLabel(400, 310, 60, 20, "이름", editUserInfoPanel);
		makeLabel(400, 340, 60, 20, "나이", editUserInfoPanel);
		makeLabel(400, 370, 60, 20, "전화번호", editUserInfoPanel);

		JTextField textfieldName = makeTextfield(500, 310, 150, 20, editUserInfoPanel, user.getName());
		JTextField textfieldAge = makeTextfield(500, 340, 150, 20, editUserInfoPanel, Integer.toString(user.getAge()));
		JTextField textfieldPhone = makeTextfield(500, 370, 150, 20, editUserInfoPanel, user.getPhone());

		changePanel(frame, editUserInfoPanel, secondPanel);

		JButton editBtn = makeButton(510, 460, 80, 20, "변경", editUserInfoPanel);
		showProgramNotice(frame, editUserInfoPanel);
		showPanel(frame, editUserInfoPanel);
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = textfieldName.getText();
				String stringAge = textfieldAge.getText();
				String phone = textfieldPhone.getText();

				if (name.contentEquals("")) {
					JOptionPane.showMessageDialog(editUserInfoPanel, "이름을 입력해주세요.", "Edit User Info failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (stringAge.contentEquals("")) {
					JOptionPane.showMessageDialog(editUserInfoPanel, "나이를 입력해주세요.", "Edit User Info failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (phone.contentEquals("")) {
					JOptionPane.showMessageDialog(editUserInfoPanel, "전화번호를 입력해주세요.", "Edit User Info failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					int age = -1;
					try {
						age = Integer.parseInt(stringAge);
					} catch (Exception e1) {

					}
					if (age == -1) {
						JOptionPane.showMessageDialog(editUserInfoPanel, "나이는 정수만 입력해주세요.", "Edit User Info failed",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						user.setName(name);
						user.setAge(age);
						user.setPhone(phone);
						JOptionPane.showMessageDialog(editUserInfoPanel, "수정 성공", "Edit User Info successed",
								JOptionPane.INFORMATION_MESSAGE);
						myPageMenu(frame, firstPanel, user);
					}
				}
			}
		});

		return editUserInfoPanel;
	}

	// 마이페이지 - 비밀번호 수정
	private JPanel editUserPW(JFrame frame, JPanel firstPanel, JPanel secondPanel, User user) {
		JPanel editUserPWPanel = makePanel("image/pw.png");
		editUserPWPanel.setLayout(null);

		makeLabel(450, 255, 200, 20, "비밀번호 수정", editUserPWPanel);
		makeLabel(350, 310, 100, 20, "기존 비밀번호", editUserPWPanel);
		makeLabel(350, 340, 100, 20, "새 비밀번호", editUserPWPanel);
		makeLabel(330, 370, 120, 20, "새 비밀번호(확인)", editUserPWPanel);

		JPasswordField textfieldPreviousPW = new JPasswordField();
		textfieldPreviousPW.setBounds(500, 310, 150, 20);
		editUserPWPanel.add(textfieldPreviousPW);

		JPasswordField textfieldNewPW = new JPasswordField();
		textfieldNewPW.setBounds(500, 340, 150, 20);
		editUserPWPanel.add(textfieldNewPW);

		JPasswordField textfieldNewPWConfirm = new JPasswordField();
		textfieldNewPWConfirm.setBounds(500, 370, 150, 20);
		editUserPWPanel.add(textfieldNewPWConfirm);

		changePanel(frame, editUserPWPanel, secondPanel);

		JButton editBtn = makeButton(510, 460, 80, 20, "변경", editUserPWPanel);
		showProgramNotice(frame, editUserPWPanel);
		showPanel(frame, editUserPWPanel);
		editBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String previousPW = new String(textfieldPreviousPW.getPassword());
				String newPW = new String(textfieldNewPW.getPassword());
				String newPWConfirm = new String(textfieldNewPWConfirm.getPassword());

				if (previousPW.contentEquals("")) {
					JOptionPane.showMessageDialog(editUserPWPanel, "기존 비밀번호를 입력해주세요.", "Edit User PW failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (newPW.contentEquals("")) {
					JOptionPane.showMessageDialog(editUserPWPanel, "새 비밀번호를 입력해주세요.", "Edit User PW failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (newPWConfirm.contentEquals("")) {
					JOptionPane.showMessageDialog(editUserPWPanel, "새 비밀번호(확인)를 입력해주세요.", "Edit User PW failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (!user.getPw().contentEquals(previousPW)) {
						JOptionPane.showMessageDialog(editUserPWPanel, "기존 비밀번호가 일치하지 않습니다.", "Edit User PW failed",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						if (!newPW.contentEquals(newPWConfirm)) {
							JOptionPane.showMessageDialog(editUserPWPanel, "새 비밀번호가 서로 일치하지 않습니다.",
									"Edit User PW failed", JOptionPane.INFORMATION_MESSAGE);
						}

						else {
							if (newPW.contentEquals(previousPW)) {
								JOptionPane.showMessageDialog(editUserPWPanel, "새 비밀번호가 기존 비밀번호와 동일합니다.",
										"Edit User PW failed", JOptionPane.INFORMATION_MESSAGE);
							} else {
								user.setPW(newPW);
								JOptionPane.showMessageDialog(editUserPWPanel, "수정 성공", "Edit User PW successed",
										JOptionPane.INFORMATION_MESSAGE);
								myPageMenu(frame, firstPanel, user);
							}
						}
					}
				}
			}
		});

		return editUserPWPanel;
	}

	// 마이페이지 - 메시지 함
	private JPanel messegeBox(JFrame frame, JPanel firstPanel, JPanel secondPanel, User user) {
		JPanel messegeBoxPanel = makePanel("image/msgbox.png");

		messegeBoxPanel.setLayout(null);

		makeLabel(500, 220, 100, 20, "메세지 함", messegeBoxPanel);

		JTabbedPane jtab = makeTab(messegeBoxPanel);
		jtab.addTab("받은 메시지", receivedMessageTab(frame, firstPanel, secondPanel, user));
		jtab.addTab("보낸 메시지", sentMessageTab(frame, firstPanel, secondPanel, user));

		changePanel(frame, messegeBoxPanel, secondPanel);
		showProgramNotice(frame, messegeBoxPanel);
		showPanel(frame, messegeBoxPanel);

		return messegeBoxPanel;
	}

	// 마이페이지 - 메시지 함 - 받은 메시지 탭
	private JPanel receivedMessageTab(JFrame frame, JPanel firstPanel, JPanel secondPanel, User user) {
		JPanel receivedMessageTabPanel = makePanel("image/null.png");

		ArrayList<Message> list = user.getReceivedMessageList();
		TableController tc = new TableController();
		String field[] = { "From", "To", "작성일", "제목" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(receivedMessageTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Message msg = (Message) tc.getList().get(row);
					JOptionPane.showMessageDialog(receivedMessageTabPanel,
							"[보낸 이] : " + msg.getFrom() + "\n\n" + "[받는 이] : " + msg.getTo() + "\n\n" + "[작성일] : "
									+ University.DateToString(msg.getDate()) + "\n\n" + "[제목] : " + msg.getTitle()
									+ "\n\n" + "[내용]\n" + msg.getText() + "\n\n\n",
							"메시지", JOptionPane.CLOSED_OPTION);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(receivedMessageTabPanel, tc, tableModel, list, false);
		showPanel(frame, receivedMessageTabPanel);

		return receivedMessageTabPanel;
	}

	// 마이페이지 - 메시지 함 - 보낸 메시지 탭
	private JPanel sentMessageTab(JFrame frame, JPanel firstPanel, JPanel secondPanel, User user) {
		JPanel sentMessageTabPanel = makePanel("image/null.png");

		ArrayList<Message> list = user.getSentMessageList();
		TableController tc = new TableController();
		String field[] = { "From", "To", "작성일", "제목" };
		tc.setField(field);
		tc.setTableModel();
		tc.addToPanel(sentMessageTabPanel);
		tc.createTable(list, false);
		tc.setList(list);

		JTable table = tc.getTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { // 마우스 클릭 시
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					Message msg = (Message) tc.getList().get(row);
					JOptionPane.showMessageDialog(sentMessageTabPanel,
							"[보낸 이] : " + msg.getFrom() + "\n\n" + "[받는 이] : " + msg.getTo() + "\n\n" + "[작성일] : "
									+ University.DateToString(msg.getDate()) + "\n\n" + "[제목] : " + msg.getTitle()
									+ "\n\n" + "[내용]\n" + msg.getText() + "\n\n\n",
							"메시지", JOptionPane.CLOSED_OPTION);
					changePanel(frame, sentMessageTabPanel, secondPanel);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		DefaultTableModel tableModel = tc.getTableModel();
		searchTable(sentMessageTabPanel, tc, tableModel, list, false);
		showPanel(frame, sentMessageTabPanel);

		return sentMessageTabPanel;
	}

	// 마이페이지 - 메시지 작성
	private JPanel writeMessage(JFrame frame, JPanel firstPanel, JPanel secondPanel, User sendUser) {
		JPanel writeMessagePanel = makePanel("image/msgsend.png");
		writeMessagePanel.setLayout(null);

		makeLabel(500, 250, 100, 20, "메시지 작성", writeMessagePanel);

		makeLabel(400, 300, 90, 20, "보내는 이(ID)", writeMessagePanel);
		makeLabel(400, 330, 90, 20, "받는 이(ID)", writeMessagePanel);
		makeLabel(400, 360, 90, 20, "작성일", writeMessagePanel);
		makeLabel(400, 390, 90, 20, "제목", writeMessagePanel);
		makeLabel(400, 420, 90, 20, "내용", writeMessagePanel);

		JTextField textfieldFromID = makeTextfield(500, 300, 150, 20, writeMessagePanel,
				sendUser.getId() + "(" + sendUser.getName() + ")");
		textfieldFromID.setEditable(false);
		JTextField textfieldToID = makeTextfield(500, 330, 150, 20, writeMessagePanel, null);
		JTextField textfieldDate = makeTextfield(500, 360, 150, 20, writeMessagePanel, null);
		JTextField textfieldTitle = makeTextfield(500, 390, 150, 20, writeMessagePanel, null);
		JTextArea textareaText = makeTextarea(500, 420, 150, 60, writeMessagePanel, null);

		changePanel(frame, writeMessagePanel, secondPanel);

		JButton sendBtn = makeButton(510, 550, 80, 20, "전송", writeMessagePanel);
		showProgramNotice(frame, writeMessagePanel);
		showPanel(frame, writeMessagePanel);
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fromID = sendUser.getId();
				String toID = textfieldToID.getText();
				String date = textfieldDate.getText();
				String title = textfieldTitle.getText();
				String text = textareaText.getText();

				if (toID.contentEquals("")) {
					JOptionPane.showMessageDialog(writeMessagePanel, "받는 이를 입력해주세요.", "Send message failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (date.contentEquals("")) {
					JOptionPane.showMessageDialog(writeMessagePanel, "작성일를 입력해주세요.", "Send message failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (title.contentEquals("")) {
					JOptionPane.showMessageDialog(writeMessagePanel, "제목을 입력해주세요.", "Send message failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (text.contentEquals("")) {
					JOptionPane.showMessageDialog(writeMessagePanel, "내용을 입력해주세요.", "Send message failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (University.isNotDateType(date)) {
					JOptionPane.showMessageDialog(writeMessagePanel, "날짜 형식을 확인해주세요.", "Send message failed",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					User receiveUser = null;
					receiveUser = University.userMgr.find(toID, false);
					if (receiveUser == null) {
						JOptionPane.showMessageDialog(writeMessagePanel, "받는 이가 존재하지 않습니다.", "Send message failed",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						Message msg = new Message(fromID, toID, University.StringToDate(date), title, text); // 위의 조건 통과
																												// 시 객체
																												// 생성
						University.messageMgr.read(msg);
						receiveUser.addReceivedMessage(msg);
						sendUser.addSentMessage(msg);
						JOptionPane.showMessageDialog(writeMessagePanel, "전송 성공", "Send message successed",
								JOptionPane.INFORMATION_MESSAGE);
						myPageMenu(frame, firstPanel, sendUser);
					}
				}
			}
		});

		return writeMessagePanel;
	}

	// 로그아웃
	private void logout(JFrame frame, JPanel panel) {
		JButton logoutBtn = makeButton(420, 625, 100, 20, "로그아웃", panel);
		logoutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login(frame);
			}
		});
	}

	// 화면 전환
	private void changePanel(JFrame frame, JPanel oldPanel, JPanel newPanel) {
		JButton backBtn = new JButton("뒤로가기");
		backBtn.setBounds(500, 625, 100, 20);
		backBtn.setBackground(new Color(232, 232, 232));
		oldPanel.add(backBtn);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				oldPanel.setVisible(false); // 현재 패널을 화면에서 없앰
				showPanel(frame, newPanel); // 가져온 패널을 보여줌
			}
		});
	}

	// 테이블 검색
	private void searchTable(JPanel parentPanel, TableController tc, DefaultTableModel tableModel,
			ArrayList<? extends Manageable> list, boolean isAdmin) {
		JPanel searchTablePanel = makePanel("image/null.png"); // 버튼 패널 하단에 추가
		searchTablePanel.setLayout(null);
		parentPanel.add(searchTablePanel, BorderLayout.PAGE_END);
		JTextField kwdTextField = makeTextfield(425, 0, 100, 25, searchTablePanel, null);
		JButton searchBtn = makeButton(525, 0, 60, 25, "검색", searchTablePanel);

		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String kwd = kwdTextField.getText();
				loadData(kwd, tc, tableModel, list, isAdmin);
			}
		});
	}

	// 검색 키워드에 따라 테이블 수정
	private void loadData(String kwd, TableController tc, DefaultTableModel tableModel,
			ArrayList<? extends Manageable> list, boolean isAdmin) {
		ArrayList<Manageable> kwdList = new ArrayList<>();
		for (Manageable m : list) {
			if (m.matches(kwd, true))
				kwdList.add(m);
		}
		tableModel.setRowCount(0);
		for (Object m : kwdList)
			tableModel.addRow(((UIData) m).getUiTexts(isAdmin));

		tc.setList(kwdList);
	}

//TODO
	// ========== 컴포넌트 관련 ==========

	// 기존 패널 지우고 새 패널 추가 후 보이기
	private void showPanel(JFrame frame, JPanel panel) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}

	// 패널 만들기
	private JPanel makePanel(String imgPath) {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			ImageIcon background = new ImageIcon(imgPath);

			public void paintComponent(Graphics g) {// 그리는 함수
				g.drawImage(background.getImage(), 0, 0, 1100, 800, null);// background를 그려줌
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		panel.setPreferredSize(new Dimension(1000, 400));

		return panel;
	}

	// 탭 만들기
	private JTabbedPane makeTab(JPanel panel) {
		JTabbedPane tab = new JTabbedPane();
		tab.setBounds(300, 250, 500, 350);
		panel.add(tab, BorderLayout.CENTER);

		return tab;
	}

	// 라벨 만들기(위치, 이름, 위치할 패널), 반환 x
	private void makeLabel(int x, int y, int z, int w, String name, JPanel panel) {
		JLabel label = new JLabel();
		label.setBounds(x, y, z, w);
		label.setText(name);
		label.setHorizontalAlignment(JLabel.CENTER);
		panel.add(label, BorderLayout.CENTER);
	}

	// 텍스트필드 만들기, 반환o
	private JTextField makeTextfield(int x, int y, int z, int w, JPanel panel, String text) {
		JTextField textfield = new JTextField(text);
		textfield.setBounds(x, y, z, w);
		panel.add(textfield);

		return textfield;
	}

	// 텍스트에리어 만들기, 반환o
	private JTextArea makeTextarea(int x, int y, int z, int w, JPanel panel, String text) {
		JTextArea textarea = new JTextArea(text);
		JScrollPane sp = new JScrollPane(textarea);
		sp.setBounds(x, y, z, w);
		panel.add(sp);

		return textarea;
	}

	// 버튼 만들기, 반환o
	private JButton makeButton(int x, int y, int z, int w, String name, JPanel panel) { // 버튼 만들기, 반환o
		JButton Btn = new JButton(name);
		Btn.setBounds(x, y, z, w);
		Btn.setBackground(new Color(232, 232, 232));
		panel.add(Btn, BorderLayout.CENTER);

		return Btn;
	}

//TODO
	// ========== 테이블 관련 ==========

	// 테이블 제어 클래스
	class TableController {
		DefaultTableModel tableModel;
		JTable table;
		JScrollPane sp;
		String field[];
		ArrayList<? extends Manageable> list;

		public void setField(String field[]) { // 필드 생성
			this.field = field;
		}

		public void setTableModel() { // defaultTableModel 구조 생성
			tableModel = new DefaultTableModel(field, 0) {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table = new JTable(tableModel);
			sp = new JScrollPane(table);
			sp.setPreferredSize(new Dimension(450, 200));
			sp.setLocation(325, 250);
		}

		public void addToPanel(JPanel panel) { // 현재 패널에 테이블 추가
			panel.add(sp, BorderLayout.CENTER);
		}

		public void resetTable() {
			tableModel.setRowCount(0);
		}

		public void createTable(ArrayList<?> arrayList, boolean isAdmin) { // 테이블에 데이터 추가
			Object record[] = new Object[field.length]; // 필드 수만큼 열 구조 생성
			for (Object ob : arrayList) { // 리스트에 있는 항목 저장
				record = ((UIData) ob).getUiTexts(isAdmin);
				tableModel.addRow(record);
			}
		}

		public ArrayList<? extends Manageable> getList() {
			return list;
		}

		public void setList(ArrayList<? extends Manageable> list) {
			this.list = list;
		}

		public JTable getTable() {
			return table;
		}

		public DefaultTableModel getTableModel() {
			return tableModel;
		}
	}

//TODO
	// ========== 스케쥴러 관련 ==========

	// 스케쥴러 제어 클래스
	class SchedulerController extends CalendarManager {
		JFrame frame;
		User user;
		JPanel calendarPane;

		JPanel NorthPanel = new JPanel();
		JPanel CenterPanel = new JPanel();
		JPanel EastPanel = new JPanel();

		// ========== 캘린더 관련(1) ==========

		public void setCalendar() {
			setToday();

			JButton LButton = new JButton("<<");
			LButton.setBackground(new Color(232, 232, 232));
			NorthPanel.add(LButton);
			JLabel YMLabel = new JLabel(getYear() + "년 " + getMonth() + "월");
			NorthPanel.add(YMLabel);
			JButton RButton = new JButton(">>");
			RButton.setBackground(new Color(232, 232, 232));
			NorthPanel.add(RButton);
			JLabel todayLabel = new JLabel("Today: " + getYear() + "/" + getMonth() + "/" + getDate());
			NorthPanel.add(todayLabel);
			NorthPanel.setOpaque(false);

			JLabel[] weekDaysLabel = new JLabel[7];
			JButton[][] dateButton = new JButton[CAL_HEIGHT][CAL_WIDTH];

			// 캘린더 - 월~일 라벨 생성
			for (int i = 0; i < CAL_WIDTH; i++) {
				weekDaysLabel[i] = new JLabel(WEEK_DAY_NAME[i]);
				weekDaysLabel[i].setHorizontalAlignment(JLabel.CENTER);
				if (i == 0)
					weekDaysLabel[i].setForeground(Color.red);
				else
					weekDaysLabel[i].setForeground(Color.BLACK);
				CenterPanel.add(weekDaysLabel[i]);
			}

			// 캘린더 - 일자 버튼 생성
			for (int i = 0; i < CAL_HEIGHT; i++) {
				for (int j = 0; j < CAL_WIDTH; j++) {
					dateButton[i][j] = new JButton();
					if (j == 0)
						dateButton[i][j].setForeground(Color.red);
					dateButton[i][j].setBackground(new Color(255, 255, 255));
					CenterPanel.add(dateButton[i][j]);
				}
			}
			CenterPanel.setOpaque(false);
			calculateButtons(dateButton);

			// 캘린더 - 오늘로 이동 버튼
			JButton todayButton = new JButton("Today");
			todayButton.setBackground(new Color(232, 232, 232));
			todayButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setToday();
					YMLabel.setText(calYear + "년" + (calMonth + 1) + "월");
					calculateButtons(dateButton);
				}

			});
			NorthPanel.add(todayButton);

			// 캘린더 - 월 변경
			RButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == RButton) {
						if (calMonth == 13) {
							moveMonth(12);
						} else
							moveMonth(1);
						YMLabel.setText(calYear + "년" + (calMonth + 1) + "월");
					}
					calculateButtons(dateButton);
				}
			});

			LButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == LButton) {
						if (calMonth == 0) {
							moveMonth(-12);
						} else
							moveMonth(-1);
						YMLabel.setText(calYear + "년" + (calMonth + 1) + "월");
					}
					calculateButtons(dateButton);
				}
			});

			EastPanel.setLayout(new GridLayout(7, 0, 20, 0));
			EastPanel.setOpaque(false);

			JLabel content = new JLabel("오늘의 일정");
			content.setHorizontalAlignment(JLabel.CENTER);
			EastPanel.add(content);

			// ========== 컨텐츠 관련 ==========

			// 컨텐츠 패널(라벨)
			JLabel contentOfLectures = new JLabel("컨텐츠");
			contentOfLectures.setHorizontalAlignment(JLabel.CENTER);
			EastPanel.add(contentOfLectures);

			// 과목 컨텐츠 목록
			JPanel contentPanel = new JPanel();
			contentPanel.setOpaque(false);
			ArrayList<Content> todayContentList = new ArrayList<>();
			for (Lecture lec : ((Student) user).getStudentLectureList()) {
				for (Content c : lec.getlectureContentList()) {
					if (University.DateToString(c.getDate()).contentEquals("" + getYear() + getMonth() + getDate()))
						todayContentList.add(c);
				}
			}
			TableController contentTC = new TableController();
			String contentField[] = { "과목명", "컨텐츠 종류", "작성일", "내용" };
			contentTC.setField(contentField);
			contentTC.setTableModel();
			contentTC.sp.setPreferredSize(new Dimension(450, 90));
			contentTC.addToPanel(contentPanel);
			contentTC.createTable(todayContentList, false);
			contentTC.setList(todayContentList);
			JTable contentTable = contentTC.getTable();
			contentTable.setAutoCreateRowSorter(true);
			contentTable.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e1) { // 마우스 클릭 시
					if (e1.getClickCount() == 2) {
						int row = contentTable.getSelectedRow();
						Content ct = (Content) contentTC.getList().get(row);
						JOptionPane.showMessageDialog(calendarPane,
								"[과목명] : " + ct.getLectureTitle() + "\n\n" + "[컨텐츠 종류] : " + ct.getContentCategory()
										+ "\n\n" + "[작성일] : " + University.DateToString(ct.getDate()) + "\n\n"
										+ "[내용]\n" + ct.getBody() + "\n\n\n",
								ct.getContentCategory(), JOptionPane.INFORMATION_MESSAGE);
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}
			});
			EastPanel.add(contentPanel);

			// ========== 할 일 관련 ==========

			// 할 일 패널(라벨)
			JLabel Todo = new JLabel("할 일");
			Todo.setHorizontalAlignment(JLabel.CENTER);
			EastPanel.add(Todo);

			// 할 일 목록
			JPanel todoPanel = new JPanel();
			todoPanel.setOpaque(false);
			ArrayList<Todo> todayTodoList = new ArrayList<>();
			for (Todo td : ((Student) user).getTodoList()) {
				if (University.DateToString(td.getDate()).contentEquals("" + getYear() + getMonth() + getDate()))
					todayTodoList.add(td);
			}
			TableController todoTC = new TableController();
			String todoPanelField[] = { "작성일", "할 일", "완료 여부" };
			todoTC.setField(todoPanelField);
			todoTC.setTableModel();
			todoTC.sp.setPreferredSize(new Dimension(450, 90));
			todoTC.addToPanel(todoPanel);
			todoTC.createTable(todayTodoList, false);
			todoTC.setList(todayTodoList);
			JTable todoTable = todoTC.getTable();
			todoTable.setAutoCreateRowSorter(true);
			todoTable.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e1) { // 마우스 클릭 시
					if (e1.getClickCount() == 2) {
						int row = todoTable.getSelectedRow();
						Todo td = (Todo) todoTC.getList().get(row);
						String[] answer = { "완료", "수정", "삭제" };
						int result = JOptionPane.showOptionDialog(calendarPane,
								"[작성일] : " + University.DateToString(td.getDate()) + "\n\n" + "[할 일]\n" + td.getText()
										+ "\n\n" + "[완료 여부] : " + td.showFinished() + "\n\n\n",
								"할 일", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, answer,
								answer[0]);
						if (result == JOptionPane.YES_OPTION) {
							td.setFinished();
							studentCalendar(frame, user);
						} else if (result == JOptionPane.NO_OPTION) {
							studentEditTodo(frame, td, user);
						} else if (result == JOptionPane.CANCEL_OPTION) {
							((Student) user).deleteTodo(td);
							JOptionPane.showMessageDialog(calendarPane, "할 일이 삭제되었습니다.", "Delete todo successed",
									JOptionPane.INFORMATION_MESSAGE);
							studentCalendar(frame, user);
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

			});
			EastPanel.add(todoPanel);

			// 할 일 추가 버튼
			JPanel studentAddTodoBtnPanel = new JPanel();
			studentAddTodoBtnPanel.setOpaque(false);
			JButton studentAddTodoBtn = new JButton("할 일 추가");
			studentAddTodoBtn.setBackground(new Color(232, 232, 232));
			studentAddTodoBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					studentAddTodo(frame, user);
				}

			});
			studentAddTodoBtnPanel.add(studentAddTodoBtn);
			EastPanel.add(studentAddTodoBtnPanel);

			// ========== 캘린더 관련(2) ==========

			// 캘린더 - 일자 버튼 리스너
			for (int i = 0; i < CAL_HEIGHT; i++) {
				for (int j = 0; j < CAL_WIDTH; j++) {

					dateButton[i][j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							for (int a = 0; a < CAL_HEIGHT; a++) { // 원래 색으로 변경
								for (int b = 0; b < CAL_WIDTH; b++) {
									dateButton[a][b].setBackground(Color.WHITE);
								}
							}
							JButton clickedButton = (JButton) e.getSource();
							clickedButton.setBackground(new Color(255, 150, 150)); // 클릭시 빨간색으로 변경
							String date = null;
							if (clickedButton.getText().length() == 1) {
								date = "0" + clickedButton.getText();
							} else
								date = clickedButton.getText();
							String clickedDay = null;
							if (getMonth() > 9)
								clickedDay = "" + getYear() + getMonth() + date;
							else
								clickedDay = "" + getYear() + "0" + getMonth() + date;
							content.setText(getYear() + "년 " + getMonth() + "월 " + date + "일의 일정");
							changeContentSchedule(clickedDay, contentPanel, contentTC, user);
							changeTodoSchedule(clickedDay, todoPanel, todoTC, user);
						}
					});
				}
			}
			calendarPane.add(NorthPanel, BorderLayout.NORTH);
			calendarPane.add(EastPanel, BorderLayout.EAST);
			CenterPanel.setLayout(new GridLayout(0, 7, 1, 1));
			calendarPane.add(CenterPanel, BorderLayout.CENTER);
		}

		// 캘린더 - 일자 계산
		private void calculateButtons(JButton[][] dateButton) {
			// 버튼 모두 보이기
			for (int i = 0; i < CAL_HEIGHT; i++) {
				for (int j = 0; j < CAL_WIDTH; j++) {
					dateButton[i][j].setVisible(true);
				}
			}
			// 다음 1일까지 버튼 공백
			for (int j = 0; j < 1; j++) {
				for (int i = 0; i < dayofweek - 1; i++) {
					dateButton[j][i].setText("");
				}
			}
			// 1일 전 버튼 숨김
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < calStartingPos; j++) {
					dateButton[i][j].setVisible(false);
				}
			}
			// 일자 버튼 작성일 text 저장
			for (int i = 0, num = 1, k = 0; i < CAL_HEIGHT; i++) {
				if (i == 0)
					k = calStartingPos;
				else
					k = 0;
				for (int j = k; j < CAL_WIDTH; j++) {
					if (num <= lastday)
						dateButton[i][j].setText("" + num++);
					else {
						dateButton[i][j].setText("");
						dateButton[i][j].setVisible(false); // 마지막날 이후 버튼 숨김
					}
				}
			}
		}

		// 선택한 날짜의 컨텐츠로 변경
		protected void changeContentSchedule(String clickedDay, JPanel contentPanel, TableController tc, User user) {
			Student st = (Student) user;
			// 해당 일자의 강의 컨텐츠 출력
			ArrayList<Content> contentsOfTheDay = new ArrayList<>();
			for (Lecture lec : st.getStudentLectureList()) {
				for (Content c : lec.getlectureContentList()) {
					if (University.DateToString(c.getDate()).contentEquals(clickedDay))
						contentsOfTheDay.add(c);
				}
			}
			tc.resetTable();
			tc.createTable(contentsOfTheDay, false);
			tc.setList(contentsOfTheDay);
		}

		// 선택한 날짜의 할 일로 변경
		protected void changeTodoSchedule(String clickedDay, Container calendarPane, TableController tc, User user) {
			Student st = (Student) user;
			// 해당 일자의 할 일 출력
			ArrayList<Todo> todoOfTheDay = new ArrayList<>();
			for (Todo td : st.getTodoList()) {
				if (University.DateToString(td.getDate()).contentEquals(clickedDay))
					todoOfTheDay.add(td);
			}
			tc.resetTable();
			tc.createTable(todoOfTheDay, false);
			tc.setList(todoOfTheDay);
		}

		public void setPane(JPanel calendarPane) {
			this.calendarPane = calendarPane;
			calendarPane.setLayout(new BorderLayout());
		}

		public void setFrame(JFrame frame) {
			this.frame = frame;
		}

		public void setUser(User user) {
			this.user = user;
		}

	}
}
