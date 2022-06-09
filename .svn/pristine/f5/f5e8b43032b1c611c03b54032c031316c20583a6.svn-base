package univ;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarManager {
	String WEEK_DAY_NAME[] = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" }; // 월~일 배열
	final int calLastDateOfMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }; // 1~12월의 끝날짜를 배열에담음

	final int CAL_WIDTH = 7;
	final int CAL_HEIGHT = 6;

	int dayofweek = 7; // 월의 시작일 일요일=0
	int lastday; // 월의 마지막날
	int calStartingPos; // 월의 시작일

	int calYear;
	int calMonth;
	int calDayOfMon;
	Calendar cal;
	Calendar today = Calendar.getInstance(); // 현재 날짜에 대한 정보를 가져옴

	public void CalendarManger() {
		setToday();

	}

	public void setToday() {
		calYear = today.get(Calendar.YEAR);
		calMonth = today.get(Calendar.MONTH);
		calDayOfMon = today.get(Calendar.DAY_OF_MONTH);
		makeCalData(today);
	}

	private void makeCalData(Calendar cal) {
		calStartingPos = (cal.get(Calendar.DAY_OF_WEEK) + 7 - (cal.get(Calendar.DAY_OF_MONTH)) % 7) % 7;
		if (calMonth == 0) // 1월이라면 윤년인지 아닌지 검사
			lastday = calLastDateOfMonth[calMonth] + leapCheck(calYear);
		else
			lastday = calLastDateOfMonth[calMonth];
	}

	public void moveMonth(int mon) { // 현재달로 부터 n달 전후를 받아 달력 배열을 만드는 함수(1년은 +12, -12달로 이동 가능)
		calMonth += mon;
		if (calMonth > 11)
			while (calMonth > 11) {
				calYear++;
				calMonth = 0;
			}
		else if (calMonth < 0)
			while (calMonth < 0) {
				calYear--;
				calMonth = 11;
			}
		cal = new GregorianCalendar(calYear, calMonth, calDayOfMon);
		makeCalData(cal);
	}

	private int leapCheck(int year) { // 윤년인지 확인하는 함수
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
			return 1;
		else
			return 0;
	}

	public int getYear() {
		return calYear;
	}

	public int getMonth() {
		return calMonth + 1;
	}

	public int getDate() {
		return calDayOfMon;
	}
}
