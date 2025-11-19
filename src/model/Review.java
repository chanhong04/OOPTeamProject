package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import mgr.Manageable;

public class Review implements Manageable {
	private String reviewID; // review 식별을 위한 고유 ID
    private String cafeteriaName; //음식점 이름
    private String menuName;  //리뷰 작성한 메뉴(주문한 메뉴 중에)
    private String authorID; //작성자 아이디
    private int rating;  //평점(1~5)
    private String content;  //리뷰 내용
    private Date writtenDate;  //작성일
    private int warningNum; //신고횟수

    //기본 생성자
    public Review(){}

    //생성자
    public Review(String reviewID,String cafeteriaName, String menuName, String authorID, int rating, String content, Date writtenDate) {
        this.reviewID = reviewID;
        this.cafeteriaName = cafeteriaName;
        this.menuName = menuName;
        this.authorID = authorID;
        setRating(rating);
        this.content = content;
        this.writtenDate = writtenDate;
        this.warningNum = 0;
    }

    @Override
    public void read(Scanner scan) {
        if (!scan.hasNextLine()) return;

        String line = scan.nextLine();

        String[] parts = line.split("\t");

        if (parts.length < 8) {
            System.err.println("경고: 리뷰 데이터 누락 또는 형식 오류");
            return;
        }

        try {
            this.reviewID = parts[0];
            this.cafeteriaName = parts[1];
            this.menuName = parts[2];
            this.authorID = parts[3];

            setRating(Integer.parseInt(parts[4]));

            this.writtenDate = new Date(Long.parseLong(parts[5]));
            this.warningNum = Integer.parseInt(parts[6]);

            this.content = parts[7];

        } catch (NumberFormatException e) {
            System.err.println("오류: 숫자 필드 변환 실패 " );
        }
    }

    @Override
    public void print() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String dateStr = (writtenDate != null) ? sdf.format(writtenDate) : "N/A";

        System.out.printf("[%s] %s - %s (★%d, 신고:%d) | %s | 작성자:%s (%s)\n",
                reviewID, cafeteriaName, menuName, rating, warningNum,
                content, authorID, dateStr);
    }

    @Override
    public boolean matches(String kwd) {
        if (kwd == null) return false;

        // 평점 검색 (숫자일 경우)
        if (kwd.length() == 1 && Character.isDigit(kwd.charAt(0))) {
            if (Integer.toString(rating).equals(kwd)) return true;
        }

        // 문자열 필드 검색 (포함 여부)
        if (menuName.contains(kwd)) return true;
        if (cafeteriaName.contains(kwd)) return true;
        if (content.contains(kwd)) return true;
        if (authorID.equals(kwd)) return true; // ID는 정확히 일치할 때만

        return false;
    }

    //메뉴별로 리뷰를 구별하여 출력하기 위한 matches 함수
    public boolean matchesMenu(String cafeteriaName, String menuName) {
        if (this.cafeteriaName == null || this.menuName == null) {
            return false;
        }
        // 둘 다 일치해야 함
        return this.cafeteriaName.equals(cafeteriaName) && this.menuName.equals(menuName);
    }

    //내가 쓴 리뷰 관리/주문한 내역의 리뷰 작성 유무를 표시하기 위한 matches 함수
    public boolean matchesAuthor(String authorId) {
        if (this.authorID == null) {
            return false;
        }
        return this.authorID.equals(authorId);
    }

    public void incrementWarning() {
        this.warningNum++;
    }

    //Getter 메서드(review.txt 파일을 읽어올 때 사용)
    //Setter 메서드(review.txt에 값을 넣거나 수정할 때 사용)

    //Getter 메서드(읽기 전용)
    public String getReviewID() { return reviewID; }
    public String getCafeteriaName() { return cafeteriaName; }
    public String getMenuName() { return menuName; }
    public String getAuthorID() { return authorID; }
    public int getRating() { return rating; }
    public String getContent() { return content; }
    public Date getWrittenDate() { return writtenDate; }
    public int getWarningNum() { return warningNum; }

    //Setter 메서드(쓰기 전용)
    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
    public void setCafeteriaName(String cafeteriaName) {
        this.cafeteriaName = cafeteriaName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) { // 1~5점 사이의 별점 부여
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("오류: 평점은 1~5 사이만 가능합니다."); //범위밖의 별점 입력시 비정상 종료(그럴일 없긴 해)
        }
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setWrittenDate(Date writtenDate) {
        this.writtenDate = writtenDate;
    }
    public void setWarningNum(int warningNum) {
        this.warningNum = warningNum;
    }

}
