package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import vant.app.Persisted;

public class Selection extends vant.model.Selection implements Persisted<JDBC> {
	protected final JDBC _conf = new JDBC();
	protected PreparedStatement _insert, _delete, _deleteAll;

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
	public JDBC conf() {
		return _conf;
	}

	@Override
	public void open() throws Exception {
		Connection conn = _conf.connect();
		_insert = conn.prepareStatement("INSERT INTO " + _conf.table
				+ "VALUE(?)");
		_delete = conn.prepareStatement("DELETE FROM " + _conf.table
				+ " WHRE id=?");
		_deleteAll = conn.prepareStatement("DELETE FROM " + _conf.table);

		Statement sql = conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM " + _conf.table);
		int n = rs.next() ? rs.getInt(1) : 0;
		rs.close();
		if (n == 0)
			return;
		_ids = new int[n + 20];
		rs = sql.executeQuery("SELECT id FROM " + _conf.table);
		while (rs.next())
			_ids[_count++] = rs.getInt(1);
		rs.close();
		sql.close();
	}

	@Override
	public void setup() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.executeUpdate("CREATE TABLE " + _conf.table + "(id INT)");
		conn.close();
	}

	@Override
	public void erase() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.execute("DROP TABLE IF EXISTS " + _conf.table);
		conn.close();
	}

	@Override
	public Persisted.State check() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		try {
			sql.execute("SELECT * FROM " + _conf.table + " LIMIT 1");
		} catch (SQLSyntaxErrorException e) {
			conn.close();
			return State.NOT_EXIST;
		}
		try {
			sql.execute("SELECT id FROM " + _conf.table + " LIMIT 1");
		} catch (SQLSyntaxErrorException e) {
			conn.clearWarnings();
			return State.INVALID;
		}
		conn.close();
		return State.OK;
	}
}
