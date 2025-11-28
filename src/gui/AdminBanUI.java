package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.User;
import model.UserManager;
import mgr.Manageable;

public class AdminBanUI extends JPanel {

    private MainGUI mainGUI;
    private UserManager userMgr;
    private JPanel scrollPanel;
    private JLabel listLabel;

    public AdminBanUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));

        userMgr = UserManager.getInstance();

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

        listLabel = new JLabel("차단한 멤버 목록", SwingConstants.CENTER);
        listLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        listLabel.setBounds(0, 120, 1600, 40);
        add(listLabel);

        // 스크롤 영역
        scrollPanel = new JPanel(null);
        scrollPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBounds(300, 170, 1000, 680);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll);

        // 탭 전환 시 리스트 새로고침
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshBanList();
            }
        });
        refreshBanList();
    }

    // 차단 목록 새로고침
    public void refreshBanList() {
        scrollPanel.removeAll();

        int y = 0;
        int count = 0;
        int cardHeight = 100;
        int gap = 10;

        for (Manageable m : userMgr.mList) {
            User u = (User) m;
            if (u.isBanned()) {
                JPanel card = makeBanCard(u);
                card.setBounds(0, y, 1000, cardHeight);
                scrollPanel.add(card);
                y += (cardHeight + gap);
                count++;
            }
        }

        listLabel.setText("차단한 멤버 목록 (" + count + "명)");
        scrollPanel.setPreferredSize(new Dimension(1000, y + 50));

        scrollPanel.revalidate();
        scrollPanel.repaint();
    }

    private JPanel makeBanCard(User user) {
        JPanel card = new JPanel(null);
        card.setBorder(BorderFactory.createLineBorder(Color.RED));
        card.setBackground(Color.WHITE);

        JLabel id = new JLabel("ID : " + user.getId());
        id.setFont(new Font("Dialog", Font.BOLD, 18));
        id.setBounds(30, 20, 300, 30);
        card.add(id);

        JLabel name = new JLabel("이름 : " + user.getName());
        name.setFont(new Font("Dialog", Font.PLAIN, 16));
        name.setBounds(30, 55, 300, 30);
        card.add(name);

        JLabel type = new JLabel("멤버 구분 : 차단됨");
        type.setForeground(Color.RED);
        type.setBounds(750, 60, 200, 30);
        card.add(type);

        //차단 해제
        JButton unbanBtn = new JButton("차단 해제");
        unbanBtn.setBounds(880, 20, 100, 30);
        unbanBtn.addActionListener(e -> {
            int ans = JOptionPane.showConfirmDialog(this,
                    user.getName() + " 님의 차단을 해제하시겠습니까?", "해제", JOptionPane.YES_NO_OPTION);

            if (ans == JOptionPane.YES_OPTION) {
                user.unban();
                userMgr.saveUsers("users.txt");

                refreshBanList();
            }
        });
        card.add(unbanBtn);

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