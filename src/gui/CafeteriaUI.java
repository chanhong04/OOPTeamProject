package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CafeteriaUI extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용

    public CafeteriaUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 상단 타이틀 + 구분선
        JLabel title = new JLabel("경슐랭", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 28));
        title.setBounds(0, 24, 1600, 40);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(320, 90, 960, 1);
        add(sep);

        // 식당 버튼 4개
        JButton cafe1 = makeBigButton("식당 1", 420, 150);
        JButton cafe2 = makeBigButton("식당 2", 840, 150);
        JButton cafe3 = makeBigButton("식당 3", 240, 450);
        JButton cafe4 = makeBigButton("식당 4", 640, 450);
        JButton cafe5 = makeBigButton("식당 5", 1040, 450);
        add(cafe1);
        add(cafe2);
        add(cafe3);
        add(cafe4);
        add(cafe5);

        // 하단 바: 학식 / 마이페이지
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(320, 740, 960, 80);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton tabCafe = new JButton("학식");
        JButton tabMy   = new JButton("마이페이지");
        styleTab(tabCafe);
        styleTab(tabMy);
        tabCafe.setForeground(new Color(146, 107, 191)); // 현재 선택된 탭 느낌

        bottomBar.add(tabCafe);
        bottomBar.add(tabMy);
        add(bottomBar);

        // 리스너 연결
        CafeButtonListener listener = new CafeButtonListener();
        cafe1.addActionListener(listener);
        cafe2.addActionListener(listener);
        cafe3.addActionListener(listener);
        cafe4.addActionListener(listener);
        cafe5.addActionListener(listener);
        tabCafe.addActionListener(listener);
        tabMy.addActionListener(listener);
    }

    private JButton makeBigButton(String text, int x, int y) {
        JButton b = new JButton(text);
        b.setBounds(x, y, 300, 260);
        b.setFont(new Font("Dialog", Font.BOLD, 26));
        b.setFocusPainted(false);
        return b;
    }

    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // 동작 연결: MainGUI의 CardLayout과 연동
    class CafeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "식당 1":
                    // MainGUI에서 "CAFE1" 이름으로 패널 등록해두었다는 가정
                    mainGUI.showScreen("CAFE1");
                    break;
                case "식당 2":
                    mainGUI.showScreen("CAFE2");
                    break;
                case "식당 3":
                    mainGUI.showScreen("CAFE3");
                    break;
                case "식당 4":
                    mainGUI.showScreen("CAFE4");
                    break;
                case "식당 5":
                	mainGUI.showScreen("CAFE5");
                	break;
                case "마이페이지":
                    // MyPage 패널을 "MYPAGE" 이름으로 CardLayout에 추가했다는 가정
                    mainGUI.showScreen("MYPAGE");
                    break;
                case "학식":
                    // 현재 화면이지만, 혹시 다른 화면에서 돌아올 때도 재사용 가능
                    mainGUI.showScreen("CAFETERIA");
                    break;
            }
        }
    }
}
