package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.MainApp;
/*
 * 로그인
 */
public class GUIMain {
	private static GUIMain main = null;
	private GUIMain() {}
	public static GUIMain getInstance() {
		if (main == null) 	
			main = new GUIMain();
		return main;
	}
	
	static MainApp app = MainApp.getInstance();
	
	public static void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIMain.getInstance().createAndShowGUI();
			}
		});
	}
	
	static JFrame mainFrame = new JFrame("Test");
	private void createAndShowGUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane jtab = new JTabbedPane();
        

        jtab.add("test1", itemPane);
        jtab.add("test2", orderPane);
        mainFrame.getContentPane().add(jtab);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    private JPanel itemPane;
    private JPanel orderPane;
    
    public static void main (String args[]) {
    	app.run();
    	startGUI();
    }
}
