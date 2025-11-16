package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Comparator;

public class ReviewManager {
    private static ReviewManager instance = new ReviewManager();
    private ArrayList<Review> reviewList = new ArrayList<>();

    private ReviewManager() {}
    public static ReviewManager getInstance() {
        return instance;
    }
	
	public void readReviews(String fileName) {
        try (Scanner fileScan = new Scanner(new File(fileName))) {

            reviewList.clear();

            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] parts = line.split("\t");

                if (parts.length != 8) {
                    System.out.println("데이터 형식 오류");
                    continue;
                }

                try { 
                    Review r = new Review();

                    r.setReviewID(parts[0]);
                    r.setCafeteriaName(parts[1]);
                    r.setMenuName(parts[2]);
                    r.setAuthorID(parts[3]);
                    r.setRating(Integer.parseInt(parts[4]));
                    r.setContent(parts[5]);
                    r.setWrittenDate(new Date(Long.parseLong(parts[6])));
                    r.setWarningNum(Integer.parseInt(parts[7]));

                    reviewList.add(r);

                } catch (IllegalArgumentException e) {
                    System.out.println("데이터 형식 오류->"+ e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(fileName + " 파일을 찾을 수 없습니다.");
        }
    }


    public void saveReviews(String reviews) {
        try (PrintWriter pw = new PrintWriter(reviews)) {
            for (Review r : reviewList) {
                String line = String.join("\t",
                        r.getReviewID(),
                        r.getCafeteriaName(),
                        r.getMenuName(),
                        r.getAuthorID(),
                        String.valueOf(r.getRating()),
                        r.getContent(),
                        String.valueOf(r.getWrittenDate().getTime()),
                        String.valueOf(r.getWarningNum())
                );
                pw.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(reviews + " 파일 쓰기 오류: " + e.getMessage());
        }
    }

	public void writeReview(Review review) {
        reviewList.add(review);
        saveReviews("reviews.txt");
    }

    public ArrayList<Review> findReviewsByMenu(String cafeteriaName, String menuName) {
        ArrayList<Review> results = new ArrayList<>();
        for (Review r : reviewList) {
            if (r.matchesMenu(cafeteriaName, menuName)) {
                results.add(r);
            }
        }
        return results;
    }

    public ArrayList<Review> findReviewsByAuthor(String authorId) {
        ArrayList<Review> results = new ArrayList<>();
        for (Review r : reviewList) {
            if (r.matchesAuthor(authorId)) {
                results.add(r);
            }
        }
        return results;
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
        ArrayList<Review> sortedList = new ArrayList<>(reviewList);

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

        for (Review r : reviewList) {
            if (r.getReviewID().equals(reviewID)) {
                reviewToRemove = r;
                break;
            }
        }
        if (reviewToRemove != null) {
            reviewList.remove(reviewToRemove);
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

        for (Review r : reviewList) {
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
}
