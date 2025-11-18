package model;

import mgr.Factory;

public class MainApp {
	private static MainApp app = null;
	
	private MainApp() {
		
	}
	
	public static MainApp getInstance() {
		if (app == null)
			app = new MainApp();
		return app;
	}
	
	static CafeteriaManager cafeteriaManager = CafeteriaManager.getInstance();
	static UserManager userManager = UserManager.getInstance();
	static ReviewManager reviewMgr =  ReviewManager.getInstance();
	
	public void run() {
		
		userManager.readAll("users.txt", new Factory<User>() {
			public User create() {
				return new User();
			}
		});
		userManager.printAll();
		reviewMgr.readReviews("reviews.txt");
		reviewMgr.displayReviews();
		cafeteriaManager.readAll("cafeterias.txt", new Factory<Cafeteria>() {
			public Cafeteria create() { return new Cafeteria(); }
		});
		cafeteriaManager.printAll();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainApp app = new MainApp();
		app.run();
	}
}
