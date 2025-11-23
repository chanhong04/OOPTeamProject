package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CafeInfoUI extends JFrame {

    // ---------- 카페 데이터 구조 ----------
    private static class CafeData {
        int id;
        String name;
        String location;
        String position;
        String hours;
        String description;
    }

    // cafeterias.txt 내용을 id -> CafeData 로 캐싱
    private static final Map<Integer, CafeData> CAFE_MAP = loadCafeData();

    private static Map<Integer, CafeData> loadCafeData() {
        Map<Integer, CafeData> map = new HashMap<>();

        File file = new File("cafeterias.txt");  // 프로젝트 루트에 있는 txt
        if (!file.exists()) {
            System.out.println("cafeterias.txt 못 찾음: " + file.getAbsolutePath());
            return map;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",", 6); // id,이름,위치,입구기준,시간,설명
                if (p.length < 6) continue;

                CafeData c = new CafeData();
                c.id          = Integer.parseInt(p[0].trim());
                c.name        = p[1].trim();
                c.location    = p[2].trim();
                c.position    = p[3].trim();
                c.hours       = p[4].trim();
                c.description = p[5].trim();

                map.put(c.id, c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }


    // ---------- 필드 ----------
    private MainGUI mainGUI;
    private int cafeId;

    public CafeInfoUI(MainGUI mainGUI, int cafeId) {
        this.mainGUI = mainGUI;
        this.cafeId = cafeId;
        initUI();
    }

    public CafeInfoUI() {
        this(null, 1);
    }

    // ---------- UI 구성 ----------
    private void initUI() {
        CafeData data = CAFE_MAP.get(cafeId);
        if (data == null) {
            JOptionPane.showMessageDialog(this,
                    "해당 ID의 식당 정보를 찾을 수 없습니다: " + cafeId);
            dispose();
            return;
        }

        setTitle("상세 정보");
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Container cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.WHITE);

        // 상단 바 -------------------------------------------------
        JLabel back = new JLabel("←", SwingConstants.CENTER);
        back.setFont(new Font("Dialog", Font.PLAIN, 22));
        back.setBounds(40, 20, 40, 30);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cp.add(back);
        back.addMouseListener(new BackButtonListener());

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 70, 820, 1);
        cp.add(sep);

        // ====== 왼쪽: 이미지 영역 =================================
        int imgX = 40;
        int imgY = 150;   // ← 버튼보다 약 100px 아래
        int imgW = 260;
        int imgH = 260;

        JPanel imagePanel = new JPanel(new GridLayout(1, 1));
        imagePanel.setBackground(Color.WHITE);
        addImageLabel(imagePanel, getImageFileName(cafeId));

        JScrollPane imageScroll = new JScrollPane(imagePanel);
        imageScroll.setBounds(imgX, imgY, imgW, imgH);
        imageScroll.setBorder(BorderFactory.createTitledBorder("사진"));
        cp.add(imageScroll);

        // ====== 가운데: 텍스트 영역 ===============================
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Dialog", Font.PLAIN, 14));

        infoArea.setText(buildInfoText(data));

        int infoX = imgX + imgW + 40;
        int infoY = imgY;
        int infoW = 900 - infoX - 40;
        int infoH = 470;

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBounds(infoX, infoY, infoW, infoH);
        infoScroll.setBorder(
                BorderFactory.createTitledBorder(data.name + " 정보"));
        cp.add(infoScroll);
        
        setVisible(true);
    }

    // ---------- 텍스트 구성 ----------
    private String buildInfoText(CafeData c) {
        StringBuilder sb = new StringBuilder();
        sb.append("[이름] ").append(c.name).append("\n\n");
        sb.append("[위치] ").append(c.location)
          .append(" (").append(c.position).append(")\n\n");
        sb.append("[영업시간] ").append(c.hours).append("\n\n");
        sb.append("[소개]\n").append(c.description).append("\n");
        return sb.toString();
    }

    // ---------- 공통 유틸 ----------
    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // 이미지 파일 이름 매핑
    private String getImageFileName(int id) {
        switch (id) {
            case 1: return "만권화밥.png";
            case 2: return "쑝쑝돈까스.png";
            case 3: return "버거앤타코.png";
            case 4: return "위델가.png";
            case 5: return "신머이쌀국수.png";
            default: return null;
        }
    }

    // 이미지 로드
    private ImageIcon loadIcon(String fileName) {
        if (fileName == null) return null;
        java.net.URL url = CafeInfoUI.class.getResource("/images/" + fileName);
        if (url == null) {
            System.out.println("이미지 파일을 찾을 수 없습니다: " + fileName);
            return null;
        }
        return new ImageIcon(url);
    }

    // 비율 유지 스케일링
    private ImageIcon loadScaledIcon(String fileName, int targetW, int targetH) {
        ImageIcon original = loadIcon(fileName);
        if (original == null) return null;

        int iw = original.getIconWidth();
        int ih = original.getIconHeight();
        double scale = Math.min((double) targetW / iw, (double) targetH / ih);

        int newW = (int) (iw * scale);
        int newH = (int) (ih * scale);

        Image scaled = original.getImage()
                .getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void addImageLabel(JPanel panel, String fileName) {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = loadScaledIcon(fileName, 220, 220);
        if (icon != null) {
            lbl.setIcon(icon);
        } else {
            lbl.setText("이미지 없음");
        }
        panel.add(lbl);
    }


    class BackButtonListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (mainGUI != null) {
                // CAFE1, CAFE2, ... 으로 돌아가게
                mainGUI.showScreen("CAFE" + cafeId);
            }
            dispose();
        }
    }
}
