package lectureContent;

import java.util.Date;

public class Notice extends Content {

	public Notice() {

	}

	public Notice(String code, String contentKey, Date date, String tmp) {
		this.code = code;
		this.contentKey = contentKey;
		this.date = date;
		this.body = tmp;
	}

	@Override
	void printContentType() {
		System.out.print("[공지]");
	}

}
