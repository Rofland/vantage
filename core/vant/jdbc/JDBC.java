package vant.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import vant.Norm;
import vant.app.Conf;
import vant.lang.Struct;

public class JDBC extends Conf {
	public String table;
	public String user;
	public String pswd;

	@Override
	public void extract(Struct s) {
		url = Norm.STRING(s.value("url"), "localhost");
		table = Norm.STRING(s.value("table"));
		user = Norm.STRING(s.value("user"));
		pswd = Norm.STRING(s.value("pswd"));
	}

	public Connection connect() throws SQLException {
		return DriverManager.getConnection(url, user, pswd);
	}
}