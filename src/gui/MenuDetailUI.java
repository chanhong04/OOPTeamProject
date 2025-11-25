package gui;

import model.Review;
import model.ReviewManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MenuDetailUI extends JFrame {

    private MainGUI mainGUI;
    private String cafeteriaName;
    private String menuName;

    public MenuDetailUI(MainGUI mainGUI, String cafeteriaName, String menuName) {
        this.mainGUI = mainGUI;
        this.cafeteriaName = cafeteriaName;
        this.menuName = menuName;
        initUI();
    }

    // 테스트 실행용
    public MenuDetailUI() {
        this(null, "만권화밥", "공기밥");
    }

    private void initUI() {
        setTitle("메뉴 상세");
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.WHITE);

        // 1. 뒤로가기 버튼
        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(40, 20, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new BackButtonListener());
        cp.add(back);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 70, 820, 1);
        cp.add(sep);



        // [핵심] 2. 파일에서 이미지, 설명 가져오기
        String imgFileName = null;
        String menuDesc = "설명 정보 없음"; // 기본값

        // 여기서 데이터를 찾아옵니다!
        String[] info = findMenuInfo(cafeteriaName, menuName);
        if (info != null) {
            imgFileName = info[0]; // 이미지 파일명
            menuDesc    = info[1]; // 메뉴 설명
        }


        // 3. 이미지 표시 (회색박스 대신)
        ImageIcon icon = loadScaledIcon(imgFileName, 140, 140);
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(80, 100, 140, 140);
        imgLabel.setBorder(new LineBorder(new Color(210,210,210)));

        if (icon != null) {
            imgLabel.setIcon(icon);
        } else {
            imgLabel.setText("No Image");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imgLabel.setBackground(new Color(230,230,230));
            imgLabel.setOpaque(true);
        }
        cp.add(imgLabel);


        // 4. 메뉴 이름 표시
        JLabel menuNameLabel = new JLabel(menuName);
        menuNameLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        menuNameLabel.setBounds(250, 105, 300, 32);
        cp.add(menuNameLabel);


        // 5. 별점 표시
        ReviewManager rm = ReviewManager.getInstance();
        double avgRating = rm.getAverageRatingForMenu(cafeteriaName, menuName);

        JLabel ratingLabel = new JLabel(String.format("평점: %.1f / 5.0", avgRating));
        ratingLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        ratingLabel.setForeground(new Color(255, 153, 0));
        ratingLabel.setBounds(250, 145, 200, 30);
        cp.add(ratingLabel);


        // 6. [추가] 메뉴 설명 표시
        JLabel descLabel = new JLabel("<html>" + menuDesc + "</html>");
        descLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setVerticalAlignment(SwingConstants.TOP);
        descLabel.setBounds(250, 180, 500, 60); // 별점 아래에 위치
        cp.add(descLabel);


        // 7. 리뷰 리스트
        ArrayList<Review> reviewList = rm.findReviewsByMenu(cafeteriaName, menuName);
        int viewX = 60, viewY = 260, viewW = 780, viewH = 300;
        JPanel reviewListPanel = new JPanel(null);
        reviewListPanel.setBackground(Color.WHITE);

        int cardW = viewW - 20, cardH = 150, gapY = 20, y = 0;

        if (reviewList.isEmpty()) {
            JLabel noReview = new JLabel("작성된 리뷰가 없습니다.", SwingConstants.CENTER);
            noReview.setBounds(0, 50, cardW, 30);
            reviewListPanel.add(noReview);
        } else {
            for (Review r : reviewList) {
                JPanel rc = reviewCard(cardW, cardH, r);
                rc.setBounds(0, y, cardW, cardH);
                reviewListPanel.add(rc);
                y += cardH + gapY;
            }
        }
        reviewListPanel.setPreferredSize(new Dimension(viewW - 20, Math.max(y, viewH)));

        JScrollPane reviewScroll = new JScrollPane(reviewListPanel);
        reviewScroll.setBounds(viewX, viewY, viewW, viewH);
        reviewScroll.setBorder(null);
        reviewScroll.getVerticalScrollBar().setUnitIncrement(16);
        cp.add(reviewScroll);


        // 8. 하단 버튼
        JPanel bottomBar = new JPanel(new GridLayout(1, 2));
        bottomBar.setBounds(60, 590, 780, 60);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton cafeBtn = new JButton("학식");
        JButton myBtn   = new JButton("마이페이지");
        styleTab(cafeBtn);
        styleTab(myBtn);

        TabButtonListener tabListener = new TabButtonListener();
        cafeBtn.addActionListener(tabListener);
        myBtn.addActionListener(tabListener);

        bottomBar.add(cafeBtn);
        bottomBar.add(myBtn);
        cp.add(bottomBar);

        setVisible(true);
    }

    // ================= 유틸 메서드 =================

    // 1. 메뉴 정보(이미지, 설명) 찾기
    private String[] findMenuInfo(String cafeName, String mName) {
        File file = new File("menus.txt");

        if (!file.exists()) {
            System.out.println("menus.txt 파일이 없습니다.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\t");

                if (p.length < 6) continue;

                if (p[0].trim().equals(cafeName) && p[1].trim().equals(mName)) {
                    // [5]=이미지, [3]=설명
                    return new String[] { p[5].trim(), p[3].trim() };
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 2. 이미지 로드
    private ImageIcon loadScaledIcon(String fileName, int w, int h) {
        if (fileName == null || fileName.isEmpty()) return null;
        File f = new File("images/" + fileName);
        ImageIcon origin = null;
        if (f.exists()) origin = new ImageIcon(f.getAbsolutePath());
        else {
            java.net.URL url = getClass().getResource("/images/" + fileName);
            if (url != null) origin = new ImageIcon(url);
        }
        if (origin == null) return null;
        Image scaled = origin.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JPanel reviewCard(int w, int h, Review r) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230,230,230)));

        // 1. 별점 표시 (★★☆☆☆)
        String stars = "★".repeat(r.getRating()) + "☆".repeat(5 - r.getRating());
        JLabel starLabel = new JLabel(stars);
        starLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        starLabel.setForeground(new Color(255, 153, 0)); // 주황색
        starLabel.setBounds(18, 12, 100, 20);
        card.add(starLabel);

        // 2. 신고 버튼 및 기능
        JLabel reportLabel = new JLabel("신고 " + r.getWarningNum());
        reportLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        reportLabel.setForeground(new Color(140,140,140));
        reportLabel.setBounds(w - 80, 14, 60, 16);
        reportLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        reportLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        reportLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "이 리뷰를 신고하시겠습니까?",
                        "신고 확인",
                        JOptionPane.YES_NO_OPTION
                );

                if (answer == JOptionPane.YES_OPTION) {
                    boolean success = ReviewManager.getInstance().reportReview(r.getReviewID());
                    if (success) {
                        r.setWarningNum(r.getWarningNum() + 1); // 객체 정보 업데이트
                        reportLabel.setText("신고 " + r.getWarningNum()); // 라벨 즉시 갱신
                        JOptionPane.showMessageDialog(null, "정상적으로 신고되었습니다.");
                    }
                }
            }
        });
        card.add(reportLabel);

        // 3. 메뉴 이름
        JLabel titleLabel = new JLabel(r.getMenuName());
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        titleLabel.setBounds(18, 40, w - 36, 20);
        card.add(titleLabel);

        // 4. 리뷰 내용 (줄바꿈 지원)
        JLabel contentLabel = new JLabel("<html>" + r.getContent() + "</html>");
        contentLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        contentLabel.setBounds(18, 65, w - 36, 40);
        card.add(contentLabel);

        // 5. 작성자 ID
        JLabel writerLabel = new JLabel(r.getAuthorID());
        writerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        writerLabel.setForeground(Color.GRAY);
        writerLabel.setBounds(18, 112, 120, 16);
        card.add(writerLabel);

        // 6. 작성 날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        JLabel dateLabel = new JLabel(sdf.format(r.getWrittenDate()));
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(140,140,140));
        dateLabel.setBounds(18, 128, 120, 16);
        card.add(dateLabel);

        return card;
    }

    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
    }

    class TabButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (mainGUI != null) {
                String cmd = e.getActionCommand();
                if(cmd.equals("학식")) mainGUI.showScreen("CAFETERIA");
                else mainGUI.showScreen("MYPAGE");
                dispose();
            }
        }
    }
    class BackButtonListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (mainGUI != null) mainGUI.showScreen("CAFE1"); // ID에 맞게 수정 필요
            dispose();
        }
    }
}