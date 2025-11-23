package gui;

import model.Review;
import model.ReviewManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat; //("yyyy.MM.dd")포멧으로 변환
import java.util.ArrayList;

public class MenuDetailUI extends JFrame {

    private MainGUI mainGUI;  // 카드레이아웃 전환용
    //메뉴별로 출력할 때 필터링을 위해 필요
    private String cafeteriaName; // 식당 이름
    private String menuName;      // 메뉴 이름

    // 생성자
    public MenuDetailUI(MainGUI mainGUI, String cafeteriaName, String menuName) {
        this.mainGUI = mainGUI;
        this.cafeteriaName = cafeteriaName;
        this.menuName = menuName;
        initUI();
    }

    // 테스트용 생성자(임의로 한식당의 제육덮밥을 넣음)
    // 예전처럼 단독 실행할 수 있게 기본 생성자도 유지 (cardLayout 연동 X)
    public MenuDetailUI() {
        this(null, "한식당", "제육덮밥"); // 테스트하고 싶은 메뉴를 여기에 적으세요!
    }

    private void initUI() {
        setTitle("메뉴 상세");
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.WHITE);

        // --- 상단 바 ---
        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(40, 20, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cp.add(back);

        back.addMouseListener(new BackButtonListener());

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 70, 820, 1);
        cp.add(sep);

        // --- 상단 : 메뉴 정보 ---
        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(new Color(230,230,230));
        imgPanel.setBorder(new LineBorder(new Color(210,210,210)));
        imgPanel.setBounds(80, 100, 140, 140);
        cp.add(imgPanel);

        JLabel menuNameLabel = new JLabel(menuName); // 전달받은 메뉴 이름 표시
        menuNameLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        menuNameLabel.setBounds(250, 105, 300, 32);
        cp.add(menuNameLabel);

        // 평균 평점 계산 및 표시
        ReviewManager rm = ReviewManager.getInstance();
        double avgRating = rm.getAverageRatingForMenu(cafeteriaName, menuName);

        JLabel ratingLabel = new JLabel(String.format("평점: %.1f / 5.0", avgRating));
        ratingLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        ratingLabel.setForeground(new Color(255, 153, 0)); // 주황색 별 느낌
        ratingLabel.setBounds(250, 145, 200, 30);
        cp.add(ratingLabel);

        //주문 버튼 삭제
//        JButton orderBtn = new JButton("주문");
//        orderBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
//        orderBtn.setBounds(650, 130, 120, 40);
//        orderBtn.setBackground(new Color(146,107,191));
//        orderBtn.setForeground(Color.WHITE);
//        orderBtn.setFocusPainted(false);
//        orderBtn.setBorderPainted(false);
//        cp.add(orderBtn);

        // 리뷰 영역
        // ReviewManager에서 해당 메뉴의 리뷰들 가져오기
        ArrayList<Review> reviewList = rm.findReviewsByMenu(cafeteriaName, menuName);

        int viewX = 60, viewY = 260, viewW = 780, viewH = 300;
        JPanel reviewListPanel = new JPanel(null);
        reviewListPanel.setBackground(Color.WHITE);

        int cardW = viewW - 20;
        int cardH = 150;
        int gapY  = 20;
        int y = 0;

        // 2. 존재하는 리뷰 개수만큼 카드 생성/ 없으면 없음 문구 출력
        if (reviewList.isEmpty()) {
            JLabel noReview = new JLabel("작성된 리뷰가 없습니다.", SwingConstants.CENTER);
            noReview.setBounds(0, 50, cardW, 30);
            reviewListPanel.add(noReview);
        } else {
            // 리뷰 객체를 이용해 카드 생성
            for (Review r : reviewList) {
                JPanel rc = reviewCard(cardW, cardH, r);
                rc.setBounds(0, y, cardW, cardH);
                reviewListPanel.add(rc);
                y += cardH + gapY;
            }
        }

        reviewListPanel.setPreferredSize(new Dimension(viewW - 20, Math.max(y, viewH))); // 스크롤 영역 크기 설정

