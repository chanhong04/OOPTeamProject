package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPage extends JPanel {
	
	private MainGUI mainGUI;
	
    public MyPage(MainGUI mainGUI) {
    	this.mainGUI = mainGUI;
    	mainGUI.mainFrame.setTitle("My Page");
    	    	
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        
        // 상단 타이틀 + 구분선
        JLabel title = new JLabel("My Page", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.PLAIN, 22));
        title.setBounds(0, 16, 1600, 30);
        this.add(title);

        JSeparator line = new JSeparator();
        line.setBounds(40, 50, 1520, 1);
        this.add(line);

        // 프로필 박스
        JPanel profile = new JPanel(null);
        profile.setBackground(new Color(230, 230, 230));
        profile.setBounds(80, 72, 1440, 92);
        this.add(profile);

        JLabel name = new JLabel("홍길동", SwingConstants.CENTER);
        name.setFont(new Font("Dialog", Font.BOLD, 24));
        name.setBounds(0, 10, 1440, 30);
        profile.add(name);

        JLabel uid = new JLabel("ID : asdf1020!", SwingConstants.CENTER);
        uid.setFont(new Font("Dialog", Font.PLAIN, 14));
        uid.setBounds(0, 48, 1440, 20);
        profile.add(uid);

        // 중앙 버튼(아이콘 없이 텍스트만, 동작 없음)
        JButton LogoutBtn = makeFlatButton("로그아웃");
        LogoutBtn.setBounds(240, 210, 180, 180);
        this.add(LogoutBtn);

        JButton ReviewBtn = makeFlatButton("내가 쓴 리뷰");
        ReviewBtn.setBounds(710, 210, 180, 180);
        this.add(ReviewBtn);

        JButton ChargeBtn = makeFlatButton("캐시 충전");
        ChargeBtn.setBounds(1180, 210, 180, 180);
        this.add(ChargeBtn);

        JButton MyOrderBtn = makeFlatButton("주문 내역");
        MyOrderBtn.setBounds(240, 420, 180, 180);
        this.add(MyOrderBtn);

        // 남은 캐시(둥근 버튼 스타일) — 표시용
        RoundedButton cash = new RoundedButton("남은 경기 캐시 :  N 원");
        cash.setBounds(250, 640, 1100, 44);
        cash.setBackground(new Color(58, 118, 248));
        cash.setForeground(Color.WHITE);
        cash.setFont(new Font("Dialog", Font.BOLD, 16));
        this.add(cash);

        // 하단 바: 버튼 두 개(학식 / 마이페이지)
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(80, 740, 1440, 90);
        bottomBar.setBackground(new Color(245, 238, 252));

        JButton CafeBtn = new JButton("학식");
        JButton MyPageBtn = new JButton("마이페이지");
        styleTab(CafeBtn);
        styleTab(MyPageBtn);
        MyPageBtn.setForeground(new Color(146, 107, 191)); // 현재 선택된 탭 느낌

        bottomBar.add(CafeBtn);
        bottomBar.add(MyPageBtn);
        this.add(bottomBar);
        
        MyPageButtonListener listener = new MyPageButtonListener();
        CafeBtn.addActionListener(listener);
        LogoutBtn.addActionListener(listener);

        setVisible(true);
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

    class MyPageButtonListener implements ActionListener{ //버튼 누를 시 액션

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String buttonName = e.getActionCommand();
			switch(buttonName) {
			case "학식":
				//new CafeteriaUI();
				//dispose();
				break;
			/*case "내가 쓴 리뷰":
				new MyReviewUI();
				dispose();
				break;
			case "캐시 충전":
				new ChargeUI();
				dispose();
				break;
			case "주문 내역":
				new MyOrder();
				dispose();
				break;
			*/case "로그아웃":
				new MainGUI();
				//dispose();
				break;
			}
}
    }
}
