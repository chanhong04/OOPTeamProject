package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CafeteriaUI extends JPanel {
	
	private MainGUI mainGUI;
	
    public CafeteriaUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        
    	mainGUI.mainFrame.setTitle("경슐랭");

        this.setLayout(null);
        this.setBackground(Color.WHITE);

        // 상단 타이틀 + 구분선
        JLabel title = new JLabel("경슐랭", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 28));
        title.setBounds(0, 24, 1600, 40);
        this.add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(320, 90, 960, 1);
        this.add(sep);

        // 우측 상단 검색 아이콘(이미지 미사용, 벡터로 그림)
        JButton searchBtn = new JButton(new MagnifierIcon(34, new Color(30, 30, 30)));
        searchBtn.setBounds(1180, 28, 44, 44);
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setOpaque(false);
        searchBtn.setActionCommand("검색");
        this.add(searchBtn);

        // 식당 버튼 4개 (원형 모양 신경 X, 버튼으로만 구성)
        JButton cafe1 = makeBigButton("식당 1", 420, 150);
        JButton cafe2 = makeBigButton("식당 2", 840, 150);
        JButton cafe3 = makeBigButton("식당 3", 420, 450);
        JButton cafe4 = makeBigButton("식당 4", 840, 450);
        this.add(cafe1); this.add(cafe2); this.add(cafe3); this.add(cafe4);

        // 하단 바: 학식 / 마이페이지 (버튼)
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(320, 740, 960, 80);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton tabCafe = new JButton("학식");
        JButton tabMy   = new JButton("마이페이지");
        styleTab(tabCafe); styleTab(tabMy);
        tabCafe.setForeground(new Color(146, 107, 191)); // 현재 선택된 탭 느낌
        bottomBar.add(tabCafe);
        bottomBar.add(tabMy);
        this.add(bottomBar);

        // 리스너 연결
        CafeButtonListener listener = new CafeButtonListener();
        cafe1.addActionListener(listener);
        cafe2.addActionListener(listener);
        cafe3.addActionListener(listener);
        cafe4.addActionListener(listener);
        searchBtn.addActionListener(listener);
        tabCafe.addActionListener(listener);
        tabMy.addActionListener(listener);

        setVisible(true);
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

    // 간단한 돋보기 아이콘(이미지 파일 없이 투명 배경)	
    static class MagnifierIcon implements Icon {
        private final int size;
        private final Color color;
        MagnifierIcon(int size, Color color) { this.size = size; this.color = color; }
        @Override public int getIconWidth()  { return size; }
        @Override public int getIconHeight() { return size; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(Math.max(2f, size/10f)));
            int r = (int)(size * 0.55);
            int cx = x + size/2 - r/2;
            int cy = y + size/2 - r/2 - 3;
            g2.drawOval(cx, cy, r, r);
            int hx = cx + r - 2, hy = cy + r - 2;
            g2.drawLine(hx, hy, hx + size/4, hy + size/4);
            g2.dispose();
        }
    }

    // 동작 연결(원하는 화면으로 교체)
    class CafeButtonListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "마이페이지":           //new MyPage();      dispose(); break;
                case "학식":                 /* 현재 화면 */                 break;
                //case "검색":                 new ResearchUI();  dispose(); break;
            }
        }
    }
}
