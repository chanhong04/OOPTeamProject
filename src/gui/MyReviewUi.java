package gui;

import model.Review;  // ★ Review 클래스 사용

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyReviewUi extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용
    private JPanel reviewListPanel;   // 스크롤 안쪽 패널

    public MyReviewUi(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 상단 ← 버튼
        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(40, 20, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new BackButtonListener());
        add(back);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 70, 1520, 1);
        add(sep);

        JLabel title = new JLabel("내가 쓴 리뷰", SwingConstants.LEFT);
        title.setFont(new Font("Dialog", Font.BOLD, 24));
        title.setBounds(80, 80, 400, 30);
        add(title);

        // 리뷰 리스트 영역 -------------------------------------------------
        reviewListPanel = new JPanel(null);
        reviewListPanel.setBackground(Color.WHITE);

        JScrollPane reviewScroll = new JScrollPane(reviewListPanel);
        reviewScroll.setBounds(80, 120, 1440, 560);
        reviewScroll.setBorder(null);
        reviewScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(reviewScroll);

        // 하단 탭 바 (학식 / 마이페이지) ------------------------------------
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(80, 700, 1440, 90);
        bottomBar.setBackground(new Color(245, 238, 252));

        JButton cafeBtn = new JButton("학식");
        JButton myBtn   = new JButton("마이페이지");
        styleTab(cafeBtn);
        styleTab(myBtn);
        // 현재 화면은 마이페이지 쪽이므로
        myBtn.setForeground(new Color(146, 107, 191));

        CafeMenuButtonListener tabListener = new CafeMenuButtonListener();
        cafeBtn.addActionListener(tabListener);
        myBtn.addActionListener(tabListener);

        bottomBar.add(cafeBtn);
        bottomBar.add(myBtn);
        add(bottomBar);

        // 처음 진입 시 한 번 로딩
        loadMyReviews();
    }

    // MainGUI에서 "MYREVIEW"로 올 때마다 새로고침 할 때 사용
    public void refreshReviews() {
        loadMyReviews();
    }

    // ================== 핵심: 로그인 사용자의 리뷰만 읽어서 출력 ==================
    private void loadMyReviews() {
        reviewListPanel.removeAll();

        // ★ MainGUI에 저장된 로그인 ID 사용 (이름은 너가 쓰는 메서드명에 맞춰서)
        String loginId = mainGUI.getLoginID();   // getLoginId() 라면 여기만 바꿔줘

        int cardW = 1400;
        int cardH = 140;
        int gapY  = 16;

        if (loginId == null || loginId.isEmpty()) {
            JLabel msg = new JLabel("로그인 후 이용해 주세요.", SwingConstants.CENTER);
            msg.setBounds(0, 40, cardW, 30);
            reviewListPanel.add(msg);
            reviewListPanel.setPreferredSize(new Dimension(cardW, 120));
            reviewListPanel.revalidate();
            reviewListPanel.repaint();
            return;
        }

        // 로그인한 사용자의 이름 (Java King 같은) 읽기
        String userName = getUserName(loginId);

        // reviews.txt 에서 로그인한 사용자(review.authorID == loginId) 것만 읽기
        List<Review> myReviews = readReviewsFromFile(loginId);

        if (myReviews.isEmpty()) {
            JLabel msg = new JLabel("작성한 리뷰가 없습니다.", SwingConstants.CENTER);
            msg.setBounds(0, 40, cardW, 30);
            reviewListPanel.add(msg);
            reviewListPanel.setPreferredSize(new Dimension(cardW, 120));
        } else {
            int y = 0;
            for (Review r : myReviews) {
                JPanel card = createReviewCard(cardW, cardH, r, userName);
                card.setBounds(0, y, cardW, cardH);
                reviewListPanel.add(card);
                y += cardH + gapY;
            }
            reviewListPanel.setPreferredSize(new Dimension(
                    cardW,
                    myReviews.size() * (cardH + gapY)
            ));
        }

        reviewListPanel.revalidate();
        reviewListPanel.repaint();
    }

    // reviews.txt 읽어서 loginId가 쓴 리뷰만 Review 객체로 만들어 반환
    private List<Review> readReviewsFromFile(String loginId) {
        List<Review> list = new ArrayList<>();
        File file = new File("reviews.txt");

        if (!file.exists()) {
            System.out.println("reviews.txt 파일을 찾을 수 없습니다.");
            return list;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 형식: 리뷰ID  식당명  메뉴명  userID  rating  메뉴ID  warningNum  content
                String[] p = line.split("\t");
                if (p.length < 8) continue;

                String authorId = p[3].trim();
                if (!loginId.equals(authorId)) {
                    continue; // 다른 사람이 쓴 리뷰는 패스
                }

                Review r = new Review();    // ★ Review.java 사용
                r.setReviewID(p[0].trim());
                r.setCafeteriaName(p[1].trim());
                r.setMenuName(p[2].trim());
                r.setAuthorID(authorId);

                try {
                    r.setRating(Integer.parseInt(p[4].trim()));
                } catch (Exception ignore) {
                    r.setRating(0);
                }

                try {
                    r.setWarningNum(Integer.parseInt(p[6].trim()));
                } catch (Exception ignore) {
                    r.setWarningNum(0);
                }

                r.setContent(p[7].trim());

                list.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // users.txt 에서 userId에 해당하는 이름(Java King 등) 읽기
    private String getUserName(String userId) {
        File file = new File("users.txt");
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 형식: id<TAB>pw<TAB>name<TAB>role<TAB>level
                String[] t = line.split("\t");
                if (t.length < 3) continue;

                String id = t[0].trim();
                if (!id.equals(userId)) continue;

                return t[2].trim();  // 전체 이름 (공백 포함)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 리뷰 카드 하나 그리기
    private JPanel createReviewCard(int w, int h, Review r, String userName) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230, 230, 230)));

        // 1. 상단 : [식당] 메뉴명
        JLabel titleLabel = new JLabel("[" + r.getCafeteriaName() + "] " + r.getMenuName());
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setBounds(18, 12, w - 120, 20);
        card.add(titleLabel);

        // 2. 별점 표시 (★★★☆☆)
        int rating = r.getRating();
        if (rating < 0) rating = 0;
        if (rating > 5) rating = 5;
        String stars = "★".repeat(rating) + "☆".repeat(5 - rating);

        JLabel starLabel = new JLabel(stars);
        starLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        starLabel.setForeground(new Color(255, 153, 0));
        starLabel.setBounds(18, 36, 120, 18);
        card.add(starLabel);

        // 3. 신고 수
        JLabel warnLabel = new JLabel("신고 " + r.getWarningNum(), SwingConstants.RIGHT);
        warnLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        warnLabel.setForeground(new Color(140, 140, 140));
        warnLabel.setBounds(w - 100, 14, 80, 16);
        card.add(warnLabel);

        // 4. 내용
        JLabel contentLabel = new JLabel("<html>" + r.getContent() + "</html>");
        contentLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        contentLabel.setBounds(18, 60, w - 36, 40);
        card.add(contentLabel);

        // 5. 작성자 (Java King (user001) 형태)
        String writerText;
        if (userName != null && !userName.isEmpty()) {
            writerText = userName + " (" + r.getAuthorID() + ")";
        } else {
            writerText = r.getAuthorID();
        }

        JLabel writerLabel = new JLabel(writerText);
        writerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        writerLabel.setForeground(Color.GRAY);
        writerLabel.setBounds(18, 110, 260, 16);
        card.add(writerLabel);

        return card;
    }

    // 하단 탭 버튼 스타일
    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
    }

    // 하단 탭 버튼 리스너
    class CafeMenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "학식":
                    mainGUI.showScreen("CAFETERIA");
                    break;
                case "마이페이지":
                    mainGUI.showScreen("MYPAGE");
                    break;
            }
        }
    }

    // 상단 ← 버튼 (MyPage로 돌아가기)
    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            mainGUI.showScreen("MYPAGE");
        }
    }
}
