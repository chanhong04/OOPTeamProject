package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterUI extends JPanel {
	private MainGUI mainGUI;
	JLabel title;
	JLabel idLabel;
	JTextField idField;
	JLabel pwLabel;
	JPasswordField pwField;
	JLabel nameLabel;
	JTextField nameField;
	JButton cancel;
	JButton signUp;
	
	private final String ID_REGEX = "^[a-zA-Z][a-zA-Z0-9_-]{4,19}$";
    private final String PW_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";
    private final String NAME_REGEX = "^[a-zA-Z0-9가-힣_]{2,10}$";
    
    public RegisterUI(MainGUI mainGUI) {
    	this.mainGUI = mainGUI;
    	mainGUI.mainFrame.setTitle("회원 가입");
        
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        title = new JLabel("회원 가입", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 44));
        title.setBounds(0, 110, 1600, 60);
        this.add(title);

        idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        idLabel.setBounds(420, 250, 200, 24);
        this.add(idLabel);

        idField = new JTextField();
        idField.setBounds(420, 282, 560, 48);
        styleInput(idField);
        idField.setText("");                      // 디자인용 플레이스홀더
        idField.setForeground(new Color(160,160,160));
        this.add(idField);

        pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        pwLabel.setBounds(420, 350, 200, 24);
        this.add(pwLabel);

        pwField = new JPasswordField();
        pwField.setBounds(420, 382, 560, 48);
        styleInput(pwField);
        pwField.setText("");                      // 디자인용 플레이스홀더
        pwField.setForeground(new Color(160,160,160));
        pwField.setEchoChar((char)0);                  // 디자인용(보이게)
        this.add(pwField);
        
        nameLabel = new JLabel("이름");
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        nameLabel.setBounds(420, 450, 200, 24);
        this.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(420, 482, 560, 48);
        styleInput(nameField);
        nameField.setText("");                      // 디자인용 플레이스홀더
        nameField.setForeground(new Color(160,160,160)); 
        this.add(nameField);

        cancel = new JButton("Cancel");
        cancel.setBounds(420, 660, 120, 44);
        cancel.setBorderPainted(false);
        cancel.setFocusPainted(false);
        cancel.setContentAreaFilled(false);
        cancel.setFont(new Font("Dialog", Font.PLAIN, 16));
        this.add(cancel);

        signUp = new RoundedButton("Sign up", 14);
        signUp.setBounds(420 + 560 - 180, 656, 180, 50); // 입력창 오른쪽 정렬
        signUp.setBackground(new Color(45,45,45));
        signUp.setForeground(Color.WHITE);
        signUp.setFont(new Font("Dialog", Font.PLAIN, 18));
        signUp.setFocusPainted(false);
        signUp.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        this.add(signUp);

        MenuListener listener = new MenuListener();
        cancel.addActionListener(listener);
        signUp.addActionListener(listener);
 
        setVisible(true);
    }

    private void styleInput(JTextComponent field) {
        field.setBackground(Color.WHITE);
        field.setOpaque(true);
        field.setFont(new Font("Dialog", Font.PLAIN, 16));
        Border outer = new LineBorder(new Color(220,220,220), 1, true); // 라운드 테두리
        Border inner = new EmptyBorder(10,14,10,14);                    // 내부 패딩
        field.setBorder(new CompoundBorder(outer, inner));
    }

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
    
    class MenuListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "Cancel":
                    mainGUI.showScreen("LOGIN");
                    break;
                case "Sign up":
                	String id = idField.getText();
                	String name = nameField.getText();
                	String pw = new String(pwField.getPassword());
                	
                	String errorMsg = validateForm(id,name,pw);
                	
                	if(errorMsg == null)
                		showSignUpSuccess();
                	else
                		showErrorMsg(errorMsg);
                    break;
            }
        }
    }
    
    private String validateForm(String id, String name, String pw) {
    	if(id.matches(ID_REGEX) == false) {
    		return "아이디 형식이 올바르지 않습니다. (영문/숫자, 5~20자)";
    	}
    	if(pw.matches(PW_REGEX) == false) {
    		return "비밀번호 형식이 올바르지 않습니다. (영문/숫자/특수문자 조합, 8자 이상)";
    	}
    	if(name.matches(NAME_REGEX) == false) {
    		return "이름(닉네임) 형식이 올바르지 않습니다. (한글/영문/숫자, 2~10자)";
    	}
    	return null;
    }
    
    private void showSignUpSuccess() {
        JDialog signUpDlg = new JDialog(mainGUI.mainFrame, "회원가입", true);
        signUpDlg.setLayout(new BorderLayout(10,10));
        signUpDlg.setSize(360, 160);
        signUpDlg.setLocationRelativeTo(this);

        JLabel msg = new JLabel("회원가입 성공!", SwingConstants.CENTER);
        msg.setFont(new Font("Dialog", Font.BOLD, 18));
        signUpDlg.add(msg, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton ok = new JButton("OK");
        	           
        ok.addActionListener(ev -> {
            signUpDlg.dispose();
            mainGUI.showScreen("LOGIN");
            }
        );
        btnPanel.add(ok);
        signUpDlg.add(btnPanel, BorderLayout.SOUTH);

        signUpDlg.setVisible(true);
      
    }
    // 이 부분 완성해주셨으면 합니다.
    private void showErrorMsg(String errorMsg) {
    	
    }
}
