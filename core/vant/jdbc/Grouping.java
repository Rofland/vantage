package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import vant.Ensure;
import vant.app.Store;

public class Grouping extends vant.model.Grouping implements Store {
	protected final Connection _conn;
	protected final String _table;
	protected PreparedStatement _insert, _update, _delete;

	public Grouping(Connection c, String table) {
		this._conn = c;
		this._table = table;
	}

	@Override
	protected void create(int k, int v) throws Exception {
		_insert.setInt(1, k);
		_insert.setInt(2, v);
		_insert.executeUpdate();
	}

	@Override
	protected void update(int k, int v) throws Exception {
		_update.setInt(1, v);
		_update.setInt(2, k);
		_update.executeUpdate();
	}

	@Override
	protected void delete(int k) throws Exception {
		_delete.setInt(1, k);
		_delete.executeUpdate();
	}

	@Override
	public void open() throws Exception {
		_insert = _conn.prepareStatement("INSERT INTO " + _table
				+ "(k,v) VALUE(?,?)");
		_update = _conn.prepareStatement("UPDATE " + _table
				+ " SET v=? WHERE k=?");
		_delete = _conn.prepareStatement("DELTE " + _table + " WHERE k=?");
		Statement sql = _conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT MAX(k) FROM " + _table);
		rs.next();
		int max = rs.getInt(1);
		rs.close();
		_groups = new int[max + 64];
		rs = sql.executeQuery("SELECT k,v FROM " + _table + " ORDER BY k");
		while (rs.next()) {
			int k = Ensure.id("k", rs.getInt(1));
			int v = Ensure.range("v", rs.getInt(2), 0, Integer.MAX_VALUE);
			_groups[k - 1] = v;
			_nonZero++;
			if (v > _kCounts.length)
				_kCounts = Arrays.copyOf(_kCounts, v + 32);
			_kCounts[v - 1]++;
		}
		sql.close();
	}

	@Override
	public void setup() throws Exception {
		Statement sql = _conn.createStatement();
		sql.execute("CREATE TABLE " + _table + "(k INT PRIMARY KEY, v INT)");
		sql.close();
	}

	@Override
	public void erase() throws Exception {
		JDBC.drop(_conn, _table);
	}

	@Override
	public void check() throws Exception {
		Statement sql = _conn.createStatement();
		sql.execute("SELECT k, v FROM " + _table + " LIMIT 1");
		sql.close();
	}
}
