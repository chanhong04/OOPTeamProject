package model;

import java.util.ArrayList;
import java.util.Date;

public class ReviewManager {
	ArrayList<Review> reviewList = new ArrayList();
	
	void readReviews(String fileName) {};
	void saveReviews(String fileName) {};
	void writeReview(Review review) {};
	void findReviewsByMenu(String menuName) {};
	void findReviewByUser(String authorId) {};
	/*
	 * 기본적으로 추천 수대로 정렬된 리뷰들을 띄어쓰기와 줄바꿈으로 구분하여 출력해주셨으면 합니다.
	 * 그리고 가능하다면 reviewID, menuName, authorID, rating, content, writtenDate 필드별 검색 등이 가능하면 좋겠습니다.
	 * ex) search("yeon1234 2024.01.25") authorID와 writtenDate가 주어졌을 때 띄어쓰기를 구분자로 하여 두 개 항목을 만족하는 레코드가 검색이 되었으면 합니다.
	 */
	void displayReviews() {}
}
