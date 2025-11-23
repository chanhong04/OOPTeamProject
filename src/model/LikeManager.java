package model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LikeManager {
	/*
    private String fileName = "likes.txt";

    // 1. 좋아요 저장 (파일 뒤에 이어쓰기 - append)
    public void addLike(String userId, String cafeName, String menuName) {
        // 이미 좋아요 한 상태인지 메모리상에서 먼저 체크하는 로직 필요 (생략)

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // true 옵션: 덮어쓰지 않고 뒤에 추가(append)함
            String line = userId + "\t" + cafeName + "\t" + menuName;
            writer.write(line);
            writer.newLine(); // 줄바꿈
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. 좋아요 삭제 (파일 전체 다시 쓰기)
    public void removeLike(String userId, String cafeName, String menuName) {
        // 1. 파일 내용을 전부 읽어서 임시 리스트에 담음
        List<String> lines = new ArrayList<>();
        try (Scanner scan = new Scanner(new File(fileName))) {
            scan.useDelimiter("\t|\r\n|\n");
            while(scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
        } catch (FileNotFoundException e) { return; }

        // 2. 삭제하려는 내용과 일치하지 않는 줄만 다시 파일에 씀
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                // 탭으로 쪼개서 비교
                String[] parts = line.split("\t");
                if (parts.length < 3) continue;

                // ID, 식당, 메뉴가 모두 일치하면 -> 삭제 대상이므로 파일에 안 씀(skip)
                if (parts[0].equals(userId) && parts[1].equals(cafeName) && parts[2].equals(menuName)) {
                    continue; 
                }
                
                // 일치하지 않는(살아남은) 데이터만 다시 씀
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 3. 로딩 (프로그램 시작 시)
    public void loadLikes(UserManager userMgr) {
        try (Scanner scan = new Scanner(new File(fileName))) {
             scan.useDelimiter("\t|\r\n|\n");
             while(scan.hasNext()) {
                 String uid = scan.next();
                 String cafe = scan.next(); // 식당명도 읽지만 User 객체엔 메뉴명만 넣을 경우
                 String menu = scan.next();
                 
                 User u = userMgr.findUser(uid);
                 if(u != null) {
                     // User 객체에 좋아요 정보 추가
                     // (식당 이름까지 구분해서 저장하려면 User의 likeList 구조를 좀 바꿔야 할 수 있음)
                     u.addLikeMenu(menu); 
                 }
             }
        } catch (Exception e) { }
    }
    */
}
