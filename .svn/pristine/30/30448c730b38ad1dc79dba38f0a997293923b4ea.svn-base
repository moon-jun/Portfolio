package mgr;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager<T extends Manageable> {
	public ArrayList<T> mList = new ArrayList<>();

	public T find(String kwd, boolean isSearch) { // 검색 기능을 사용할 경우 true, 기본키를 검사하여 객체 하나만을 가져올 때는 false;
		for (T m : mList)
			if (m.matches(kwd, isSearch))
				return m;
		return null;
	}

	public void delete(T m) { // 리스트에서 특정 객체 삭제
		mList.remove(m);
	}

	public void read(T m) {
		mList.add(m);
	}

	public void readAll(String filename, Factory<T> fac) {
		Scanner filein = openFile(filename);
		while (filein.hasNext()) {
			T t = fac.create();
			t.read(filein);
			mList.add(t);
		}
		filein.close();
	}

	public void printAll() {
		for (T m : mList) {
			m.print();
		}
	}

	public Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.println(filename + ": 파일 없음");
			System.exit(0);
		}
		return filein;
	}
}
