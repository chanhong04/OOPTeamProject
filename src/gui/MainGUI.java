package gui;

import javax.swing.*;

//import model.MainApp;

import java.awt.*;

public class MainGUI {
    private static MainGUI main = null;
    public static MainGUI getInstance() {
        if (main == null)
            main = new MainGUI();
        return main;
    }

    //static MainApp app = MainApp.getInstance();

    public static void main(String[] args) {
        // 사용자/메뉴 데이터 로드
        //app.run();
        // GUI 시작
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
    private CafeMenuUI cafe1Panel;
    private CafeMenuUI cafe2Panel;
    private CafeMenuUI cafe3Panel;
    private CafeMenuUI cafe4Panel;
    private CafeMenuUI cafe5Panel;
    private MyPage mypagePanel;
    private MyOrder myorderPanel;
    private MyReviewUi myreviewPanel;
    //private RecommandUI myrecommPanel;
    //private WriteReviewUI writereviewPanel;
    
    static JFrame mainFrame = new JFrame("학식 추천 앱이름");

    public void createAndShowGUI() {
        mainFrame.setSize(1600, 900);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // 각각의 화면을 JPanel로 생성 (this를 넘겨서 화면 전환에 사용)
        loginPanel = new LoginUI(this);
        cafeteriaPanel = new CafeteriaUI(this);
        registerPanel = new RegisterUI(this);
        cafe1Panel = new CafeMenuUI(this, "만권화밥");
        cafe2Panel = new CafeMenuUI(this, "버거&타코");
        cafe3Panel = new CafeMenuUI(this, "신머이쌀국수");
        cafe4Panel = new CafeMenuUI(this, "숑숑돈까스");
        cafe5Panel = new CafeMenuUI(this, "위델가");
        mypagePanel = new MyPage(this);
        myorderPanel = new MyOrder(this);
        myreviewPanel = new MyReviewUi(this);
        //myrecommPanel = new RecommandUI(this);
        //writereviewPanel = new WriteReviewUI();
        cardLayout = new CardLayout();
        mainContainerPanel = new JPanel(cardLayout);

        mainContainerPanel.add(loginPanel, "LOGIN");
        mainContainerPanel.add(cafeteriaPanel, "CAFETERIA");
        mainContainerPanel.add(registerPanel, "REGISTER");
        mainContainerPanel.add(cafe1Panel, "CAFE1");  // 만권화밥
        mainContainerPanel.add(cafe2Panel, "CAFE2");  // 신머이쌀국수
        mainContainerPanel.add(cafe3Panel, "CAFE3");  // 쑝쑝돈까스
        mainContainerPanel.add(cafe4Panel, "CAFE4");  // 위델가
        mainContainerPanel.add(cafe5Panel, "CAFE5");  // 버거앤타코
        mainContainerPanel.add(mypagePanel, "MYPAGE");
        mainContainerPanel.add(myorderPanel, "MYORDER");
        mainContainerPanel.add(myreviewPanel, "MYREVIEW");
        //mainContainerPanel.add(myrecommPanel, "RECOMMAND");

        mainFrame.setContentPane(mainContainerPanel);
        mainFrame.setVisible(true);

        // 시작 화면은 로그인 화면
        showScreen("LOGIN");
    }

    public void showScreen(String panelName) {
        cardLayout.show(mainContainerPanel, panelName);
    }
}
