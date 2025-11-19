package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Comparator;
import java.util.List;

import mgr.Manager;

public class ReviewManager extends Manager<Review> {
    private static ReviewManager instance = new ReviewManager();

    private ReviewManager() {}

    public static ReviewManager getInstance() {
        return instance;
    }

    public void readReviews(String fileName) {
        // "Review 객체를 어떻게 만드는지(new Review())"를 람다식으로 알려줍니다.
        // Factory 인터페이스의 create() 메서드를 즉석에서 구현한 것입니다.
        readAll(fileName, () -> new Review());
    }


    public void saveReviews(String fileName) {
        try (PrintWriter pw = new PrintWriter(fileName)) {
            // 부모 클래스의 mList를 사용합니다.
            for (Review r : mList) {
                String line = String.join("\t",
                        r.getReviewID(),
                        r.getCafeteriaName(),
                        r.getMenuName(),
                        r.getAuthorID(),
                        String.valueOf(r.getRating()),
                        // 주의: Review.read()에서 nextLine()으로 content를 맨 마지막에 읽으므로
                        // 저장할 때도 content를 반드시 맨 마지막에 둬야 합니다.
                        String.valueOf(r.getWrittenDate().getTime()),
                        String.valueOf(r.getWarningNum()),
                        r.getContent()
                );
                pw.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " 파일 쓰기 오류: " + e.getMessage());
        }
    }

	public void writeReview(Review review) {
        mList.add(review);
        saveReviews("reviews.txt");
    }

    public ArrayList<Review> findReviewsByMenu(String cafeteriaName, String menuName) {
        ArrayList<Review> results = new ArrayList<>();
        for (Review r : mList) {
            if (r.matchesMenu(cafeteriaName, menuName)) {
                results.add(r);
            }
        }
        return results;
    }

    public ArrayList<Review> findReviewsByAuthor(String authorId) {
        ArrayList<Review> results = new ArrayList<>();
        for (Review r : mList) {
            if (r.matchesAuthor(authorId)) {
                results.add(r);
            }
        }
        return results;
    }

    // 주문 내역 등에서 리뷰 존재 여부 확인 (헬퍼 메서드 재사용)
    public boolean doesReviewExist(String authorId, String cafeteriaName, String menuName) {
        for (Review r : mList) {
            if (r.matchesAuthor(authorId) && r.matchesMenu(cafeteriaName, menuName)) {
                return true;
            }
        }
        return false;
    }

    public double getAverageRatingForMenu(String cafeteriaName, String menuName) {
        ArrayList<Review> menuReviews = findReviewsByMenu(cafeteriaName, menuName);

        if (menuReviews.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (Review r : menuReviews) {
            totalRating += r.getRating();
        }
        return (double) totalRating / menuReviews.size();
    }

    public void displayReviews() {
        ArrayList<Review> sortedList = new ArrayList<>(mList);

        sortedList.sort(new Comparator<Review>() {
            @Override
            public int compare(Review r1, Review r2) {
                return Integer.compare(r2.getRating(), r1.getRating());
            }
        });

        System.out.println("\n--- 전체 리뷰 목록 (평점순) --------------------------------------------");
        for (Review r : sortedList) {
            r.print();
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    public boolean deleteReview(String reviewID) {
        Review reviewToRemove = null;

        for (Review r : mList) {
            if (r.getReviewID().equals(reviewID)) {
                reviewToRemove = r;
                break;
            }
        }
        if (reviewToRemove != null) {
            mList.remove(reviewToRemove);
            saveReviews("reviews.txt");
            System.out.println("리뷰가 삭제되었습니다.");
            return true;
        } else {
            System.out.println("리뷰를 찾을 수 없습니다.");
            return false;
        }
    }

    public boolean reportReview(String reviewID) {
        Review reviewToReport = null;

        for (Review r : mList) {
            if (r.getReviewID().equals(reviewID)) {
                reviewToReport = r;
                break;
            }
        }

        if (reviewToReport != null) {
            reviewToReport.incrementWarning();
            saveReviews("reviews.txt");
            System.out.println("리뷰가 신고되었습니다. (현재 신고 획수: " + reviewToReport.getWarningNum() + "회)");
            return true;
        } else {
            System.out.println("신고하려는 리뷰를 찾을 수 없습니다.");
            return false;
        }
    }
    public ArrayList<Review> searchReviews(String kwd) {
        // 부모 클래스(Manager)가 제공하는 findAll 메서드를 사용하여 검색
        // Review.matches(kwd)가 호출됩니다.
        return (ArrayList<Review>) findAll(kwd);
    }
}
