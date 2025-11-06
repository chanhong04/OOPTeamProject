package model;

import java.util.ArrayList;

public class Cafeteria {
	String name;
	ArrayList<Menu> menuList; // 메뉴 리스트가 각 메뉴 인스턴스에 해당하는 리뷰의 평점을 반영하여 평점 순으로 정렬이 되면 좋겠습니다. 
	
	void createMeues() {}
	void readMenus() {}
	void updateMenus() {}
	void deleteMenu() {}
	/*
	 * 기본적으로 name을 기준으로 검색이 가능하면 좋겠습니다.
	 * 추가적으로는 name, price, like, category, description를 기준으로 reviewManger class 주석의 예시와 같은 검색이 가능하면 좋습니다.
	 *  isSoldOut을 반영하여 만약 true라면 걸러지도록 하면 좋을 것 같습니다. 
	 */
	void SearchMenu() {}
}
