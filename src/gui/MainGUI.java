package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;

import mgr.MainApp;

import java.awt.*;
import java.awt.event.*;

public class MainGUI {
	private static MainGUI main = null;
	public static MainGUI getInstance() {
		if (main == null)
			main = new MainGUI();
		return main;
	}
	static  MainApp app = MainApp.getInstance();
	
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
    
	static JFrame mainFrame = new JFrame("학식 추천 앱이름");
	
    public void createAndShowGUI(){
    	mainFrame.setSize(1600,900);
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mainFrame.setLocationRelativeTo(null);
    	
    	loginPanel = new LoginUI(this); 
    	cafeteriaPanel = new CafeteriaUI(this);
    	registerPanel = new RegisterUI(this);
    	
    	cardLayout = new CardLayout();
    	mainContainerPanel = new JPanel(cardLayout);
    	mainContainerPanel.add(loginPanel, "LOGIN");
    	mainContainerPanel.add(cafeteriaPanel, "CAFETERIA");
    	mainContainerPanel.add(registerPanel, "REGISTER");
    	
        mainFrame.add(mainContainerPanel);
        mainFrame.setVisible(true);
    }
    
    public void showScreen(String panelName) {
        cardLayout.show(mainContainerPanel, panelName);
    }
}

