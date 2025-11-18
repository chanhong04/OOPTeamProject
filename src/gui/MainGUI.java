package gui;

import javax.swing.*;
import model.MainApp;

import java.awt.*;

public class MainGUI {
    private static MainGUI main = null;
    public static MainGUI getInstance() {
        if (main == null)
            main = new MainGUI();
        return main;
    }

    static MainApp app = MainApp.getInstance();

    public static void main(String[] args) {
        app.run();
        startGUI();
    }
    
    public static void startGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainGUI.getInstance().createAndShowGUI();
            }
        });
    }

    private CardLayout cardLayout;
    private JPanel mainContainerPanel;

    private CafeteriaUI cafeteriaPanel;
    private RegisterUI registerPanel;
    private LoginUI loginPanel;
    private Cafe1MenuUI	cafe1Panel;
    private MyPage mypagePanel;
    private MyOrder myorderPanel;
    private MyReviewUi myreviewPanel;
    //private MenuDetailUI menudetailPanel;
    
    static JFrame mainFrame = new JFrame("학식 추천 앱이름");

    public void createAndShowGUI() {
        mainFrame.setSize(1600, 900);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // 각각의 화면을 JPanel로 생성 (this를 넘겨서 화면 전환에 사용)
        loginPanel = new LoginUI(this);
        cafeteriaPanel = new CafeteriaUI(this);
        registerPanel = new RegisterUI(this);
        cafe1Panel = new Cafe1MenuUI(this);
        mypagePanel = new MyPage(this);
        myorderPanel = new MyOrder(this);
        myreviewPanel = new MyReviewUi(this);
        //menudetailPanel = new MenuDetailUI(this);

        cardLayout = new CardLayout();
        mainContainerPanel = new JPanel(cardLayout);

        mainContainerPanel.add(loginPanel, "LOGIN");
        mainContainerPanel.add(cafeteriaPanel, "CAFETERIA");
        mainContainerPanel.add(registerPanel, "REGISTER");
        mainContainerPanel.add(cafe1Panel, "CAFE1");
        mainContainerPanel.add(mypagePanel, "MYPAGE");
        mainContainerPanel.add(myorderPanel, "MYORDER");
        mainContainerPanel.add(myreviewPanel, "MYREVIEW");
        //mainContainerPanel.add(menudetailPanel, "MENUDETAIL");

        mainFrame.setContentPane(mainContainerPanel);
        mainFrame.setVisible(true);

        // 시작 화면은 로그인 화면
        showScreen("LOGIN");
    }

    public void showScreen(String panelName) {
        cardLayout.show(mainContainerPanel, panelName);
    }
}
