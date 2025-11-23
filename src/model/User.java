package model;

import java.util.ArrayList;
import java.util.Scanner;

import facade.UIData;
import mgr.Manageable;

public class User implements Manageable, UIData {
	String userId;
	String password;
	String nickName;
	String role;
	int warningCount;
	private ArrayList<String> likeMenus;
	
	public User() {}
	public User(String userId, String password, String nickName, String role) {
		this.userId = userId;
		this.password = password;
		this.nickName = nickName;
		this.role = role;
	}
	
	public void read(Scanner scan) {
		userId = scan.next();
		password = scan.next();
		nickName = scan.next();
		role = scan.next();
		warningCount = scan.nextInt();
	}
	
	public void print() {
		System.out.format("[%s] (경고%d회) ", userId, warningCount);
		System.out.println();
	}
	
	public boolean matches(String kwd) {
		return true;
	}
	
	void checkPassword(String pw) {}
	
	public void set(String[] uitexts) {
		this.userId = uitexts[0];
		this.password = uitexts[1];
		this.nickName = uitexts[2];
		this.role = uitexts[3];
	};

	public String[] getUiTexts() {
		String[] texts = new String[5];
		texts[0] = userId;
		texts[1] = password;
		texts[2] = nickName;
		texts[3] = role;
		texts[4] = String.valueOf(warningCount);
		return texts;
	}
	
	public void addLike(String menuName) {
        if (!likeMenus.contains(menuName)) {
            likeMenus.add(menuName);
        }
    }

    public void removeLike(String menuName) {
        likeMenus.remove(menuName);
    }

    public boolean isLiked(String menuName) {
        return likeMenus.contains(menuName);
    }

    public ArrayList<String> getLikeMenus() {
        return likeMenus;
    }
}
