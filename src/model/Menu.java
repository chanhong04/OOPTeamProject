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
	// *** 추가된 부분 시작 ***
	private String category; // 메뉴 분류 (예: 한식, 중식)
	// *** 추가된 부분 끝 ***
	private String imagePath;
	// private boolean isSoldOut; // 품절 여부 - 추후 추가를 위한 주석 처리

	// *** 수정된 생성자: category 필드 추가 ***
	public Menu(String name, int price, String description, String category, String imagePath) {
		this.name = name;
		this.price = price;
		this.description = description;
		// *** 추가된 부분 시작 ***
		this.category = category;
		// *** 추가된 부분 끝 ***
		this.imagePath = imagePath;
		// this.isSoldOut = isSoldOut; // 품절 여부 - 추후 추가를 위한 주석 처리
	}

	// --- 3. Getters ---
	public String getName() { return name; }
	public int getPrice() { return price; }
	public String getDescription() { return description; }
	// *** 추가된 Getter ***
	public String getCategory() { return category; }
	// *** 추가된 Getter 끝 ***
	public String getImagePath() { return imagePath; }
	// public boolean isSoldOut() { return isSoldOut; } // 품절 여부 - 추후 추가를 위한 주석 처리

	// --- 4. Setters ---
	public void setName(String name) { this.name = name; }
	public void setPrice(int price) { this.price = price; }
	public void setDescription(String description) { this.description = description; }
	// *** 추가된 Setter ***
	public void setCategory(String category) { this.category = category; }
	// *** 추가된 Setter 끝 ***
	public void setImagePath(String imagePath) { this.imagePath = imagePath; }
	// public void setSoldOut(boolean soldOut) { this.isSoldOut = soldOut; } // 품절 여부 - 추후 추가를 위한 주석 처리

	// --- 5. 유틸리티 메소드 ---

	/**
	 * [수정됨] menus.txt에 저장될 때 사용될 형식 (탭 구분)
	 * 형식: 메뉴명(탭)가격(탭)설명(탭)메뉴분류(탭)사진경로
	 */
	// *** toString() 수정: category 필드 추가 ***
	@Override
	public String toString() {
		return String.join("\t",
				name,
				String.valueOf(price),
				description,
				category, // *** category 추가 ***
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