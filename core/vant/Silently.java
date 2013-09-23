package vant;

import java.io.Closeable;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Statement;

public class Silently {
	public static <T> T create(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static <T> T[] create(Class<T> clazz, int n) {
		@SuppressWarnings("unchecked")
		T[] array = (T[])Array.newInstance(clazz, n);
		for (int i = 0; i < n; i++)
			array[i] = Silently.create(clazz);
		return array;
	}
	
	public static <T> void create(Class<T> clazz, T[] array, int offset, int n) {
		int end = offset + n;
		for (int i = offset; i < end; i++)
			array[i] = Silently.create(clazz);
	}

	public static void close(Closeable... array) {
		for (Closeable c : array) {
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(Statement... array) {
		for (Statement s : array) {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(Connection... array) {
		for (Connection c : array) {
			try {
				c.close();
			} catch (Exception e) {
			}
		}
	}
}
