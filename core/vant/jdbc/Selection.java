package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import vant.app.Store;

public class Selection extends vant.model.Selection implements Store {
	protected final Connection _conn;
	protected final String _table;
	protected PreparedStatement _insert, _delete, _deleteAll;

	public Selection(Connection c, String table) {
		this._conn = c;
		this._table = table;
	}

	protected void _add(int id) throws Exception {
		_insert.setInt(1, id);
		_insert.execute();
	}

	protected void _evict(int id) throws Exception {
		_delete.setInt(1, id);
		_delete.execute();
	}

	protected void _clear() throws Exception {
		_deleteAll.execute();
	}

	@Override
	public void open() throws Exception {
		_insert = _conn.prepareStatement("INSERT INTO " + _table + "VALUE(?)");
		_delete = _conn
				.prepareStatement("DELETE FROM " + _table + " WHRE id=?");
		_deleteAll = _conn.prepareStatement("DELETE FROM " + _table);

		Statement sql = _conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM " + _table);
		int n = rs.next() ? rs.getInt(1) : 0;
		rs.close();
		if (n == 0)
			return;
		_ids = new int[n + 20];
		rs = sql.executeQuery("SELECT id FROM " + _table);
		while (rs.next())
			_ids[_count++] = rs.getInt(1);
		rs.close();
		sql.close();
	}

	@Override
	public void setup() throws Exception {
		Statement sql = _conn.createStatement();
		sql.executeUpdate("CREATE TABLE " + _table + "(id INT PRIMARY KEY)");
		sql.close();
	}

	@Override
	public void erase() throws Exception {
		JDBC.drop(_conn, _table);
	}

	@Override
	public void check() throws Exception {
		Statement sql = _conn.createStatement();
		sql.execute("SELECT id FROM " + _table + " LIMIT 1");
		sql.close();
	}
}
