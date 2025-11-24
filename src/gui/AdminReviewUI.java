package gui;

import javax.swing.*;
import java.awt.*;

public class AdminReviewUI extends JPanel {

    private MainGUI mainGUI;

    public AdminReviewUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));

        // 상단 뒤로가기
        JButton backBtn = createBackButton();
        add(backBtn);

        backBtn.addActionListener(e ->
                mainGUI.showScreen("ADMINTAB")
        );

        JLabel title = new JLabel("리뷰관리", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 16, 1600, 40);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(300, 70, 1000, 1);
        add(sep);

        JLabel listTitle = new JLabel("리뷰 목록", SwingConstants.CENTER);
        listTitle.setFont(new Font("Dialog", Font.BOLD, 22));
        listTitle.setBounds(0, 120, 1600, 40);
        add(listTitle);

        // 스크롤
        JPanel scrollPanel = new JPanel(null);
        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBounds(300, 170, 1000, 680);
        scroll.setBorder(null);
        add(scroll);

        int y = 0;
        for (int i = 0; i < 2; i++) {
            JPanel card = makeReviewCard();
            card.setBounds(0, y, 1000, 180);
            scrollPanel.add(card);
            y += 190;
        }

        scrollPanel.setPreferredSize(new Dimension(1000, y + 10));
    }

    private JPanel makeReviewCard() {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel stars = new JLabel("⭐ ⭐ ⭐ ⭐ ☆");
        stars.setBounds(20, 15, 200, 30);
        card.add(stars);

        JLabel delete = new JLabel("삭제");
        delete.setBounds(900, 15, 80, 30);
        card.add(delete);

        JLabel title = new JLabel("밥");
        title.setFont(new Font("Dialog", Font.BOLD, 20));
        title.setBounds(20, 55, 300, 30);
        card.add(title);

        JLabel content = new JLabel("밥이 맛있었어요");
        content.setBounds(20, 85, 500, 30);
        card.add(content);

        JLabel writer = new JLabel("홍길동  2025.11.05");
        writer.setBounds(20, 120, 300, 30);
        card.add(writer);

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
