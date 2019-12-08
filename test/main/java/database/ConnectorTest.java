package main.java.database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class ConnectorTest {

	@Test
	void EstablishConnectionTest() {
		Connector.establishConnection();		
		Connection conn;
		try {
			conn = Connector.getConnection();
			assertNotNull(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}
