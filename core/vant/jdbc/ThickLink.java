package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import vant.app.Store;

public class ThickLink extends vant.model.ThickLink implements Store {
	protected final Connection _conn;
	protected final String _table;
	protected PreparedStatement _join, _chop;

	public ThickLink(Connection c, String table) {
		this._conn = c;
		this._table = table;
	}

	protected void save(int src, int dst) throws Exception {
		_join.setInt(1, src);
		_join.setInt(2, dst);
		_join.execute();
	}

	protected void delete(int src, int dst) throws Exception {
		_chop.setInt(1, src);
		_chop.setInt(2, dst);
		_chop.execute();
	}

	@Override
	public void open() throws Exception {
		Statement sql = _conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT MAX(src) FROM " + _table);
		rs.next();
		_sizes = new int[rs.getInt(1) & 0xffffffc0 + 64];
		rs.close();

		rs = sql.executeQuery("SELECT * FROM " + _table);
		while (rs.next())
			put(rs.getInt("src"), rs.getInt("dst"));
		rs.close();
		sql.close();

		_join = _conn.prepareStatement("INSERT INTO " + _table
				+ "(src,dst) VALUE(?,?)");
		_chop = _conn.prepareStatement("DELETE FROM " + _table
				+ " WHERE src=? AND dst=?");
	}

	@Override
	public void setup() throws Exception {
		Statement sql = _conn.createStatement();
		sql.execute("CREATE TABLE " + _table
				+ "(src INT, dst INT, PRIMARY KEY(src, dst)");
		sql.close();
	}

	@Override
	public void erase() throws Exception {
		JDBC.drop(_conn, _table);
	}

	@Override
	public void check() throws Exception {
		Statement sql = _conn.createStatement();
		sql.execute("SELECT src, dst FROM " + _table + " LIMIT 1");
		sql.close();
	}
}
