package gui;

import javax.swing.*;
import java.awt.*;

public class AdminBanUI extends JPanel {

    private MainGUI mainGUI;

    public AdminBanUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));

        // 상단바
        JButton backBtn = createBackButton();
        add(backBtn);

        backBtn.addActionListener(e -> mainGUI.showScreen("ADMINTAB"));

        JLabel title = new JLabel("차단한 멤버 관리", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 20, 1600, 40);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(300, 70, 1000, 1);
        add(sep);

        JLabel listLabel = new JLabel("차단한 멤버 목록", SwingConstants.CENTER);
        listLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        listLabel.setBounds(0, 120, 1600, 40);
        add(listLabel);

        // 스크롤 영역
        JPanel scrollPanel = new JPanel(null);
        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBounds(300, 170, 1000, 680);
        scroll.setBorder(null);
        add(scroll);

        int y = 0;
        for (int i = 0; i < 3; i++) {
            JPanel card = makeBanCard();
            card.setBounds(0, y, 1000, 140);
            scrollPanel.add(card);
            y += 150;
        }

        scrollPanel.setPreferredSize(new Dimension(1000, y + 10));
    }

    private JPanel makeBanCard() {
        JPanel card = new JPanel(null);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setBackground(Color.WHITE);

        JLabel id = new JLabel("id : ???");
        id.setBounds(30, 20, 300, 30);
        card.add(id);

        JLabel pw = new JLabel("비밀번호 : xxxxxx");
        pw.setBounds(30, 55, 300, 30);
        card.add(pw);

        JLabel type = new JLabel("멤버 구분 : 일반");
        type.setBounds(750, 60, 200, 30);
        card.add(type);

        JLabel unban = new JLabel("차단 해제");
        unban.setBounds(880, 20, 100, 30);
        card.add(unban);

        return card;
    }

    private JButton createBackButton() {
        JButton btn = new JButton("<");
        btn.setFont(new Font("Dialog", Font.BOLD, 28));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBounds(50, 18, 50, 40);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
