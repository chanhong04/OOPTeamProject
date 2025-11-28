package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

import model.User;
import model.UserManager;
import mgr.Factory;
import mgr.Manageable;

public class AdminMemberUI extends JPanel {

    private MainGUI mainGUI;
    private UserManager userMgr;
    private JPanel scrollPanel;
    private JLabel listLabel;

    public AdminMemberUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));

        userMgr = UserManager.getInstance();

        if (userMgr.mList.isEmpty()) {
            userMgr.readAll("users.txt", new Factory<User>() {
                @Override
                public User create() {
                    return new User();
                }
            });
        }

        // 상단바
        JButton backBtn = createBackButton();
        add(backBtn);
        backBtn.addActionListener(e -> mainGUI.showScreen("ADMINTAB"));

        JLabel title = new JLabel("멤버 관리", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 16, 1600, 40);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(320, 70, 960, 1);
        add(sep);

        listLabel = new JLabel("전체 멤버 (로딩 중...)", SwingConstants.CENTER);
        listLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        listLabel.setBounds(0, 110, 1600, 40);
        add(listLabel);

        // 스크롤 패널
        scrollPanel = new JPanel(null); // 절대 좌표
        scrollPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBounds(280, 160, 1050, 700);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll);

        // 탭 사용 시 목록 새로고침
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshList();
            }
        });
        refreshList();
    }

    public void refreshList() {
        scrollPanel.removeAll(); // 기존 카드 전부 제거

        int y = 0;
        int cardHeight = 80;
        int gap = 10;
        int count = 0;

        // 리스트를 처음부터 다시 순회하며 좌표 재설정
        for (Manageable m : userMgr.mList) {
            User u = (User) m;
            if (!u.isBanned() && !u.getRole().equals("admin")) {
                JPanel card = makeSimpleMemberCard(u);
                card.setBounds(0, y, 1050, cardHeight);
                scrollPanel.add(card);
                y += (cardHeight + gap);
                count++;
            }
        }

        // 라벨 및 스크롤 높이 갱신
        listLabel.setText("전체 멤버 (" + count + "명)");
        scrollPanel.setPreferredSize(new Dimension(1050, y + 50));

        // 화면 다시 그리기
        scrollPanel.revalidate();
        scrollPanel.repaint();
    }

    private JPanel makeSimpleMemberCard(User user) {
        JPanel card = new JPanel(null);
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        String userId = user.getId();
        String userNick = user.getName();

        String infoText = String.format("ID :  %s    |    이름 :  %s", userId, userNick);
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        infoLabel.setBounds(40, 0, 700, 80);
        card.add(infoLabel);

        JButton deleteBtn = new JButton("삭제");
        deleteBtn.setFont(new Font("Dialog", Font.BOLD, 14));
        deleteBtn.setBackground(new Color(255, 100, 100));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBounds(920, 20, 80, 40);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        deleteBtn.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "정말 [" + userNick + "] 님을 삭제하시겠습니까?",
                    "멤버 삭제",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                // 상태 변경 (차단)
                user.ban();
                // 파일에 즉시 저장 (users.txt 업데이트)
                userMgr.saveUsers("users.txt");
                // 전체 목록 새로고침 (화면 갱신)
                refreshList();
            }
        });

        card.add(deleteBtn);
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