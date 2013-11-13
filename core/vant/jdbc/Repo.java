package vant.jdbc;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import vant.Misc;
import vant.Mold;
import vant.Usage;
import vant.lang.BufferWriter;
import vant.lang.StreamReader;
import vant.model.Tuple;

public class Repo<T extends Tuple> extends vant.model.Repo<T> implements
		vant.app.Store {
	protected final Connection _conn;
	protected final String _table;
	protected PreparedStatement _insert, _update;
	protected final ByteBuffer _bin = ByteBuffer.allocate(proto.binaryLimit());
	protected final vant.lang.Writer _writer = new BufferWriter(_bin);
	protected final InputStream _bufferInput = Misc.input(_bin);

	public Repo(Mold<T> m, Connection c, String table) {
		super(m);
		this._conn = c;
		this._table = table;
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
	public void open() throws SQLException, IOException, Usage {
		_insert = _conn.prepareStatement("INSERT INTO " + _table
				+ "(tuple, id) VALUE(?,?)");
		_update = _conn.prepareStatement("UPDATE " + _table
				+ " SET tuple=? WHERE id=?");

		Statement sql = _conn.createStatement();
		ResultSet rs = sql.executeQuery("SELECT COUNT(*) FROM " + _table);
		int count = rs.next() ? rs.getInt(1) : 0;
		if (count == 0)
			return;
		ensureCapacity(count);
		StreamReader reader = new StreamReader(null);
		rs = sql.executeQuery("SELECT id, tuple FROM " + _table);
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
		sql.close();
	}

	@Override
	public void setup() throws SQLException {
		Statement sql = _conn.createStatement();
		sql.execute("CREATE TABLE " + _table
				+ "(id INT PRIMARY KEY, tuple VARBINARY(" + proto.binaryLimit()
				+ "))");
		sql.close();
	}

	@Override
	public void erase() throws SQLException {
		JDBC.drop(_conn, _table);
	}

	@Override
	public void check() throws SQLException {
		Statement sql = _conn.createStatement();
		sql.execute("SELECT id, tuple FROM " + _table + " LIMIT 1");
		sql.close();
	}
}
