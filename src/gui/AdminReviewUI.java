package gui;

import model.Review;
import model.ReviewManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdminReviewUI extends JPanel {

    private MainGUI mainGUI;
    private JPanel scrollPanel; // 리뷰 카드들이 붙을 패널 (새로고침을 위해 멤버변수화)

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

        JLabel listTitle = new JLabel("신고 누적 리뷰 목록 (10회 이상)", SwingConstants.CENTER);
        listTitle.setFont(new Font("Dialog", Font.BOLD, 22));
        listTitle.setForeground(Color.BLACK); // 경고 느낌의 빨간색
        listTitle.setBounds(0, 120, 1600, 40);
        add(listTitle);

        //스크롤 영역 초기화
        scrollPanel = new JPanel(null);
        scrollPanel.setBackground(Color.WHITE);
        // 스크롤
        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBounds(300, 170, 1000, 680);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);

        refreshReviewList();
    }

    private void refreshReviewList() {
        //기존에 붙어있던 카드들을 모두 제거
        scrollPanel.removeAll();

        ReviewManager rm = ReviewManager.getInstance();
        ArrayList<Review> reportedList = rm.getReportedReviews(10); // 10회 이상

        int y = 0;
        int cardHeight = 180;
        int gap = 10;

        if (reportedList.isEmpty()) {
            JLabel noData = new JLabel("신고 누적된 리뷰가 없습니다.", SwingConstants.CENTER);
            noData.setFont(new Font("Dialog", Font.PLAIN, 18));
            noData.setBounds(0, 50, 1000, 50);
            scrollPanel.add(noData);
        } else {
            for (Review r : reportedList) {
                JPanel card = makeReviewCard(r); // 실제 리뷰 객체 전달
                card.setBounds(0, y, 1000, cardHeight);
                scrollPanel.add(card);
                y += cardHeight + gap;
            }
        }

        scrollPanel.setPreferredSize(new Dimension(1000, Math.max(y, 680)));
        scrollPanel.revalidate();
        scrollPanel.repaint();
    }

    /* 개별 리뷰 카드 생성 (삭제 버튼 기능 포함)*/
    private JPanel makeReviewCard(Review r) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // 작성자 ID
        JLabel writer = new JLabel("작성자: " + r.getAuthorID());
        writer.setFont(new Font("Dialog", Font.BOLD, 16));
        writer.setBounds(20, 15, 300, 30);
        card.add(writer);

        // 신고 횟수
        JLabel warning = new JLabel("신고 횟수: " + r.getWarningNum());
        warning.setFont(new Font("Dialog", Font.PLAIN, 16));
        warning.setForeground(Color.BLACK);
        warning.setHorizontalAlignment(SwingConstants.RIGHT);
        warning.setBounds(680, 15, 200, 30);
        card.add(warning);

        // 삭제 버튼
        JButton deleteBtn = new JButton("삭제");
        deleteBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
        deleteBtn.setBackground(new Color(220, 60, 60)); // 빨간색 버튼
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBounds(900, 15, 80, 30);
        deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(deleteBtn);

        // 삭제 버튼 이벤트 리스너
        deleteBtn.addActionListener(e -> {
            // 팝업창
            int answer = JOptionPane.showConfirmDialog(
                    null,
                    "정말로 이 리뷰를 삭제하시겠습니까?\n(삭제 후 복구할 수 없습니다)",
                    "삭제 확인",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // '예' 선택 시 삭제 진행
            if (answer == JOptionPane.YES_OPTION) {
                boolean success = ReviewManager.getInstance().deleteReview(r.getReviewID());
                if (success) {
                    JOptionPane.showMessageDialog(null, "리뷰가 삭제되었습니다.");
                    // 화면 목록 새로고침 (삭제된 리뷰 사라짐)
                    refreshReviewList();
                } else {
                    JOptionPane.showMessageDialog(null, "삭제 실패: 리뷰를 찾을 수 없습니다.");
                }
            }
        });

        // 메뉴 이름 (참고용)
        JLabel title = new JLabel("메뉴: " + r.getMenuName());
        title.setFont(new Font("Dialog", Font.PLAIN, 16));
        title.setBounds(20, 55, 300, 30);
        card.add(title);

        // 리뷰 내용
        JLabel content = new JLabel("<html>" + r.getContent() + "</html>"); // 줄바꿈 지원
        content.setFont(new Font("Dialog", Font.PLAIN, 14));
        content.setVerticalAlignment(SwingConstants.TOP);
        content.setBounds(20, 90, 900, 50);
        card.add(content);

        // 작성일
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        JLabel date = new JLabel(sdf.format(r.getWrittenDate()));
        date.setFont(new Font("Dialog", Font.PLAIN, 12));
        date.setForeground(Color.GRAY);
        date.setBounds(20, 145, 200, 20);
        card.add(date);

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
