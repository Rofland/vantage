package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import vant.app.Store;

public class ThinLink extends vant.model.ThinLink implements Store {
	protected final Connection _conn;
	protected final String _table;
	protected PreparedStatement _join, _chop;

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

	public ThinLink(Connection c, String table) {
		this._conn = c;
		this._table = table;
	}

	@Override
	public void open() throws Exception {
		Statement sql = _conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM " + _table);
		_count = rs.next() ? rs.getInt(1) : 0;
		if (_count == 0)
			return;
		rs.close();
		_pairs = new long[_count & 0xfffffe00 + 512];

		rs = sql.executeQuery("SELECT MAX(src) FROM " + _table);
		rs.next();
		_sizes = new int[rs.getInt(1) & 0xffffff00 + 256];
		rs.close();

		rs = sql.executeQuery("SELECT src, dst FROM " + _table);
		for (int i = 0; i < _count; i++) {
			rs.next();
			int src = rs.getInt(1);
			int dst = rs.getInt(2);
			_pairs[i] = (((long) src) << 32) | dst;
			_sizes[src - 1]++;
		}
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
