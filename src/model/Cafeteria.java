package model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import mgr.Manageable;
import facade.UIData;

public class Cafeteria implements Manageable, UIData {

	private String name;     // 가게 이름 (Key 역할)
	private String location; // 가게 위치
	private ArrayList<Menu> menuList; // 해당 가게의 메뉴 목록

	//기본 생성자 Manager.readAll()의 Factory가 객체 생성을 위해 필요

	public Cafeteria() {
		this.menuList = new ArrayList<>();
	}


	public Cafeteria(String name, String location) {
		this.name = name;
		this.location = location;
		this.menuList = new ArrayList<>();
	}

	public String getName() { return name; }
	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	@Override
	public String toString() {
		return String.join("\t", name, location);
	}

	public void createMenu(Menu menu) {
		menuList.add(menu);
	}

	public ArrayList<Menu> readMenus() {
		return menuList;
	}

	public Menu findMenu(String menuName) {
		for (Menu m : menuList) {
			if (m.getName().equals(menuName)) {
				return m;
			}
		}
		return null;
	}

	public boolean updateMenu(String originalMenuName, Menu updatedMenu) {
		return false;
	}

	public boolean deleteMenu(String menuName) {
		return false;
	}

	public ArrayList<Menu> searchMenu(String... keywords) {
		ArrayList<Menu> results = new ArrayList<>();
		for (Menu menu : this.menuList) {
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

	@Override
	public void read(Scanner scan) {
		this.name = scan.next();
		this.location = scan.next();
	}

	@Override
	public void print() {
		System.out.printf("\n[가게: %s (%s)]\n", name, location);
		if (menuList.isEmpty()) {
			System.out.println("  (메뉴 정보 없음)");
			return;
		}
		for(Menu m : menuList) {
			System.out.printf("  - %s (%,d원) [%s]: %s\n",
					m.getName(),
					m.getPrice(),
					m.getCategory(),
					m.getDescription());

		}
	}

	//키워드가 가게 이름/위치와 일치하는지 확인
	@Override
	public boolean matches(String kwd) {
		if (kwd == null || kwd.trim().isEmpty()) {
			return false;
		}
		if (this.name.contains(kwd) || this.location.contains(kwd)) {
			return true;
		}
		return false;
	}

	@Override
	public void set(String[] uitexts) {
		this.name = uitexts[0];
		this.location = uitexts[1];
	}

	@Override
	public String[] getUiTexts() {
		return new String[]{ name, location };
	}
}