package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RegisterUI extends JPanel {

    // 카드레이아웃 전환용
    private MainGUI mainGUI;

    // 가입 정보 필드
    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;

    public RegisterUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(null);
        setBackground(Color.WHITE);

        // 타이틀
        JLabel title = new JLabel("회원 가입", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 44));
        title.setBounds(0, 110, 1600, 60);
        add(title);

        // 아이디
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        idLabel.setBounds(420, 250, 200, 24);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(420, 282, 560, 48);
        styleInput(idField);
        idField.setText("");
        idField.setForeground(new Color(160,160,160));
        add(idField);

        // 비밀번호
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        pwLabel.setBounds(420, 350, 200, 24);
        add(pwLabel);

        pwField = new JPasswordField();
        pwField.setBounds(420, 382, 560, 48);
        styleInput(pwField);
        pwField.setText("");
        pwField.setForeground(new Color(160,160,160));
        pwField.setEchoChar((char)0);   // 디자인용(보이게)
        add(pwField);

        // 이름
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        nameLabel.setBounds(420, 450, 200, 24);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(420, 482, 560, 48);
        styleInput(nameField);
        nameField.setText("");
        nameField.setForeground(new Color(160,160,160));
        add(nameField);

        // 하단 버튼 영역
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(420, 660, 120, 44);
        cancel.setBorderPainted(false);
        cancel.setFocusPainted(false);
        cancel.setContentAreaFilled(false);
        cancel.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(cancel);

        JButton signUp = new RoundedButton("Sign up", 14);
        signUp.setBounds(420 + 560 - 180, 656, 180, 50); // 입력창 오른쪽 정렬
        signUp.setBackground(new Color(45,45,45));
        signUp.setForeground(Color.WHITE);
        signUp.setFont(new Font("Dialog", Font.PLAIN, 18));
        signUp.setFocusPainted(false);
        signUp.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        add(signUp);

        // 동작 연결
        MenuListener listener = new MenuListener();
        cancel.addActionListener(listener);
        signUp.addActionListener(listener);
    }

    private void styleInput(JTextComponent field) {
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setFont(new Font("Dialog", Font.PLAIN, 16));
        Border outer = new LineBorder(new Color(220,220,220), 1, true); // 라운드 테두리
        Border inner = new EmptyBorder(10,14,10,14);                    // 내부 패딩
        field.setBorder(new CompoundBorder(outer, inner));
    }

    // 라운드 버튼
    static class RoundedButton extends JButton {
        private final int radius;
        RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius*2, radius*2);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public boolean isOpaque() { return false; }
    }

    // 버튼 동작 (Cancel / Sign up)
    class MenuListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "Cancel":
                    // JFrame 새로 만들지 말고 로그인 화면으로 카드 전환
                    mainGUI.showScreen("LOGIN");
                    break;
                case "Sign up":
                    handleSignUp();
                    break;
            }
        }
    }

    // 회원가입 처리 + users.txt 저장
    private void handleSignUp() {
        String id = idField.getText().trim();
        String pw = new String(pwField.getPassword());
        String name = nameField.getText().trim();

        if (id.isEmpty() || pw.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "아이디, 비밀번호, 이름을 모두 입력해주세요.",
                    "입력 오류",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 권한, 레벨은 고정: user 0
        String role = "user";
        String level = "0";

        File file = new File("users.txt"); // 실행 위치 기준

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // 한 줄 추가: 아이디 비밀번호 이름 user 0
            out.printf("\n%s\t%s\t%s\t%s\t%s%n", id, pw, name, role, level);

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "회원정보 저장 중 오류가 발생했습니다.",
                    "파일 오류",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 저장이 정상적으로 끝났으면 성공 창 띄우기
        showSignUpSuccess();
    }

    // 가입 성공(중앙 표시 후 로그인 화면으로 이동)
    private void showSignUpSuccess() {
        JOptionPane.showMessageDialog(
                this,
                "회원가입 성공!",
                "회원가입",
                JOptionPane.INFORMATION_MESSAGE
        );
        // OK 누르면 로그인 화면으로
        mainGUI.showScreen("LOGIN");
    }
}
