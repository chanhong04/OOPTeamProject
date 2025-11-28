package gui;

import facade.Auth;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminTabUI extends JPanel {

    private MainGUI mainGUI;

    public AdminTabUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));

        // 상단 타이틀 + 구분선
        JLabel title = new JLabel("관리자 메뉴", SwingConstants.CENTER);
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

        JLabel name = new JLabel("관리자", SwingConstants.CENTER);
        name.setFont(new Font("Dialog", Font.BOLD, 24));
        name.setBounds(0, 10, 1440, 30);
        profile.add(name);

        JLabel uid = new JLabel("ID : admin", SwingConstants.CENTER);
        uid.setFont(new Font("Dialog", Font.PLAIN, 14));
        uid.setBounds(0, 48, 1440, 20);
        profile.add(uid);

        //로그아웃 버튼
        JButton logoutBtn = new JButton("로그아웃");
        logoutBtn.setFont(new Font("Dialog", Font.PLAIN, 13));
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(Color.DARK_GRAY);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBounds(755, 810, 90, 32);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutBtn.addActionListener(e -> {
            {
                mainGUI.showScreen("LOGIN");
            }
        });
        add(logoutBtn); // 프로필 패널 안에 버튼 추가

        // 관리자 메뉴 항목 영역
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(50, 60, 1500, 820);
        add(mainPanel);

        // 메뉴들
        JPanel menu1 = makeMenuItem("멤버 관리",
                () -> mainGUI.showScreen("ADMINMEM"));
        menu1.setBounds(250, 200, 1000, 55);
        mainPanel.add(menu1);

        JPanel menu2 = makeMenuItem("차단된 멤버 관리",
                () -> mainGUI.showScreen("ADMINBAN"));
        menu2.setBounds(250, 400, 1000, 55);
        mainPanel.add(menu2);

        JPanel menu3 = makeMenuItem("전체 리뷰 관리",
                () -> mainGUI.showScreen("ADMINREVIEW"));
        menu3.setBounds(250, 600, 1000, 55);
        mainPanel.add(menu3);
        
    }

    // 메뉴 박스 + 클릭
    private JPanel makeMenuItem(String text, Runnable onClick) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.LIGHT_GRAY);

        JLabel label = new JLabel("  > " + text);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 25));
        item.add(label, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });

        return item;
    }
}