package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

//가게(코너)의 정보와, 해당 가게의 '메뉴 리스트'를 함께 갖습니다.
public class Cafeteria {

	private String name;     // 가게 이름 (Key 역할)
	private String location; // 가게 위치
	private ArrayList<Menu> menuList; // 해당 가게의 메뉴 목록

	// --- 2. 생성자 ---
	public Cafeteria(String name, String location) {
		this.name = name;
		this.location = location;
		this.menuList = new ArrayList<>(); // 빈 리스트 초기화
	}

	// --- 3. Getters ---
	public String getName() { return name; }
	public String getLocation() { return location; }

	// --- 4. Setters ---
	public void setLocation(String location) { this.location = location; }

	// --- 5. CafeteriaManager가 파일 저장 시 사용할 toString ---
	@Override
	public String toString() {
		return String.join("\t", name, location);
	}

	// --- 6. 메뉴 관리 메소드들 ---

	//[CafeteriaManager용]
	//CafeteriaManager가 menus.txt에서 읽어온 메뉴를 이 객체에 추가할 때 사용

	public void createMenu(Menu menu) {
		menuList.add(menu);
	}

	 //해당 가게의 모든 메뉴 리스트를 반환
	public ArrayList<Menu> readMenus() {
		return menuList;
	}

	//(내부 사용) 이름으로 메뉴 객체를 찾아 반환
	public Menu findMenu(String menuName) {
		for (Menu m : menuList) {
			if (m.getName().equals(menuName)) {
				return m;
			}
		}
		return null;
	}

	//메뉴 정보 업데이트
	public boolean updateMenu(String originalMenuName, Menu updatedMenu) {
        /* // 추후 개발을 위한 주석처리
        Menu menuToUpdate = findMenu(originalMenuName);
        if (menuToUpdate != null) {
           menuToUpdate.setName(updatedMenu.getName());
           menuToUpdate.setPrice(updatedMenu.getPrice());
           menuToUpdate.setDescription(updatedMenu.getDescription());
           menuToUpdate.setImagePath(updatedMenu.getImagePath());
           return true;
        }
        */
		return false;
	}

	//메뉴 이름으로 특정 메뉴를 리스트에서 삭제
	public boolean deleteMenu(String menuName) {
        /* // 추후 개발을 위한 주석처리
        Iterator<Menu> iterator = menuList.iterator();
        while (iterator.hasNext()) {
           Menu menu = iterator.next();
           if (menu.getName().equals(menuName)) {
              iterator.remove();
              return true;
           }
        }
        */
		return false;
	}

	 //[수정됨] 메뉴 검색 기능
	public ArrayList<Menu> searchMenu(String... keywords) {
		ArrayList<Menu> results = new ArrayList<>();
		for (Menu menu : this.menuList) {
          /*
          // 품절 여부 - 추후 추가를 위한 주석 처리
          if (menu.isSoldOut()) {
             continue;
          }
          */
			String searchableData = (
					menu.getName() + " " +
							menu.getDescription() + " " +
							menu.getPrice()
			).toLowerCase();

			boolean allKeywordsMatch = true;
			for (String kwd : keywords) {
				if (kwd == null || kwd.trim().isEmpty()) continue;
				if (!searchableData.contains(kwd.toLowerCase().trim())) {
					allKeywordsMatch = false;
					break;
				}
			}
			if (allKeywordsMatch) {
				results.add(menu);
			}
		}
		return results;
	}

	// 객체 비교 (이름 기준)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cafeteria cafeteria = (Cafeteria) o;
		return Objects.equals(name, cafeteria.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}