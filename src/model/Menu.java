package model;

import java.util.Objects;

/**
 * [수정됨] 개별 메뉴의 정보만 담는 클래스.
 * '품절여부' 필드 및 로직 제거됨.
 */
public class Menu {

	private String name;
	private int price;
	private String description; //메뉴 설명
	private String imagePath;
	// private boolean isSoldOut; // 품절 여부 - 추후 추가를 위한 주석 처리

	public Menu(String name, int price, String description, String imagePath) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.imagePath = imagePath;
		// this.isSoldOut = isSoldOut; // 품절 여부 - 추후 추가를 위한 주석 처리
	}

	// --- 3. Getters ---
	public String getName() { return name; }
	public int getPrice() { return price; }
	public String getDescription() { return description; }
	public String getImagePath() { return imagePath; }
	// public boolean isSoldOut() { return isSoldOut; } // 품절 여부 - 추후 추가를 위한 주석 처리

	// --- 4. Setters ---
	public void setName(String name) { this.name = name; }
	public void setPrice(int price) { this.price = price; }
	public void setDescription(String description) { this.description = description; }
	public void setImagePath(String imagePath) { this.imagePath = imagePath; }
	// public void setSoldOut(boolean soldOut) { this.isSoldOut = soldOut; } // 품절 여부 - 추후 추가를 위한 주석 처리

	// --- 5. 유틸리티 메소드 ---

	/**
	 * [수정됨] menus.txt에 저장될 때 사용될 형식 (탭 구분)
	 * *주의: 이 toString()은 '가게이름'을 포함하지 않습니다.*
	 * 형식: 메뉴명(탭)가격(탭)설명(탭)사진경로
	 */
	//가게 이름만 비교
	@Override
	public String toString() {
		return String.join("\t",
				name,
				String.valueOf(price),
				description,
				imagePath
				// String.valueOf(isSoldOut) // 품절 여부 - 추후 추가를 위한 주석 처리
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Menu menu = (Menu) o;
		return Objects.equals(name, menu.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}