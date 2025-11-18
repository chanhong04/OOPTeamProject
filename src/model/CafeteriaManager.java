package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import facade.DataEngineImpl;
import mgr.Factory;


public class CafeteriaManager extends DataEngineImpl<Cafeteria> {

	private static CafeteriaManager instance = null;
	private CafeteriaManager() {}

	public static CafeteriaManager getInstance() {
		if (instance == null) {
			instance = new CafeteriaManager();
		}
		return instance;
	}

	// mList 반환
	public List<Cafeteria> getCafeteriaList() {
		return mList; // cafeteriaList -> mList
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

	@Override
	public void readAll(String fileName, Factory<Cafeteria> fac) {
		super.readAll(fileName, fac);
		this.readMenus("menus.txt");
	}

	private void readMenus(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) continue;

				String[] parts = line.split("\t");

				if (parts.length >= 5) {
					String cafeteriaName = parts[0];
					Cafeteria targetCafeteria = findCafeterias(cafeteriaName); // mList 사용

					if (targetCafeteria != null) {
						String name = parts[1];
						int price = Integer.parseInt(parts[2]);
						String description = parts[3];
						String imagePath = parts[4];

						Menu menu = new Menu(name, price, description, imagePath);
						targetCafeteria.createMenu(menu);
					} else {
						System.err.println("경고: 메뉴의 가게이름 '" + cafeteriaName + "'을(를) cafeterias.txt에서 찾을 수 없습니다.");
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Menu 파일 로드 중 오류: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Menu 파일 형식 오류 (숫자 변환): " + e.getMessage());
		}
	}

	public void saveCafeterias(String fileName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			for (Cafeteria c : mList) { // cafeteriaList -> mList
				bw.write(c.getName() + "\t" + c.getLocation());
				bw.newLine();
			}
		} catch (IOException e) {
			System.err.println("Cafeteria 파일 저장 중 오류: " + e.getMessage());
		}
	}

	public void saveMenus(String fileName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			for (Cafeteria c : mList) { // cafeteriaList -> mList
				String cafeteriaName = c.getName();

				for (Menu m : c.readMenus()) {
					String menuData = String.join("\t",
							m.getName(),
							String.valueOf(m.getPrice()),
							m.getDescription(),
							m.getImagePath()
					);

					bw.write(cafeteriaName + "\t" + menuData);
					bw.newLine();
				}
			}
		} catch (IOException e) {
			System.err.println("Menu 파일 저장 중 오류: " + e.getMessage());
		}
	}

	public Cafeteria findCafeterias(String name) {
		for (Cafeteria c : mList) { // cafeteriaList -> mList
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}


	@Override
	public void addNewRow(String[] uiTexts) {
		// Cafeteria는 아직 GUI에서 직접 추가하는 기능이 없으므로 비워둠
	}
}