        JScrollPane reviewScroll = new JScrollPane(
                reviewListPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        reviewScroll.setBounds(viewX, viewY, viewW, viewH);
        reviewScroll.setBorder(null);
        reviewScroll.getVerticalScrollBar().setUnitIncrement(16); // 스크롤 속도 개선
        cp.add(reviewScroll);

        // --- 하단 탭 ---
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(60, 590, 780, 60);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton cafeBtn = new JButton("학식");
        JButton myBtn   = new JButton("마이페이지");
        styleTab(cafeBtn);
        styleTab(myBtn);

        TabButtonListener tabListener = new TabButtonListener();
        cafeBtn.addActionListener(tabListener);
        myBtn.addActionListener(tabListener);

        bottomBar.add(cafeBtn);
        bottomBar.add(myBtn);
        cp.add(bottomBar);

        setVisible(true);
    }

    private JPanel reviewCard(int w, int h, Review r) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230,230,230)));

        // 별점 표시
        String stars = "★".repeat(r.getRating()) + "☆".repeat(5 - r.getRating());
        JLabel starLabel = new JLabel(stars);
        starLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        starLabel.setForeground(new Color(255, 153, 0));
        starLabel.setBounds(18, 12, 100, 20);
        card.add(starLabel);

        // 신고 버튼
        JLabel reportLabel = new JLabel("신고"+ r.getWarningNum());
        reportLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        reportLabel.setForeground(new Color(140,140,140));
        reportLabel.setBounds(w - 80, 14, 60, 16);
        reportLabel.setHorizontalAlignment(SwingConstants.RIGHT); // 오른쪽 정렬
        reportLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 클릭 이벤트 리스너 추가
        reportLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 확인 팝업 띄우기 (예/아니오)
                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "이 리뷰를 신고하시겠습니까?",
                        "신고 확인",
                        JOptionPane.YES_NO_OPTION
                );

                // '예'를 눌렀을 때 처리
                if (answer == JOptionPane.YES_OPTION) {
                    boolean success = ReviewManager.getInstance().reportReview(r.getReviewID());

                    if (success) {
                        // UI 즉시 갱신: 라벨 텍스트를 새로운 신고 횟수로 변경
                        reportLabel.setText("신고 " + r.getWarningNum());
                        JOptionPane.showMessageDialog(null, "정상적으로 신고되었습니다.");
                    }
                }
            }
        });
        card.add(reportLabel);

        // 메뉴 이름 표시
        JLabel titleLabel = new JLabel(r.getMenuName());
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setBounds(18, 40, w - 36, 20);
        card.add(titleLabel);

        // 리뷰 내용 표시(HTML 태그로 줄바꿈 지원)
        JLabel contentLabel = new JLabel("<html>" + r.getContent() + "</html>"); // 줄바꿈 지원
        contentLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        contentLabel.setBounds(18, 65, w - 36, 40); // 높이 늘림
        card.add(contentLabel);

        // 작성자 ID(r.getAuthorID()) 표시
        JLabel writerLabel = new JLabel(r.getAuthorID());
        writerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        writerLabel.setBounds(18, 112, 120, 16);
        card.add(writerLabel);

        // 작성 날짜(r.getWrittenDate()) 표시
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        JLabel dateLabel = new JLabel(sdf.format(r.getWrittenDate()));
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(140,140,140));
        dateLabel.setBounds(18, 128, 120, 16);
        card.add(dateLabel);

        return card;
    }

    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    class TabButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainGUI == null) return;
            String name = e.getActionCommand();
            switch (name) {
                case "학식":
                    mainGUI.showScreen("CAFETERIA");
                    dispose();
                    break;
                case "마이페이지":
                    mainGUI.showScreen("MYPAGE");
                    dispose();
                    break;
            }
        }
    }

    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (mainGUI != null) {
                mainGUI.showScreen("CAFE1");
            }
            dispose();
        }
    }

    public static void main(String[] args) {

//        SwingUtilities.invokeLater(MenuDetailUI::new);
        // 단독 실행 시 "한식당"의 "제육덮밥" 리뷰로 테스트하기 위해 테스트 생성자 호출
        // //=>나중에 위에 주석 풀고 아래 코드는 삭제
        SwingUtilities.invokeLater(() -> {
            new MenuDetailUI(null, "한식당", "제육덮밥");
        });
    }
}