package vant.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import vant.Ensure;
import vant.Usage;
import vant.lang.Exchange;
import vant.model.Tuple;

public class JDBC extends Tuple {
	public String url;
	public String user;
	public String pswd;

	public Connection alloc() throws SQLException {
		return DriverManager.getConnection(url, user, pswd);
	}

	@Override
	public void validate() throws Usage {
	}

	@Override
	public int binaryLimit() {
		return 1024;

	}

	@Override
	public void decode(Exchange r) throws IOException, Usage {
		url = Ensure.notNull("url", r.STRING("url", null));
		user = r.STRING("user", System.getProperty("user.name"));
		pswd = r.STRING("password", "");
	}

	@Override
	public void encode(Exchange w) throws IOException {
		w.STRING("url", url);
		w.STRING("user", user);
		w.STRING("password", pswd);
	}

	public static void drop(Connection c, String table) throws SQLException {
		Statement s = c.createStatement();
		s.executeQuery("DROP TABLE IF EXISTS " + table);
		s.close();
	}
}
