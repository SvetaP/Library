package librarydao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class Connect {
	public static Connection connectionDb() throws ClassNotFoundException, SQLException{
		String url = "jdbc:oracle:thin:@localhost";
		Locale.setDefault(Locale.ENGLISH);
		Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection(url, "EXAMPLE", "oracle");
		/*DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection con = DriverManager.getConnection(url, "EXAMPLE", "oracle");*/
		return con;
	}
}
