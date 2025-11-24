package gui;

import javax.swing.*;
import java.awt.*;

public class AdminMemberUI extends JPanel {

    private MainGUI mainGUI;

    public AdminMemberUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1600, 900));

        // 상단바
        JButton backBtn = createBackButton();
        add(backBtn);

        backBtn.addActionListener(e ->
            mainGUI.showScreen("ADMINTAB")
        );

        JLabel title = new JLabel("맴버관리", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 16, 1600, 40);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(320, 70, 960, 1);
        add(sep);

        JLabel listLabel = new JLabel("맴버목록", SwingConstants.CENTER);
        listLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        listLabel.setBounds(0, 110, 1600, 40);
        add(listLabel);


        // 스크롤
        JPanel scrollPanel = new JPanel(null);
        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBounds(280, 160, 1050, 700);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll);

        int y = 0;
        for (int i = 0; i < 3; i++) {  // 카드 적게 넣어도 스크롤 가능
            JPanel card = makeMemberCard();
            card.setBounds(0, y, 1050, 150);
            scrollPanel.add(card);
            y += 160;
        }

        applyScroll(scroll, scrollPanel, y);
    }


    private JPanel makeMemberCard() {
        JPanel card = new JPanel(null);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setBackground(Color.WHITE);

        JLabel id = new JLabel("id : ???");
        id.setBounds(30, 20, 300, 30);
        card.add(id);

        JLabel pw = new JLabel("비밀번호 : xxxxxx");
        pw.setBounds(30, 60, 300, 30);
        card.add(pw);

        JLabel status = new JLabel("상태 : 활동 / 정지");
        status.setBounds(750, 20, 250, 30);
        card.add(status);

        JLabel type = new JLabel("멤버 구분 : 일반");
        type.setBounds(750, 60, 250, 30);
        card.add(type);

        JLabel join = new JLabel("가입일 :");
        join.setBounds(750, 100, 250, 30);
        card.add(join);

        return card;
    }

    private void applyScroll(JScrollPane scroll, JPanel scrollPanel, int y) {
        int minHeight = scroll.getHeight() + 200;
        int contentHeight = Math.max(y + 20, minHeight);
        scrollPanel.setPreferredSize(new Dimension(scroll.getWidth(), contentHeight));
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
