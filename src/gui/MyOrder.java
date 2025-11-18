package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class MyOrder extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용

    public MyOrder(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // ── 상단 바 ───────────────────────────────────────────────
        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(20, 15, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(back);

        back.addMouseListener(new BackButtonListener());

        JLabel titleLabel = new JLabel("주문 내역", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        titleLabel.setBounds(0, 15, 900, 30);
        add(titleLabel);

        JSeparator sepTop = new JSeparator();
        sepTop.setBounds(20, 60, 860, 1);
        add(sepTop);

        // ── 주문 카드 목록 + 스크롤 ───────────────────────────────
        int viewX = 40, viewY = 80, viewW = 820, viewH = 380;

        JPanel orderList = new JPanel(null);
        orderList.setBackground(Color.WHITE);

        int cardW = viewW;
        int cardH = 130;
        int gapY  = 20;
        int y = 0;

        // 디자인용 카드 여러 개 (스크롤 확인용)
        for (int i = 0; i < 8; i++) {
            JPanel card = createOrderCard(cardW, cardH);
            card.setBounds(0, y, cardW, cardH);
            orderList.add(card);
            y += cardH + gapY;
        }

        orderList.setPreferredSize(new Dimension(viewW, y));

        JScrollPane scroll = new JScrollPane(
                orderList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBounds(viewX, viewY, viewW, viewH);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll);

        // ── 하단 탭 바 (학식 / 마이페이지) ────────────────────────
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(0, 590, 900, 60);
        bottomBar.setBackground(new Color(248, 242, 255));
        add(bottomBar);

        JButton cafeBtn = new JButton("학식");
        JButton myBtn   = new JButton("마이페이지");
        styleTab(cafeBtn);
        styleTab(myBtn);

        CafeMenuButtonListener tabListener = new CafeMenuButtonListener();
        cafeBtn.addActionListener(tabListener);
        myBtn.addActionListener(tabListener);

        bottomBar.add(cafeBtn);
        bottomBar.add(myBtn);
    }

    // 주문 카드 UI (디자인용, 나중에 텍스트는 파일로 채워도 됨)
    private JPanel createOrderCard(int w, int h) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(Color.BLACK));

        // 메뉴 이미지 자리
        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(new Color(238, 238, 238));
        imgPanel.setBounds(16, 16, 80, 80);
        card.add(imgPanel);

        // 메뉴 이름
        JLabel nameLabel = new JLabel("메뉴 이름");
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        nameLabel.setBounds(110, 22, 200, 22);
        card.add(nameLabel);

        // 금액
        JLabel priceLabel = new JLabel("금액 : N 원");
        priceLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        priceLabel.setBounds(110, 48, 200, 18);
        card.add(priceLabel);

        // 주문 날짜
        JLabel dateLabel = new JLabel("주문 날짜 : 2025.11.2");
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        dateLabel.setBounds(110, 68, 250, 18);
        card.add(dateLabel);

        // 우측 상단 '리뷰작성'
        JLabel writeLabel = new JLabel("리뷰작성");
        writeLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        writeLabel.setBounds(w - 90, 20, 70, 20);
        writeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(writeLabel);

        return card;
    }

    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // 하단 탭 리스너: MainGUI의 CardLayout과 연동
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

    // 상단 ← 버튼 리스너 (마이페이지로 돌아가기)
    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            mainGUI.showScreen("MYPAGE");
        }
    }
}
