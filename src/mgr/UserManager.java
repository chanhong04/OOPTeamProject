package mgr;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import model.User;

public class UserManager {
	private static UserManager mgr = null;
	
	ArrayList<User> userList = new ArrayList();
	
	public static UserManager getInstance() {
		if (mgr == null)
			mgr = new UserManager();
		return mgr;
	}
	
	boolean addUser(String userId, String password,
			String nickName,String role) {
		User user = new User("abc","abc","abc","abc");
		userList.add(user);
		return true;
	}
	
	public void readAll(String filename) {
		Scanner filein = openFile(filename);
		filein.useDelimiter("\t|\r\n|\n"); // 구분자를 tab, 줄바꿈으로 바꾸는 명령어
		User m = null;
		while (filein.hasNext()) {
			m = new User();
			m.read(filein);
			userList.add(m);
		}
		filein.close();
	}
	
	public void printAll() {
		for(User user : userList) {
			user.print();
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
	
	void readUsers(String fileName) {
		
	};
	void saveUsers(String fileName) {};
	void findUser(String userId) {};
	void deleteUser(String userId) {};
}