package gui;

import model.Menu;
import model.Cafeteria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.Arc2D;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class DollimpanUI extends JPanel {

    private MainGUI mainGUI;

    static class MenuEntry {
        Cafeteria caf;
        Menu menu;

        MenuEntry(Cafeteria caf, Menu menu) {
            this.caf = caf;
            this.menu = menu;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MenuEntry)) return false;
            MenuEntry other = (MenuEntry) o;
            return caf.getName().equals(other.caf.getName()) &&
                    menu.getName().equals(other.menu.getName());
        }

        @Override
        public int hashCode() {
            return (caf.getName() + menu.getName()).hashCode();
        }
    }

    private final List<Cafeteria> cafeterias = new ArrayList<>();
    private final List<MenuEntry> allMenus = new ArrayList<>();
    private final List<MenuEntry> filteredMenus = new ArrayList<>();
    private final List<MenuEntry> wheelMenus = new ArrayList<>();

    // UI 요소
    private JComboBox<String> storeCombo;
    private JComboBox<String> categoryCombo;
    private JTextField keywordField;
    private JPanel resultListPanel;
    private RoulettePanel roulettePanel;
    private JLabel resultLabel;

    public DollimpanUI(MainGUI mainGUI) {

        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        loadFromFile("menus.txt");
        buildMenuEntries();

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(0, 0, 1600, 900);
        add(mainPanel);

        JLabel title = new JLabel("학식 추천 돌림판", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setBounds(0, 30, 1600, 40);
        mainPanel.add(title);

        JSeparator sep = new JSeparator();
        sep.setBounds(200, 85, 1200, 1);
        mainPanel.add(sep);

        // ================== 왼쪽 필터창 ==================
        JPanel filterPanel = new JPanel(null);
        filterPanel.setBackground(new Color(235, 235, 235));
        filterPanel.setBounds(140, 140, 420, 560);
        mainPanel.add(filterPanel);

        JLabel filterTitle = new JLabel("필터 창", SwingConstants.LEFT);
        filterTitle.setFont(new Font("Dialog", Font.BOLD, 20));
        filterTitle.setBounds(20, 20, 200, 30);
        filterPanel.add(filterTitle);

        JLabel storeLabel = new JLabel("식당");
        storeLabel.setBounds(20, 70, 80, 25);
        filterPanel.add(storeLabel);

        storeCombo = new JComboBox<>(buildStoreModel());
        storeCombo.setBounds(100, 70, 200, 25);
        filterPanel.add(storeCombo);

        JLabel categoryLabel = new JLabel("카테고리");
        categoryLabel.setBounds(20, 110, 80, 25);
        filterPanel.add(categoryLabel);

        categoryCombo = new JComboBox<>(buildCategoryModel());
        categoryCombo.setBounds(100, 110, 200, 25);
        filterPanel.add(categoryCombo);

        JLabel keywordLabel = new JLabel("검색어");
        keywordLabel.setBounds(20, 150, 80, 25);
        filterPanel.add(keywordLabel);

        keywordField = new JTextField();
        keywordField.setBounds(100, 150, 200, 25);
        filterPanel.add(keywordField);

        JButton filterBtn = new JButton("검색");
        filterBtn.setBounds(100, 190, 95, 30);
        filterPanel.add(filterBtn);

        JButton resetBtn = new JButton("초기화");
        resetBtn.setBounds(205, 190, 95, 30);
        filterPanel.add(resetBtn);

        JLabel listLabel = new JLabel("결과 목록 (체크 = 추가)");
        listLabel.setBounds(20, 235, 200, 20);
        filterPanel.add(listLabel);

        resultListPanel = new JPanel();
        resultListPanel.setLayout(new BoxLayout(resultListPanel, BoxLayout.Y_AXIS));
        resultListPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(resultListPanel);
        scroll.setBounds(20, 260, 380, 280);
        filterPanel.add(scroll);

        // ================== 오른쪽 돌림판 ==================
        roulettePanel = new RoulettePanel();
        roulettePanel.setBounds(850, 160, 450, 450);
        mainPanel.add(roulettePanel);

        JButton spinBtn = new JButton("돌리기");
        spinBtn.setBounds(1000, 640, 150, 40);
        mainPanel.add(spinBtn);

        resultLabel = new JLabel("결과: -");
        resultLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        resultLabel.setBounds(930, 690, 500, 40);
        mainPanel.add(resultLabel);

        applyFilterAndBuildList();

        filterBtn.addActionListener(e -> applyFilterAndBuildList());

        resetBtn.addActionListener(e -> {
            storeCombo.setSelectedIndex(0);
            categoryCombo.setSelectedIndex(0);
            keywordField.setText("");
            wheelMenus.clear();
            roulettePanel.setMenus(wheelMenus);
            applyFilterAndBuildList();
        });

        spinBtn.addActionListener(e -> doSpin());

        // ⭐ CafeteriaUI와 100% 동일한 하단바
        addBottomNav(mainPanel);
    }

    // ⭐ CafeteriaUI참조 하단 버튼
    private void addBottomNav(JPanel mainPanel) {

        JPanel bottomBar = new JPanel(new GridLayout(1, 3));
        bottomBar.setBounds(320, 740, 960, 80);
        bottomBar.setBackground(new Color(248, 242, 255));

        JButton tabCafe = new JButton("학식");
        JButton tabRD = new JButton("추천돌림판");
        JButton tabMy = new JButton("마이페이지");

        styleTab(tabCafe);
        styleTab(tabRD);
        styleTab(tabMy);

        // ⭐ 현재 화면이 돌림판 → 해당 탭 강조
        tabRD.setForeground(new Color(146, 107, 191));

        tabCafe.addActionListener(e -> mainGUI.showScreen("CAFETERIA"));
        tabRD.addActionListener(e -> mainGUI.showScreen("DOLLIMPAN"));
        tabMy.addActionListener(e -> mainGUI.showScreen("MYPAGE"));

        bottomBar.add(tabCafe);
        bottomBar.add(tabRD);
        bottomBar.add(tabMy);

        mainPanel.add(bottomBar);
    }

    // 하단 바 스타일 cafteriaui 참조
    private void styleTab(JButton b) {
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // 필터·돌림판 로직

    private void doSpin() {
        if (wheelMenus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "돌림판에 메뉴가 없습니다!");
            return;
        }
        if (roulettePanel.isSpinning()) return;

        int idx = (int) (Math.random() * wheelMenus.size());
        MenuEntry chosen = wheelMenus.get(idx);

        roulettePanel.spinToIndex(idx, () -> {
            resultLabel.setText(
                    "결과: " + chosen.menu.getName() +
                            " (" + chosen.caf.getName() + ", " + chosen.menu.getPrice() + "원)"
            );
        });
    }

    private void loadFromFile(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.err.println("⚠ 메뉴 파일을 찾을 수 없음: " + file.getAbsolutePath());
            return;
        }

        Map<String, Cafeteria> map = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] t = line.split("\t");
                if (t.length < 6) continue;

                String store = t[0];
                String name = t[1];
                int price = Integer.parseInt(t[2]);
                String desc = t[3];
                String cat = t[4];
                String img = t[5];

                Cafeteria caf = map.get(store);
                if (caf == null) {
                    caf = new Cafeteria();
                    caf.set(new String[]{store, ""});
                    map.put(store, caf);
                }

                Menu m = new Menu(name, price, desc, cat, img);
                caf.createMenu(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        cafeterias.addAll(map.values());
    }

    private void buildMenuEntries() {
        allMenus.clear();
        for (Cafeteria caf : cafeterias)
            for (Menu m : caf.readMenus())
                allMenus.add(new MenuEntry(caf, m));
    }

    private DefaultComboBoxModel<String> buildStoreModel() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("전체");
        for (Cafeteria caf : cafeterias) set.add(caf.getName());
        return new DefaultComboBoxModel<>(set.toArray(new String[0]));
    }

    private DefaultComboBoxModel<String> buildCategoryModel() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add("전체");
        for (MenuEntry e : allMenus) set.add(e.menu.getCategory());
        return new DefaultComboBoxModel<>(set.toArray(new String[0]));
    }

    private void applyFilterAndBuildList() {

        String storeFilter = (String) storeCombo.getSelectedItem();
        String categoryFilter = (String) categoryCombo.getSelectedItem();
        String keyword = keywordField.getText().trim().toLowerCase();

        filteredMenus.clear();

        for (MenuEntry e : allMenus) {

            if (!"전체".equals(storeFilter) &&
                    !storeFilter.equals(e.caf.getName()))
                continue;

            if (!"전체".equals(categoryFilter) &&
                    !categoryFilter.equalsIgnoreCase(e.menu.getCategory()))
                continue;

            if (!keyword.isEmpty()) {
                String text = (e.menu.getName() + " " +
                        e.menu.getDescription() + " " +
                        e.menu.getCategory()).toLowerCase();
                if (!text.contains(keyword)) continue;
            }
            filteredMenus.add(e);
        }

        resultListPanel.removeAll();

        for (MenuEntry e : filteredMenus) {

            JCheckBox cb = new JCheckBox(
                    e.menu.getName() +
                            " (" + e.caf.getName() + ", " + e.menu.getPrice() + "원)"
            );
            cb.setOpaque(false);
            cb.putClientProperty("entry", e);

            if (wheelMenus.contains(e)) cb.setSelected(true);

            cb.addItemListener(ev -> {
                MenuEntry me = (MenuEntry) cb.getClientProperty("entry");

                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    if (!wheelMenus.contains(me)) wheelMenus.add(me);
                } else {
                    wheelMenus.remove(me);
                }
                roulettePanel.setMenus(wheelMenus);
            });

            resultListPanel.add(cb);
            resultListPanel.add(Box.createVerticalStrut(5));
        }

        resultListPanel.revalidate();
        resultListPanel.repaint();

        roulettePanel.setMenus(wheelMenus);

        resultLabel.setText(filteredMenus.isEmpty()
                ? "결과: (조건 메뉴 없음)"
                : "결과: -");
    }

    // =================== 룰렛 그래픽 ===================

    static class RoulettePanel extends JPanel {

        private List<MenuEntry> menus = new ArrayList<>();
        private int selectedIndex = -1;
        private double rotationAngle = 0;
        private boolean spinning = false;

        public RoulettePanel() {
            setOpaque(false);
        }

        public void setMenus(List<MenuEntry> menus) {
            this.menus = new ArrayList<>(menus);
            repaint();
        }

        public boolean isSpinning() {
            return spinning;
        }

        public void spinToIndex(int index, Runnable onFinished) {
            if (menus.isEmpty()) return;

            spinning = true;
            selectedIndex = -1;
            rotationAngle = 0;

            double anglePer = 360.0 / menus.size();
            double baseFinalAngle = 90 - (index + 0.5) * anglePer;
            double targetAngle = baseFinalAngle + 2160;

            new Thread(() -> {
                double cur = 0;
                double speed = 25;
                double decel = 0.25;

                while (cur < targetAngle) {
                    cur += speed;
                    rotationAngle = cur;
                    repaint();

                    speed -= decel;
                    if (speed < 8) speed = 8;

                    try { Thread.sleep(16); }
                    catch (Exception ignored) {}
                }

                rotationAngle = targetAngle;

                double normalized = (rotationAngle % 360 + 360) % 360;
                double arrowAngle = (360 - normalized + 90) % 360;
                selectedIndex = (int)(arrowAngle / anglePer);

                spinning = false;
                repaint();

                if (onFinished != null) onFinished.run();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight()) - 40;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            if (menus.isEmpty()) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(x, y, size, size);
                g2.setColor(Color.BLACK);
                g2.drawOval(x, y, size, size);
                g2.drawString("메뉴 없음", getWidth() / 2 - 20, getHeight() / 2);
                return;
            }

            double anglePer = 360.0 / menus.size();
            double start = rotationAngle;

            for (int i = 0; i < menus.size(); i++) {

                if (i == selectedIndex) g2.setColor(new Color(255, 210, 210));
                else g2.setColor(new Color(210, 210, 255));

                Arc2D.Double arc =
                        new Arc2D.Double(x, y, size, size, start, anglePer, Arc2D.PIE);
                g2.fill(arc);

                g2.setColor(Color.BLACK);
                g2.draw(arc);

                drawText(g2, menus.get(i).menu.getName(),
                        x, y, size, start, anglePer);

                start += anglePer;
            }

            int csize = size / 3;
            int cx = (getWidth() - csize) / 2;
            int cy = (getHeight() - csize) / 2;

            g2.setColor(Color.WHITE);
            g2.fillOval(cx, cy, csize, csize);
            g2.setColor(Color.GRAY);
            g2.drawOval(cx, cy, csize, csize);

            Polygon arrow = new Polygon();
            int mid = getWidth()/2;

            arrow.addPoint(mid, y + 10);
            arrow.addPoint(mid - 15, y - 10);
            arrow.addPoint(mid + 15, y - 10);

            g2.setColor(Color.RED);
            g2.fillPolygon(arrow);
            g2.setColor(Color.BLACK);
            g2.drawPolygon(arrow);
        }

        private void drawText(Graphics2D g2, String text,
                              int x, int y, int size,
                              double start, double anglePer) {

            double rad = Math.toRadians(start + anglePer / 2);
            int cx = x + size/2;
            int cy = y + size/2;
            int r = size/3;

            int tx = cx + (int)(r * Math.cos(rad));
            int ty = cy - (int)(r * Math.sin(rad));

            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, tx - fm.stringWidth(text)/2, ty + fm.getHeight()/4);
        }
    }
}
