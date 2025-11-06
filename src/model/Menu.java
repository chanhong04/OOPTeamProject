package model;

public class Menu {
	String name;
	int price;
	int rating; // 메뉴에 해당하는 review들의 rating으로 산정되었으면 합니다. 5점 만점
	String category;
	String description;
	boolean isSoldOut;
	// String imagePath; 메뉴 이미지 경로 시간이 있으면 추가적인 구현
	
	boolean matches(String kwd) {
		return true;
	}
}
