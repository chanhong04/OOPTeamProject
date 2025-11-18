package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class MyReviewUi extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용

    public MyReviewUi(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 상단 타이틀 ---------------------------------------------------
        JLabel titleLabel = new JLabel("My Page", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        titleLabel.setBounds(0, 10, 900, 30);
        add(titleLabel);

        // 프로필 영역 ---------------------------------------------------
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBackground(new Color(238, 238, 238));
        profilePanel.setBounds(0, 45, 900, 90);
        add(profilePanel);

        JLabel nameLabel = new JLabel("홍길동", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        nameLabel.setBounds(0, 10, 900, 24);
        profilePanel.add(nameLabel);

        JLabel reportLabel = new JLabel("신고 : 0회");
        reportLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        reportLabel.setBounds(720, 10, 150, 20);
        profilePanel.add(reportLabel);

        JLabel idLabel = new JLabel("ID : asdf1020!");
        idLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        idLabel.setBounds(0, 40, 900, 20);
        profilePanel.add(idLabel);

        // 섹션 타이틀 + 이전 버튼 ----------------------------------------
        JLabel sectionTitle = new JLabel("내가 쓴 리뷰 관리");
        sectionTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        sectionTitle.setBounds(40, 150, 300, 30);
        add(sectionTitle);

        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(840, 150, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(back);

        BackButtonListener backListener = new BackButtonListener();
        back.addMouseListener(backListener);

        // 리뷰 리스트 영역 (스크롤) --------------------------------------
        int viewX = 40, viewY = 190, viewW = 820, viewH = 380;
        JPanel reviewList = new JPanel(null);
        reviewList.setBackground(new Color(245, 245, 245, 0));

        int cardW = viewW - 10;
        int cardH = 150;
        int gapY  = 20;
        int y = 0;

        // 샘플 2개, 내용은 전부 공백 (디자인만)
        for (int i = 0; i < 2; i++) {
            JPanel card = reviewCard(cardW, cardH);
            card.setBounds(0, y, cardW, cardH);
            reviewList.add(card);
            y += cardH + gapY;
        }

        reviewList.setPreferredSize(new Dimension(viewW - 10, y));

        JScrollPane reviewScroll = new JScrollPane(
                reviewList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        reviewScroll.setBounds(viewX, viewY, viewW, viewH);
        reviewScroll.setBorder(null);
        add(reviewScroll);

        // 하단 탭 바 ------------------------------------------------------
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(0, 640, 900, 60);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton cafeBtn = new JButton("학식");
        JButton myBtn   = new JButton("마이페이지");
        styleTab(cafeBtn);
        styleTab(myBtn);

        // 현재 화면이 마이페이지 쪽이므로
        myBtn.setForeground(new Color(146, 107, 191));

        CafeMenuButtonListener tabListener = new CafeMenuButtonListener();
        cafeBtn.addActionListener(tabListener);
        myBtn.addActionListener(tabListener);

        bottomBar.add(cafeBtn);
        bottomBar.add(myBtn);
        add(bottomBar);
    }

    // 리뷰 카드 (모양만, 텍스트는 공백)
    private JPanel reviewCard(int w, int h) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230, 230, 230)));

        // 별점
        JLabel starLabel = new JLabel("");
        starLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        starLabel.setBounds(18, 12, 200, 20);
        card.add(starLabel);

        // 수정 / 삭제
        JLabel editLabel = new JLabel("수정");
        editLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        editLabel.setBounds(w - 90, 14, 30, 16);
        card.add(editLabel);

        JLabel deleteLabel = new JLabel("삭제");
        deleteLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        deleteLabel.setBounds(w - 50, 14, 30, 16);
        card.add(deleteLabel);

        // 리뷰 제목 – 공백
        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setBounds(18, 40, w - 36, 20);
        card.add(titleLabel);

        // 리뷰 내용 – 공백
        JLabel contentLabel = new JLabel("");
        contentLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        contentLabel.setBounds(18, 62, w - 36, 20);
        card.add(contentLabel);

        // 프로필 이미지 자리
        JPanel profile = new JPanel();
        profile.setBackground(new Color(220, 220, 220));
        profile.setBounds(18, 90, 32, 32);
        card.add(profile);

        // 작성자 이름 – 공백
        JLabel writerLabel = new JLabel("");
        writerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        writerLabel.setBounds(60, 92, 120, 16);
        card.add(writerLabel);

        // 날짜 – 공백
        JLabel dateLabel = new JLabel("");
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(140, 140, 140));
        dateLabel.setBounds(60, 108, 120, 16);
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

    // 하단 탭 리스너 (학식 / 마이페이지) → CardLayout 전환
    class CafeMenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = e.getActionCommand();
            switch (name) {
                case "학식":
                    mainGUI.showScreen("CAFETERIA");
                    break;
                case "마이페이지":
                    mainGUI.showScreen("MYPAGE");
                    break;
            }
        }
    }

    // 상단 ← 버튼 리스너 (MyPage로 돌아가기)
    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            mainGUI.showScreen("MYPAGE");
        }
    }
}
