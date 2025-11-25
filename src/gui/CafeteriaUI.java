package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CafeteriaUI extends JPanel {

    private MainGUI mainGUI;

    public CafeteriaUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 상단 타이틀 + 구분선 ---------------------------
        JLabel title = new JLabel("경슐랭", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 28));
        title.setBounds(0, 24, 1600, 40);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(320, 90, 960, 1);
        add(sep);

        // 식당 버튼 + 아래 라벨 ---------------------------
        JButton cafe1 = makeCafeButton("CAFE1", "만권화밥",   420, 150, "만권화밥.png");
        JButton cafe2 = makeCafeButton("CAFE2", "버거 앤 타코", 840, 150, "버거앤타코.png");
        JButton cafe3 = makeCafeButton("CAFE3", "신머이쌀국수", 240, 450, "신머이쌀국수.png");
        JButton cafe4 = makeCafeButton("CAFE4", "쑝쑝돈까스",   640, 450, "쑝쑝돈까스.png");
        JButton cafe5 = makeCafeButton("CAFE5", "위델가",       1040, 450, "위델가.png");

        // info 버튼 (만권화밥 / 버거 앤 타코 카드 우측 하단) ----
        JButton info1 = makeInfoButton("I1", 420 + 260, 150 + 180 - 30, "Information.png");
        JButton info2 = makeInfoButton("I2",  840 + 260, 150 + 180 - 30, "Information.png");
        JButton info3 = makeInfoButton("I3", 240 + 260, 450 + 180 - 30, "Information.png");
        JButton info4 = makeInfoButton("I4", 640 + 260, 450 + 180 - 30, "Information.png");
        JButton info5 = makeInfoButton("I5", 1040 + 260, 450 + 180 - 30, "Information.png");
        add(cafe1);
        add(cafe2);
        add(cafe3);
        add(cafe4);
        add(cafe5);
        add(info1);
        add(info2);
        add(info3);
        add(info4);
        add(info5);

        // 하단 바: 학식 / 마이페이지 ----------------------
        JPanel bottomBar = new JPanel(new GridLayout(1, 3));
        bottomBar.setBounds(320, 740, 960, 80);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton tabCafe = new JButton("학식");
        JButton RDBtn = new JButton("추천돌림판");
        JButton tabMy   = new JButton("마이페이지");
        styleTab(tabCafe);
        styleTab(RDBtn);
        styleTab(tabMy);
 
        tabCafe.setForeground(new Color(146, 107, 191)); // 현재 선택된 탭 느낌

        bottomBar.add(tabCafe);
        bottomBar.add(RDBtn);
        bottomBar.add(tabMy);
        add(bottomBar);

        // 리스너 연결 ------------------------------------
        CafeButtonListener listener = new CafeButtonListener();
        cafe1.addActionListener(listener);
        cafe2.addActionListener(listener);
        cafe3.addActionListener(listener);
        cafe4.addActionListener(listener);
        cafe5.addActionListener(listener);
        tabCafe.addActionListener(listener);
        tabMy.addActionListener(listener);
        RDBtn.addActionListener(listener);
        info1.addActionListener(listener);
        info2.addActionListener(listener);
        info3.addActionListener(listener);
        info4.addActionListener(listener);
        info5.addActionListener(listener);
              
    }

    // 이미지 로드
    private ImageIcon loadIcon(String fileName){
        java.net.URL imageURL = CafeteriaUI.class.getResource("/images/" + fileName);
        
        if (imageURL == null) {
            System.out.println("이미지 파일을 찾을 수 없습니다: " + fileName);
            return null;
        }
        return new ImageIcon(imageURL);
    }

    // 원하는 크기로 스케일링
    private ImageIcon loadScaledIcon(String fileName, int targetW, int targetH) {
        ImageIcon original = loadIcon(fileName);
        if (original == null) return null;

        int iw = original.getIconWidth();
        int ih = original.getIconHeight();

        double scale = Math.min((double) targetW / iw, (double) targetH / ih);
        int newW = (int) (iw * scale);
        int newH = (int) (ih * scale);

        Image scaled = original.getImage()
                .getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // 큰 카드형 버튼 + 아래 텍스트 라벨
    private JButton makeCafeButton(String cmd, String labelText,
                                   int x, int y, String imageFile) {
        JButton b = new JButton();
        b.setBounds(x, y, 260, 180);
        b.setFocusPainted(false);
        b.setBorderPainted(false);

        ImageIcon icon = loadScaledIcon(imageFile, 260, 180);
        if (icon != null) {
            b.setIcon(icon);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setVerticalAlignment(SwingConstants.CENTER);
        }
        b.setActionCommand(cmd);   // 리스너에서 구분용

        // ↓ 버튼 밖, 하단에 가게 이름 라벨 추가
        JLabel name = new JLabel(labelText, SwingConstants.CENTER);
        name.setFont(new Font("Dialog", Font.BOLD, 18));
        name.setBounds(x, y + 180 + 4, 260, 30);
        add(name);

        return b;
    }

    // 우측 하단 info 버튼 (작게 + 아이콘 스케일링)
    private JButton makeInfoButton(String cmd, int x, int y, String imageFile) {
        JButton I = new JButton();
        I.setBounds(x, y, 30, 20);
        I.setFocusPainted(false);
        I.setContentAreaFilled(false);  // 필요시 배경 제거
        I.setBorderPainted(false);

        I.setActionCommand(cmd);

        ImageIcon icon = loadScaledIcon(imageFile, 30, 20);
        if (icon != null) {
            I.setIcon(icon);
        } else {
            I.setText("i");  // 이미지 못 찾을 때 대체 텍스트
        }
        return I;
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
                case "CAFE1":
                    mainGUI.showScreen("CAFE1");
                    break;
                case "CAFE2":
                    mainGUI.showScreen("CAFE2");
                    break;
                case "CAFE3":
                    mainGUI.showScreen("CAFE3");
                    break;
                case "CAFE4":
                    mainGUI.showScreen("CAFE4");
                    break;
                case "CAFE5":
                    mainGUI.showScreen("CAFE5");
                    break;
                case "마이페이지":
                    mainGUI.showScreen("MYPAGE");
                    break;
                case "학식":
                    mainGUI.showScreen("CAFETERIA");
                    break;
                case "추천돌림판":
                	mainGUI.showScreen("DOLLIMPAN");
                	break;
                case "I1":
                    new CafeInfoUI(mainGUI, 1);   // info 버튼
                    break;
                case "I2":
                    new CafeInfoUI(mainGUI, 2);
                    break;
                case "I3":
                	new CafeInfoUI(mainGUI, 3);
                	break;
                case "I4":
                	new CafeInfoUI(mainGUI, 4);
                	break;
                case "I5":
                	new CafeInfoUI(mainGUI, 5);
                	break;
            }
        }
    }
}
