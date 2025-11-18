package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;          // ActionListener, MouseAdapter, MouseEvent
import javax.swing.border.LineBorder;

public class Cafe1MenuUI extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용

    public Cafe1MenuUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // ── 상단 바 ───────────────────────────────────────────────
        JLabel back = new JLabel("←", SwingConstants.CENTER); // 이전 버튼
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(110, 18, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(back);

        // ★ 이전 버튼 리스너 연결
        BackButtonListener backListener = new BackButtonListener();
        back.addMouseListener(backListener);

        JLabel title = new JLabel("식당1", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 16, 1600, 34);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(260, 66, 1080, 1);
        add(sep);

        // ── 스크롤 영역 ──────────────────────────────────────────
        int viewX = 260, viewY = 80, viewW = 1080, viewH = 630;
        JPanel list = new JPanel(null);              // 절대 배치
        list.setBackground(Color.WHITE);

        int cardW = 300, cardH = 210;
        int leftX = 40, rightX = 40 + 520;
        int gapY = 50;

        int y = 20;
        for (int i = 0; i < 10; i++) {
            JPanel cardL = menuCard(leftX, y, cardW, cardH);
            list.add(cardL);

            JPanel cardR = menuCard(rightX, y, cardW, cardH);
            list.add(cardR);

            y += cardH + gapY;
        }

        list.setPreferredSize(new Dimension(viewW, y + 20));

        JScrollPane scroll = new JScrollPane(
                list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBounds(viewX, viewY, viewW, viewH);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll);

        // ── 하단 탭 바 ───────────────────────────────────────────
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(260, 730, 1080, 84);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton cafeBtn = new JButton("학식");
        JButton myBtn   = new JButton("마이페이지");
        styleTab(cafeBtn);
        styleTab(myBtn);

        CafeMenuButtonListener listener = new CafeMenuButtonListener();
        cafeBtn.addActionListener(listener);
        myBtn.addActionListener(listener);

        bottomBar.add(cafeBtn);
        bottomBar.add(myBtn);
        add(bottomBar);
    }

    // 단일 메뉴 카드
    private JPanel menuCard(int x, int y, int w, int h) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, w, h);
        card.setBorder(new LineBorder(new Color(80,80,80), 1));
        card.setBackground(Color.WHITE);

        JPanel img = new JPanel();
        img.setBackground(new Color(230, 230, 230));
        img.setBounds(18, 16, w - 36, 120);
        img.setBorder(new LineBorder(new Color(210,210,210)));
        card.add(img);

        JLabel name = new JLabel("메뉴 이름");
        name.setFont(new Font("Dialog", Font.PLAIN, 14));
        name.setBounds(22, 144, w - 44, 20);
        card.add(name);

        JLabel price = new JLabel("~원");
        price.setFont(new Font("Dialog", Font.BOLD, 14));
        price.setBounds(22, 166, w - 44, 20);
        card.add(price);

        // 카드 클릭 시 상세 페이지로 이동 (현재는 별도 프레임 사용 중)
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	new MenuDetailUI();
            }
        });

        return card;
    }

    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ── 하단 탭(학식 / 마이페이지) 동작 클래스 ───────────────────
    class CafeMenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            switch (buttonName) {
                case "학식":
                    // 메인 카드레이아웃에서 학식 메인 화면으로
                    mainGUI.showScreen("CAFETERIA");
                    break;
                case "마이페이지":
                    // MyPage도 JPanel + CardLayout에 "MYPAGE" 이름으로 등록해둔다는 가정
                    mainGUI.showScreen("MYPAGE");
                    break;
                default:
                    break;
            }
        }
    }

    // ── 상단 '←' 이전 버튼 동작 클래스 ──────────────────────────
    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // 카드레이아웃에서 이전 화면(학식 메인)으로 돌아가기
            mainGUI.showScreen("CAFETERIA");
        }
    }
}
