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
	
	static UserManager userManager = UserManager.getInstance();
	
	public void run() {
		userManager.readAll("users.txt");
		userManager.printAll();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainApp app = new MainApp();
		app.run();
	}
}
