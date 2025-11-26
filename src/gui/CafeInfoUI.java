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
        String description;
    }

    // cafeterias.txt 내용을 id -> CafeData 로 캐싱
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
            int idCounter = 1; // 파일에 id가 없으므로 자동 생성
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // 공백(스페이스/탭) 기준으로 두 개 항목(name, location) 분리
                String[] p = line.split("\\s+", 2);
                if (p.length < 2) continue;

                CafeData c = new CafeData();
                c.id       = idCounter++;
                c.name     = p[0].trim();
                c.location = p[1].trim();

                
                cafeDescription(c);

                map.put(c.id, c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

 // 각 식당별 position / hours / description 하드코딩
    private static void cafeDescription(CafeData c) {
        switch (c.name) {
            case "만권화밥":
                c.description =
                        "만권화밥은 고추장, 간장, 된장을 기본으로 하는 한국식 소스에 숯불에 구운 재료를 밥과 함께 제공하는 덮밥 전문점입니다.\n"
                        + "퓨전 음식이지만 한국적인 맛을 살려 남녀노소 누구나 좋아하며,\n"
                        + "다양한 메뉴(예: 숯불김치돈육화밥, 숯불소고기화밥)를 합리적인 가격에 즐길 수 있는 곳입니다.";
                break;

            case "숑숑돈까스":
               c.description =
                        "숑숑돈까스는 매콤 돈까스, 옛날 왕 돈까스, 마라 왕 돈까스 등 다양한 메뉴를 제공하는 돈까스 전문점입니다.\n"
                        + "일반적으로 가격이 저렴하면서도 양이 푸짐한 가성비 맛집으로 알려져 있으며, 특히 튀김이 바삭하고 소스가 맛있는 것이 특징입니다.";
                break;

            case "버거&타코":
               c.description =
                        "버거앤타코는 수제버거와 타코를 전문으로 하는 외식 브랜드입니다.\n"
                        + "‘버거는 묵직하게, 타코는 강렬하게’라는 콘셉트 아래, 즉석에서 신선한 재료로 조리한 버거와 타코를 제공하며,\n"
                        + "특히 ‘BT버거앤타코’는 퀘사디아, 비프 살사, 치즈버거 등 다양한 메뉴를 선보입니다.";
                break;

            case "위델가":
               c.description =
                        "위델가는 경기대학교 내 학생식당 코너 중 하나로, ‘아슐랭 경기대학교점(위델가)’으로도 알려져 있습니다.\n"
                        + "한식 중심의 메뉴를 합리적인 가격에 제공하며, 따뜻한 한 끼 식사를 원하는 학생들에게 인기가 많습니다.";
                break;

            case "신머이쌀국수":
               c.description =
                        "신머이쌀국수는 베트남 현지의 맛을 재현한 쌀국수 전문 브랜드입니다.\n"
                        + "쌀로 만든 국수와 육수, 고기, 채소 등을 넣어 칠리나 라임, 고수 등을 곁들여 먹는 전통 베트남 요리이며, ‘신머이’는 베트남어로 ‘환영합니다’라는 뜻을 갖습니다.\n"
                        + "특히 닭반마리 쌀국수는 발명특허를 받은 독자적인 레시피를 사용해 신머이만의 특별한 맛을 제공합니다.";
                break;
        }
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
        sb.append("[위치] ").append(c.location).append("\n\n");
        sb.append("[정보] ").append(c.description).append("\n");
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
            case 2: return "버거앤타코.png";
            case 3: return "신머이쌀국수.png";
            case 4: return "쑝쑝돈까스.png";
            case 5: return "위델가.png";
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
}