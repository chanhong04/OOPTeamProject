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
	static ReviewManager reviewMgr =  ReviewManager.getInstance();
	public void run() {
		userManager.readAll("users.txt");
		userManager.printAll();
		reviewMgr.readReviews("reviews.txt");
		reviewMgr.displayReviews();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainApp app = new MainApp();
		app.run();
	}
}
