package vant.jdbc;

import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;

import vant.Misc;
import vant.Mold;
import vant.app.Persisted;
import vant.lang.BufferWriter;
import vant.lang.StreamReader;
import vant.model.Repo;
import vant.model.Tuple;

public class RepoJDBC<T extends Tuple> extends Repo<T> implements
		Persisted<JDBC> {
	protected final JDBC _conf = new JDBC();
	protected PreparedStatement _insert, _update;
	protected final ByteBuffer _bin = ByteBuffer.allocate(proto.binaryLimit());
	protected final vant.lang.Writer _writer = new BufferWriter(_bin);
	protected final InputStream _bufferInput = Misc.input(_bin);

	public RepoJDBC(Mold<T> m) {
		super(m);
	}

	protected void _save(int id, T tuple) throws Exception {
		_bin.clear();
		tuple.encode(_writer);
		_bin.flip();

		PreparedStatement ps = id <= _count ? _update : _insert;
		ps.setBinaryStream(1, _bufferInput);
		ps.setInt(2, id);
		ps.executeUpdate();
	}

	@Override
	public JDBC conf() {
		return _conf;
	}

	@Override
	public void open() throws Exception {
		Connection conn = _conf.connect();
		_insert = conn.prepareStatement("INSERT INTO " + _conf.table
				+ "(tuple, id) VALUE(?,?)");
		_update = conn.prepareStatement("UPDATE " + _conf.table
				+ " SET tuple=? WHERE id=?");

		Statement sql = conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM " + _conf.table);
		int count = rs.next() ? rs.getInt(1) : 0;
		if (count == 0)
			return;
		ensureCapacity(count);
		StreamReader reader = new StreamReader(null);
		rs = sql.executeQuery("SELECT id, tuple FROM " + _conf.table);
		for (int i = 0; i < count; i++) {
			rs.next();
			int id = rs.getInt(1);
			DataInputStream in = new DataInputStream(rs.getBinaryStream(2));
			reader.in = in;
			T tuple = mold.create();
			tuple.decode(reader);
			in.close();
			_tuples[id - 1] = tuple;
		}
		_count = count;
	}

	@Override
	public void setup() throws Exception {
		Connection conn = _conf.connect();
		Statement sql = conn.createStatement();
		sql.execute("CREATE TABLE " + _conf.table
				+ "(id INT PRIMARY KEY, tuple VARBINARY(" + proto.binaryLimit()
				+ "))");
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
			sql.execute("SELECT id, tuple FROM " + _conf.table + " LIMIT 1");
		} catch (SQLSyntaxErrorException e) {
			conn.close();
			return State.INVALID;
		}

		conn.close();
		return State.OK;
	}
}
