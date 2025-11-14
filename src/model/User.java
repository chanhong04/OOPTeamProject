package model;

import java.util.Scanner;

public class User {
	String userId;
	String password;
	String nickName;
	String role;
	int warningCount;
	
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

	void checkPassword(String pw) {}
}
