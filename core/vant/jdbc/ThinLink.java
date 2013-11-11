package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import vant.Silently;
import vant.app.Persisted;

public class ThinLink extends vant.model.ThinLink implements Persisted<JDBC> {
	protected final JDBC _conf = new JDBC();
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

	@Override
	public JDBC conf() {
		return _conf;
	}

	@Override
	public void open() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM " + _conf.table);
		_count = rs.next() ? rs.getInt(1) : 0;
		if (_count == 0)
			return;
		rs.close();
		_pairs = new long[_count & 0xfffffe00 + 512];

		rs = sql.executeQuery("SELECT MAX(src) FROM " + _conf.table);
		rs.next();
		_sizes = new int[rs.getInt(1) & 0xffffff00 + 256];
		rs.close();

		rs = sql.executeQuery("SELECT src, dst FROM " + _conf.table);
		for (int i = 0; i < _count; i++) {
			rs.next();
			int src = rs.getInt(1);
			int dst = rs.getInt(2);
			_pairs[i] = (((long) src) << 32) | dst;
			_sizes[src - 1]++;
		}
		rs.close();
		sql.close();

		_join = conn.prepareStatement("INSERT INTO " + _conf.table
				+ "(src,dst) VALUE(?,?)");
		_chop = conn.prepareStatement("DELETE FROM " + _conf.table
				+ " WHERE src=? AND dst=?");
	}

	@Override
	public void setup() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.execute("CREATE TABLE " + _conf.table
				+ "(src INT, dst INT, PRIMARY KEY(src, dst)");
		Silently.close(conn);
	}

	@Override
	public void erase() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.execute("DROP TABLE " + _conf.table);
		Silently.close(conn);
	}

	@Override
	public State check() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();

		try {
			sql.execute("SELECT * FROM " + _conf.table + " LIMIT 1");
		} catch (SQLSyntaxErrorException e) {
			conn.close();
			return State.NOT_EXIST;
		}

		try {
			sql.execute("SELECT src, dst FROM " + _conf.table + " LIMIT 1");
		} catch (SQLSyntaxErrorException e) {
			conn.close();
			return State.INVALID;
		}

		conn.close();
		return State.OK;
	}
}
