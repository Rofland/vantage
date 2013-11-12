package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Arrays;

import vant.Ensure;
import vant.app.Persisted;

public class Grouping extends vant.model.Grouping implements Persisted<JDBC> {
	protected final JDBC _conf = new JDBC();
	protected PreparedStatement _insert, _update, _delete;

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
	public JDBC conf() {
		return _conf;
	}

	@Override
	public void open() throws Exception {
		Connection conn = _conf.connect();
		_insert = conn.prepareStatement("INSERT INTO " + _conf.table
				+ "(k,v) VALUE(?,?)");
		_update = conn.prepareStatement("UPDATE " + _conf.table
				+ " SET v=? WHERE k=?");
		_delete = conn.prepareStatement("DELTE " + _conf.table + " WHERE k=?");
		Statement sql = conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT MAX(k) FROM " + _conf.table);
		rs.next();
		int max = rs.getInt(1);
		rs.close();
		_groups = new int[max + 64];
		rs = sql.executeQuery("SELECT k,v FROM " + _conf.table + " ORDER BY k");
		while (rs.next()) {
			int k = Ensure.id("k", rs.getInt(1));
			int v = Ensure.range("v", rs.getInt(2), 0, Integer.MAX_VALUE);
			_groups[k - 1] = v;
			_nonZero++;
			if (v > _kCounts.length)
				_kCounts = Arrays.copyOf(_kCounts, v + 32);
			_kCounts[v - 1]++;
		}
	}

	@Override
	public void setup() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.execute("CREATE TABLE " + _conf.table
				+ "(k INT PRIMARY KEY, v INT)");
		conn.close();
	}

	@Override
	public void erase() throws Exception {
		_conf.erase();
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
			sql.execute("SELECT k, v FROM " + _conf.table + " LIMIT 1");
		} catch (SQLSyntaxErrorException e) {
			conn.close();
			return State.INVALID;
		}

		conn.close();
		return State.OK;
	}

}
