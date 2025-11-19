package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;   // ★ 파일 입출력 추가

public class LoginUI extends JPanel {

    private MainGUI mainGUI;   // 화면 전환용

    // 로그인에 쓸 필드들
    private JTextField idField;
    private JPasswordField pwField;

    public LoginUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 상단 타이틀
        JLabel topText = new JLabel("학식 추천 앱이름", SwingConstants.CENTER);
        topText.setFont(new Font("Dialog", Font.BOLD, 44));
        topText.setBounds(0, 90, 1600, 60);
        add(topText);

        // 아이디
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        idLabel.setBounds(540, 210, 200, 24);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(540, 240, 520, 48);
        styleInput(idField);
        idField.setText("Value");                      // 플레이스홀더
        idField.setForeground(new Color(160,160,160));
        add(idField);

        // 비밀번호
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        pwLabel.setBounds(540, 305, 200, 24);
        add(pwLabel);

        pwField = new JPasswordField();
        pwField.setBounds(540, 336, 520, 48);
        styleInput(pwField);
        pwField.setText("Value");                      // 플레이스홀더
        pwField.setForeground(new Color(160,160,160));
        pwField.setEchoChar((char)0);                  // 플레이스홀더 보이게
        add(pwField);

        // 플레이스홀더 제거용 포커스 리스너
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("Value".equals(idField.getText())) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }
        });

        pwField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String txt = new String(pwField.getPassword());
                if ("Value".equals(txt)) {
                    pwField.setText("");
                    pwField.setForeground(Color.BLACK);
                    pwField.setEchoChar('●');
                }
            }
        });

        // Sign In (검은색 라운드 버튼)
        JButton loginButton = new RoundedButton("Sign In", 14);
        loginButton.setBounds(540, 412, 520, 50);
        loginButton.setBackground(new Color(45,45,45));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        add(loginButton);

        // Register 버튼
        JButton register = new RoundedButton("Register", 18);
        register.setBounds(720, 720, 160, 42);
        register.setBackground(new Color(45,45,45));
        register.setForeground(Color.WHITE);
        register.setFont(new Font("Dialog", Font.PLAIN, 16));
        register.setFocusPainted(false);
        register.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        add(register);

        // 버튼 리스너 연결
        ButtonListener listener = new ButtonListener();
        loginButton.addActionListener(listener);
        register.addActionListener(listener);
    }

    // 공통 인풋 디자인
    private void styleInput(JTextComponent field) {
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setFont(new Font("Dialog", Font.PLAIN, 16));
        Border outer = new LineBorder(new Color(220, 220, 220), 1, true); // 둥근 테두리
        Border inner = new EmptyBorder(10, 14, 10, 14);                   // 패딩
        field.setBorder(new CompoundBorder(outer, inner));
    }

    // 둥근 버튼 (기존 스타일)
    static class RoundedButton extends JButton {
        private final int radius;
        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius*2, radius*2);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override
        public boolean isOpaque() { return false; }
    }

    // 버튼 동작 (로그인 / 회원가입 화면 전환)
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            switch(buttonName) {
                case "Sign In":
                    handleLogin();
                    break;
                case "Register":
                    // 카드레이아웃으로 회원가입 화면으로 전환
                    mainGUI.showScreen("REGISTER");
                    break;
                default:
                    break;
            }
        }
    }

    // ★ 로그인 기능: users.txt 직접 읽어서 확인 (RegisterUI와 동일한 방식)
    private void handleLogin() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());

        if ("Value".equals(id)) id = "";
        if ("Value".equals(pw)) pw = "";

        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "아이디와 비밀번호를 모두 입력해주세요.",
                    "입력 오류",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        File file = new File("users.txt"); // 실행 위치 기준

        if (!file.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "users.txt 파일을 찾을 수 없습니다.",
                    "파일 오류",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;          // 빈 줄 건너뛰기

                // 공백(스페이스/탭) 기준으로 분리
                String[] tokens = line.split("\\s+");
                if (tokens.length < 2) continue;       // 아이디/비밀번호 없으면 건너뛰기

                String fileId = tokens[0];
                String filePw = tokens[1];

                if (id.equals(fileId) && pw.equals(filePw)) {
                    found = true;
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "로그인 처리 중 오류가 발생했습니다.",
                    "파일 오류",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (found) {
            // 로그인 성공 → 학식 화면으로 전환
            mainGUI.showScreen("CAFETERIA");
        } else {
            // 로그인 실패
            JOptionPane.showMessageDialog(
                    this,
                    "아이디 또는 비밀번호가 올바르지 않습니다.",
                    "로그인 실패",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}