package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class LoginUI extends JPanel {

	private MainGUI mainGUI;
	
	public LoginUI(MainGUI mainGUI) {
		// TODO Auto-generated constructor stub
		
		this.mainGUI = mainGUI;
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		
        JLabel topText = new JLabel("학식 추천 앱이름", SwingConstants.CENTER);
        topText.setFont(new Font("Dialog", Font.BOLD, 44));
        topText.setBounds(0, 90, 1600, 60);
        this.add(topText);

        // 아이디
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        idLabel.setBounds(540, 210, 200, 24);
        this.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(540, 240, 520, 48);
        styleInput(idField);
        idField.setText("");
        idField.setForeground(new Color(160,160,160));
        this.add(idField);

        // 비밀번호
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        pwLabel.setBounds(540, 305, 200, 24);
        this.add(pwLabel);

        JPasswordField pwField = new JPasswordField();
        pwField.setBounds(540, 336, 520, 48);
        styleInput(pwField);
        pwField.setText("");                // 디자인용 플레이스홀더
        pwField.setForeground(new Color(160,160,160));
        pwField.setEchoChar((char)0);            // 디자인용: 텍스트 보이게
        this.add(pwField);

        // Sign In (검은색 라운드)
        JButton loginButton = new RoundedButton("Sign In", 14);
        loginButton.setBounds(540, 412, 520, 50);
        loginButton.setBackground(new Color(45,45,45));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        this.add(loginButton);

        // 하단 Register (작은 라운드)
        JButton register = new RoundedButton("Register", 18);
        register.setBounds(720, 720, 160, 42);
        register.setBackground(new Color(45,45,45));
        register.setForeground(Color.WHITE);
        register.setFont(new Font("Dialog", Font.PLAIN, 16));
        register.setFocusPainted(false);
        register.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
        this.add(register);

        //동작 메소드
        ButtonListener listener = new ButtonListener();
        loginButton.addActionListener(listener);
        register.addActionListener(listener);
	}
	
	//디자인
    private void styleInput(JTextComponent field) {
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setFont(new Font("Dialog", Font.PLAIN, 16));
        Border outer = new LineBorder(new Color(220,220,220), 1, true); // 둥근 테두리
        Border inner = new EmptyBorder(10,14,10,14);                    // 패딩
        field.setBorder(new CompoundBorder(outer, inner));
    }

    // 둥근 버튼
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

    

    // UI 변경 동작
    class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            switch(buttonName) {
                case "Sign In":
                	mainGUI.showScreen("CAFETERIA");
                	break;
                case "Register":
                    mainGUI.showScreen("REGISTER");
                    break;
                default:
                    break;
            }
        }
    }
}
