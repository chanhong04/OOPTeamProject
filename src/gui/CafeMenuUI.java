package gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CafeMenuUI extends JPanel {

    private MainGUI mainGUI;   // 카드레이아웃 전환용
    private String cafeName;   // 필터용 식당 이름 (menus.txt 1열과 일치해야 함)

    // txt 한 줄 -> 메뉴 정보 구조
    private static class MenuItem {
        String cafe;      // 식당 이름
        String name;      // 메뉴 이름
        String price;     // 가격 (문자열로 그대로 사용)
        String desc;      // 설명
        String category;  // 카테고리
        String imageFile; // 이미지 파일명 (png)
    }

    public CafeMenuUI(MainGUI mainGUI, String cafeName) {
        this.mainGUI = mainGUI;
        this.cafeName = cafeName;
        initUI();
    }

    private void initUI() {
        setLayout(null);
        setBackground(Color.WHITE);

        // ── 상단 바 ───────────────────────────────────────────────
        JLabel back = new JLabel("←", SwingConstants.CENTER); // 이전 버튼
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(110, 18, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(back);

        back.addMouseListener(new BackButtonListener());

        // 상단 타이틀 (식당 이름)
        JLabel title = new JLabel(cafeName, SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 16, 1600, 34);
        add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(260, 66, 1080, 1);
        add(sep);

        // ── 메뉴 데이터 로드 ──────────────────────────────────────
        List<MenuItem> menus = loadMenuItems();

        // ── 스크롤 영역 ──────────────────────────────────────────
        int viewX = 260, viewY = 80, viewW = 1080, viewH = 630;
        JPanel list = new JPanel(null);              // 절대 배치
        list.setBackground(Color.WHITE);

        int cardW = 300, cardH = 210;
        int leftX = 40, rightX = 40 + 520;
        int gapY = 50;

        int index = 0;
        for (MenuItem item : menus) {
            int col = index % 2;          // 0: 왼쪽, 1: 오른쪽
            int row = index / 2;

            int x = (col == 0) ? leftX : rightX;
            int y = 20 + row * (cardH + gapY);

            JPanel card = menuCard(x, y, cardW, cardH, item);
            list.add(card);

            index++;
        }

        // 메뉴가 하나도 없더라도 최소 높이 확보
        int totalRows = (menus.size() + 1) / 2;
        int prefH = 20 + Math.max(1, totalRows) * (cardH + gapY);
        list.setPreferredSize(new Dimension(viewW, prefH));

        JScrollPane scroll = new JScrollPane(
                list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBounds(viewX, viewY, viewW, viewH);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll);
    }

    // ── 단일 메뉴 카드 ───────────────────────────────────────────
    private JPanel menuCard(int x, int y, int w, int h, MenuItem item) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, w, h);
        card.setBorder(new LineBorder(new Color(80,80,80), 1));
        card.setBackground(Color.WHITE);

        // 이미지 영역
        JPanel img = new JPanel(null);
        img.setBackground(new Color(230, 230, 230));
        img.setBounds(18, 16, w - 36, 120);
        img.setBorder(new LineBorder(new Color(210,210,210)));
        card.add(img);

        // 메뉴 이미지 로드 (png)
        if (item.imageFile != null && !item.imageFile.isEmpty()) {
            ImageIcon icon = loadScaledIcon(item.imageFile, w - 36, 120);
            if (icon != null) {
                JLabel imgLabel = new JLabel(icon, SwingConstants.CENTER);
                imgLabel.setBounds(0, 0, w - 36, 120);
                img.add(imgLabel);
            }
        }

        JLabel name = new JLabel(item.name != null ? item.name : "메뉴 이름");
        name.setFont(new Font("Dialog", Font.PLAIN, 14));
        name.setBounds(22, 144, w - 44, 20);
        card.add(name);

        String priceText = (item.price != null && !item.price.isEmpty())
                ? item.price + "원"
                : "~원";
        JLabel price = new JLabel(priceText);
        price.setFont(new Font("Dialog", Font.BOLD, 14));
        price.setBounds(22, 166, w - 44, 20);
        card.add(price);

        if (item.desc != null && !item.desc.isEmpty()) {
            card.setToolTipText(item.desc);  // 마우스 올리면 설명 표시
        }

        // 카드 클릭 시 상세 페이지로 이동 (현재는 별도 프레임 사용)
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MenuDetailUI(mainGUI, cafeName, item.name);   // 나중에 item 정보 넘기고 싶으면 생성자 수정
            }
        });

        return card;
    }

    private List<MenuItem> loadMenuItems() {
        List<MenuItem> list = new ArrayList<>();

        File file = new File("menus.txt");
        if (!file.exists()) {
            System.out.println("menus.txt 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
            return list;
        }

        try {
            List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            for (String line : allLines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] cols = line.split("\\t");

                if (cols.length < 4) continue;

                String cafe = cols[0].trim();
                if (!cafe.equals(cafeName)) {
                    continue;
                }

                MenuItem item = new MenuItem();
                item.cafe      = cafe;              // [0] 식당
                item.name      = cols[1].trim();    // [1] 메뉴명
                item.price     = cols[2].trim();    // [2] 가격
                item.desc      = cols[3].trim();    // [3] 설명

                // [4] 카테고리 (한식, 중식 등) - 데이터가 있을 때만
                if (cols.length > 4) {
                    item.category = cols[4].trim();
                } else {
                    item.category = "";
                }

                // [5] 이미지 파일명 (plain_rice.png 등) - 데이터가 있을 때만
                if (cols.length > 5) {
                    item.imageFile = cols[5].trim();
                } else {
                    item.imageFile = null;
                }

                list.add(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ── 이미지 로드/스케일 ───────────────────────────────────────
    private ImageIcon loadIcon(String fileName) {
        if (fileName == null || fileName.isEmpty()) return null;

        // 예시: /images/ 아래에 png 파일들이 있다고 가정
        // (원하는 경로에 맞게 수정: /images/menu/ 등)
        java.net.URL url = getClass().getResource("/images/" + fileName);
        if (url == null) {
            System.out.println("이미지 파일을 찾을 수 없습니다: " + fileName);
            return null;
        }
        return new ImageIcon(url);
    }

    private ImageIcon loadScaledIcon(String fileName, int targetW, int targetH) {
        ImageIcon original = loadIcon(fileName);
        if (original == null) return null;

        int iw = original.getIconWidth();
        int ih = original.getIconHeight();
        double scale = Math.min((double) targetW / iw, (double) targetH / ih);
        int newW = (int)(iw * scale);
        int newH = (int)(ih * scale);

        Image scaled = original.getImage()
                .getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // ── 상단 '←' 이전 버튼 동작 ─────────────────────────────────
    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (mainGUI != null) {
                mainGUI.showScreen("CAFETERIA"); // 학식 메인으로
            }
        }
    }
}