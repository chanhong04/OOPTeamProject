package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

import facade.DataEngineImpl;
import mgr.Factory;

public class UserManager extends DataEngineImpl<User> {
	private static UserManager mgr = null;
	
	public static UserManager getInstance() {
		if (mgr == null)
			mgr = new UserManager();
		return mgr;
	}

	boolean addUser(String userId, String password, String nickName, String role) {
		User user = new User(userId, password, nickName, role);
		mList.add(user);
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

	public void saveUsers(String fileName) {
		try {
			FileWriter fw = new FileWriter(fileName);
			for (User u : mList) {
				String line = u.getId() + "\t" + u.password + "\t" + u.getName() + "\t" + u.getRole() + "\t" + u.warningCount + "\n";
				fw.write(line);
			}
			fw.close();
			System.out.println("파일 저장 완료: " + fileName);

		} catch (IOException e) {
			System.out.println("파일 저장 실패");
			e.printStackTrace();
		}
	}
	void findUser(String userId) {};
	void deleteUser(String userId) {}

	@Override
	public void addNewRow(String[] uiTexts) {
		User u = new User();
		u.set(uiTexts);
		mList.add(u);
	};
}