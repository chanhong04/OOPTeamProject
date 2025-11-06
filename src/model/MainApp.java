package model;

public class MainApp {
	private static MainApp app = null;
	
	private MainApp() {
		
	}
	
	public static MainApp getInstance() {
		if (app == null)
			app = new MainApp();
		return app;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void run() {
		
	}
}
