package main.java.action.fetching;

import main.java.database.Connector;

public class FetchingTestApp {

	public static void main(String[] args) {
		Connector.establishConnection();
		FetchingController.selectAll();
		
	}

}
