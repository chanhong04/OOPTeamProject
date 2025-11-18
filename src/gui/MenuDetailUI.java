package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

class MenuDetailUI extends JFrame {

    private MainGUI mainGUI;   // 카드레이아웃 전환용

    public MenuDetailUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        initUI();
    }

    // 예전처럼 단독 실행할 수 있게 기본 생성자도 유지 (cardLayout 연동 X)
    public MenuDetailUI() {
        this(null);
    }

    private void initUI() {
        setTitle("메뉴 상세");
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.WHITE);

        // 상단 바 -------------------------------------------------
        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(40, 20, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cp.add(back);

        back.addMouseListener(new BackButtonListener());

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 70, 820, 1);
        cp.add(sep);

        // 상단 : 이미지 + 메뉴 정보 + 주문 버튼 ---------------------
        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(new Color(230,230,230));
        imgPanel.setBorder(new LineBorder(new Color(210,210,210)));
        imgPanel.setBounds(80, 100, 140, 140);
        cp.add(imgPanel);

        JLabel menuName = new JLabel("메뉴 이름");
        menuName.setFont(new Font("Dialog", Font.BOLD, 24));
        menuName.setBounds(250, 105, 300, 32);
        cp.add(menuName);

        JLabel menuDesc = new JLabel("");
        menuDesc.setFont(new Font("Dialog", Font.PLAIN, 14));
        menuDesc.setForeground(new Color(120,120,120));
        menuDesc.setBounds(250, 140, 300, 20);
        cp.add(menuDesc);

        JLabel rating = new JLabel("");
        rating.setFont(new Font("Dialog", Font.PLAIN, 18));
        rating.setBounds(250, 170, 200, 30);
        cp.add(rating);

        JButton orderBtn = new JButton("주문");
        orderBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
        orderBtn.setBounds(650, 130, 120, 40);
        orderBtn.setBackground(new Color(146,107,191));
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setFocusPainted(false);
        orderBtn.setBorderPainted(false);
        cp.add(orderBtn);

        // 리뷰 영역 (스크롤) ---------------------------------------
        int viewX = 60, viewY = 260, viewW = 780, viewH = 300;
        JPanel reviewList = new JPanel(null);
        reviewList.setBackground(new Color(245,245,245,0));

        int cardW = viewW - 20;
        int cardH = 150;
        int gapY  = 20;
        int y = 0;

        for (int i = 0; i < 2; i++) {
            JPanel rc = reviewCard(cardW, cardH);
            rc.setBounds(0, y, cardW, cardH);
            reviewList.add(rc);
            y += cardH + gapY;
        }

        reviewList.setPreferredSize(new Dimension(viewW - 20, y));

        JScrollPane reviewScroll = new JScrollPane(
                reviewList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        reviewScroll.setBounds(viewX, viewY, viewW, viewH);
        reviewScroll.setBorder(null);
        cp.add(reviewScroll);

        // 하단 탭 ---------------------------------------------------
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

    // 내용은 전부 공백으로 둔 리뷰 카드
    private JPanel reviewCard(int w, int h) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230,230,230)));

        JLabel starLabel = new JLabel("");
        starLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        starLabel.setBounds(18, 12, 200, 20);
        card.add(starLabel);

        JLabel reportLabel = new JLabel("신고");
        reportLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        reportLabel.setForeground(new Color(140,140,140));
        reportLabel.setBounds(w - 60, 14, 40, 16);
        card.add(reportLabel);

        JLabel titleLabel = new JLabel("");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setBounds(18, 40, w - 36, 20);
        card.add(titleLabel);

        JLabel contentLabel = new JLabel("");
        contentLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        contentLabel.setBounds(18, 62, w - 36, 20);
        card.add(contentLabel);

        JPanel profile = new JPanel();
        profile.setBackground(new Color(220,220,220));
        profile.setBounds(18, 90, 32, 32);
        card.add(profile);

        JLabel writerLabel = new JLabel("");
        writerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        writerLabel.setBounds(60, 92, 120, 16);
        card.add(writerLabel);

        JLabel dateLabel = new JLabel("");
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(140,140,140));
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

    // 하단 탭: MainGUI CardLayout과 연동
    class TabButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainGUI == null) return;  // 단독 실행일 때는 아무것도 하지 않음

            String name = e.getActionCommand();
            switch (name) {
                case "학식":
                    mainGUI.showScreen("CAFETERIA");  // 학식 메인
                    dispose();
                    break;
                case "마이페이지":
                    mainGUI.showScreen("MYPAGE");     // 마이페이지 메인
                    dispose();
                    break;
            }
        }
    }

    // 상단 ← 버튼: CAFE1 화면으로 돌아가게 가정
    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (mainGUI != null) {
                mainGUI.showScreen("CAFE1");  // 카페1 메뉴 화면으로 복귀
            }
            dispose();
        }
    }

    // 단독 테스트용
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuDetailUI::new);
    }
}
