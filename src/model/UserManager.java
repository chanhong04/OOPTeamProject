package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import facade.DataEngineImpl;
import mgr.Factory;

public class UserManager extends DataEngineImpl<User> {
	private static UserManager mgr = null;
	
	ArrayList<User> userList = new ArrayList();
	
	public static UserManager getInstance() {
		if (mgr == null)
			mgr = new UserManager();
		return mgr;
	}
	
	boolean addUser(String userId, String password,
			String nickName,String role) {
		User user = new User(userId,password,nickName,role);
		userList.add(user);
		return true;
	}
	
	@Override
	public Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.println(filename + ": 파일 없음");
			System.exit(0);
		}
		if (filein != null) {
            filein.useDelimiter("\t|\r\n|\n");
        }
		return filein;
	}

	void saveUsers(String fileName) {};
	void findUser(String userId) {};
	void deleteUser(String userId) {}

	@Override
	public void addNewRow(String[] uiTexts) {
		User u = new User();
		u.set(uiTexts);
		mList.add(u);
	};
}