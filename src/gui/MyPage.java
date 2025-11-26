package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;   // users.txt 읽기용

public class MyPage extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용

    // 프로필 라벨
    private JLabel nameLabel;
    private JLabel uidLabel;

    public MyPage(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 상단 타이틀 + 구분선
        JLabel title = new JLabel("My Page", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.PLAIN, 22));
        title.setBounds(0, 16, 1600, 30);
        add(title);

        JSeparator line = new JSeparator();
        line.setBounds(40, 50, 1520, 1);
        add(line);

        // 프로필 박스
        JPanel profile = new JPanel(null);
        profile.setBackground(new Color(230, 230, 230));
        profile.setBounds(80, 72, 1440, 92);
        add(profile);

        // 기본값은 임시로 표시, 나중에 파일에서 읽어서 교체
        nameLabel = new JLabel("이름", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        nameLabel.setBounds(0, 10, 1440, 30);
        profile.add(nameLabel);

        uidLabel = new JLabel("ID : ", SwingConstants.CENTER);
        uidLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        uidLabel.setBounds(0, 48, 1440, 20);
        profile.add(uidLabel);

        // users.txt에서 프로필 세팅
        loadProfileFromUsersFile();

        // 중앙 버튼(아이콘 없이 텍스트만, 동작 없음)
        JButton LogoutBtn = makeFlatButton("로그아웃", "로그아웃", "Logout.png");
        LogoutBtn.setBounds(240, 210, 180, 180);
        add(LogoutBtn);

        JButton ReviewBtn = makeFlatButton("내가 쓴 리뷰", "내가 쓴 리뷰", "MyReview.png");
        ReviewBtn.setBounds(710, 210, 180, 180);
        add(ReviewBtn);

        // 하단 바: 버튼 두 개(학식 / 마이페이지)
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(80, 740, 1440, 90);
        bottomBar.setBackground(new Color(245, 238, 252));

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

        // 리스너 연결
        MyPageButtonListener listener = new MyPageButtonListener();
        tabCafe.addActionListener(listener);
        tabMy.addActionListener(listener);
        RDBtn.addActionListener(listener);
        LogoutBtn.addActionListener(listener);
        ReviewBtn.addActionListener(listener);
        // ChargeBtn, MyPageBtn은 아직 동작 없음 (필요하면 listener 추가)
    }

    private JButton makeFlatButton(String buttonName, String text, String imageFile) {
        JButton b = new JButton(text);
        b.setFont(new Font("Dialog", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        
        ImageIcon icon = loadScaledIcon(imageFile, 180, 180);
        if (icon != null) {
            b.setIcon(icon);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setVerticalAlignment(SwingConstants.CENTER);
        }
        b.setActionCommand(buttonName);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        return b;
    }
    
    //로그인 정보 읽어오기
    public void refreshProfile() {
        loadProfileFromUsersFile();
    } 

    // users.txt에서 한 줄 읽어서 이름 / ID 세팅
    private void loadProfileFromUsersFile() {
    	//로그인 id 정보
    	String loginId = mainGUI.getLoginID();
        if (loginId == null || loginId.isEmpty()) {
            // 아직 로그인 안 했으면 기본값 유지
            return;
        }
    	
        File file = new File("users.txt");  // 실행 위치 기준

        if (!file.exists()) {
            // 파일 없으면 그냥 기본 텍스트 유지
            return;
        }
        
        nameLabel.setText("이름");
        uidLabel.setText("ID : ");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // users.txt 한 줄: id pw name role level
                String[] tokens = line.split("\t");
                if (tokens.length < 3) continue;

                String id   = tokens[0]; // user~
                String name = tokens[2]; // 이름
                
                if (!id.equals(loginId)) continue;
                
                // 윗줄 = 이름, 아랫줄 = ID : user~
                nameLabel.setText(name);
                uidLabel.setText("ID : " + id);

                // 첫 번째 유효한 사용자만 사용
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private ImageIcon loadIcon(String fileName){
        java.net.URL imageURL = CafeteriaUI.class.getResource("/images/" + fileName);
        
        if (imageURL == null) {
            System.out.println("이미지 파일을 찾을 수 없습니다: " + fileName);
            return null;
        }
        return new ImageIcon(imageURL);
    }
    
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

    private JButton makeFlatButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Dialog", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        return b;
    }

    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
    }

    // 둥근 버튼 페인팅(텍스트 표시용)
    static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
            setFocusPainted(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public boolean isOpaque() { return false; }
    }

    // 버튼 액션: MainGUI의 CardLayout과 연동
    class MyPageButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            switch(buttonName) {
                case "내가 쓴 리뷰":
                    mainGUI.showScreen("MYREVIEW");
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
                case "로그아웃":
                    mainGUI.showScreen("LOGIN");
                    break;
            }
        }
    }
}
