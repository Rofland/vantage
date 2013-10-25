package vant.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import vant.Silently;
import vant.app.Persisted;
import vant.model.ThickLink;

public class ThickLinkJDBC extends ThickLink implements Persisted<JDBC> {
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
		ResultSet rs = sql.executeQuery("SELECT MAX(src) FROM " + _conf.table);
		rs.next();
		_sizes = new int[rs.getInt(1) & 0xffffffc0 + 64];
		rs.close();

		rs = sql.executeQuery("SELECT * FROM " + _conf.table);
		while (rs.next())
			join(rs.getInt("src"), rs.getInt("dst"));
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
		conn.close();
	}

	@Override
	public void erase() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.execute("DROP TABLE " + _conf.table);
		conn.close();
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
