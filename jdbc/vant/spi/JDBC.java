package vant.spi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

public class JDBC extends Conf {
	public String table;
	public String user;
	public String pswd;

	@Override
	public void parse(JSONObject json) throws JSONException {
		url = json.getString("url");
		table = json.getString("table");
		user = json.optString("user");
		pswd = json.optString("pswd");
	}

	public Connection connect() throws SQLException {
		return DriverManager.getConnection(url, user, pswd);
	}
}