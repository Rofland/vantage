package vant.lang.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import vant.lang.ArraySerie;
import vant.lang.MapStruct;
import vant.lang.Serie;
import vant.lang.Struct;
import vant.lang.LangError;

public class Parser {
	public Struct struct(Reader r) throws IOException {
		init(r);
		Struct s = new MapStruct();
		fill(s);
		return s;
	}

	public Serie serie(Reader r) throws IOException {
		init(r);
		Serie s = new ArraySerie();
		fill(s);
		return s;
	}

	private void fill(Struct s) throws IOException {
		char c;
		String key;

		if (nextClean() != '{') {
			throw langError("Object must begin with '{'");
		}
		for (;;) {
			c = nextClean();
			switch (c) {
			case 0:
				throw langError("Object must end with '}'");
			case '}':
				return;
			default:
				back();
				key = nextValue().toString();
			}

			// The key is followed by ':'. We will also tolerate '=' or '=>'.

			c = nextClean();
			if (c == '=') {
				if (next() != '>') {
					back();
				}
			} else if (c != ':') {
				throw langError("Expected a ':' after a key");
			}
			s.put(key, nextValue());

			// Pairs are separated by ','. We will also tolerate ';'.

			switch (nextClean()) {
			case ';':
			case ',':
				if (nextClean() == '}') {
					return;
				}
				back();
				break;
			case '}':
				return;
			default:
				throw langError("Expect ',' or '}'");
			}
		}
	}

	private void fill(Serie s) throws IOException {
		if (nextClean() != '[') {
			throw langError("Array must start with '['");
		}
		if (nextClean() != ']') {
			back();
			for (;;) {
				if (nextClean() == ',') {
					back();
					s.push(null);
				} else {
					back();
					s.push(nextValue());
				}
				switch (nextClean()) {
				case ';':
				case ',':
					if (nextClean() == ']')
						return;
					back();
					break;
				case ']':
					return;
				default:
					throw langError("Expect ',' or ']'");
				}
			}
		}
	}

	

	private long character;
	private boolean eof;
	private long index;
	private long line;
	private char previous;
	private Reader reader;
	private boolean usePrevious;

	private void init(Reader reader) {
		this.reader = reader.markSupported() ? reader : new BufferedReader(
				reader);
		this.eof = false;
		this.usePrevious = false;
		this.previous = 0;
		this.index = 0;
		this.character = 1;
		this.line = 1;
	}

	/**
	 * Back up one character. This provides a sort of lookahead capability, so
	 * that you can test for a digit or letter before attempting to parse the
	 * next number or identifier.
	 */
	private void back() throws IOException {
		if (this.usePrevious || this.index <= 0) {
			throw new IOException("Stepping back two steps is not supported");
		}
		this.index -= 1;
		this.character -= 1;
		this.usePrevious = true;
		this.eof = false;
	}

	private boolean end() {
		return this.eof && !this.usePrevious;
	}

	/**
	 * Get the next character in the source string.
	 * 
	 * @return The next character, or 0 if past the end of the source string.
	 */
	private char next() throws IOException {
		int c;
		if (this.usePrevious) {
			this.usePrevious = false;
			c = this.previous;
		} else {
			try {
				c = this.reader.read();
			} catch (IOException exception) {
				throw new IOException(exception);
			}

			if (c <= 0) { // End of stream
				this.eof = true;
				c = 0;
			}
		}
		this.index += 1;
		if (this.previous == '\r') {
			this.line += 1;
			this.character = c == '\n' ? 0 : 1;
		} else if (c == '\n') {
			this.line += 1;
			this.character = 0;
		} else {
			this.character += 1;
		}
		this.previous = (char) c;
		return this.previous;
	}

	/**
	 * Get the next n characters.
	 * 
	 * @param n
	 *            The number of characters to take.
	 * @return A string of n characters.
	 * @throws IOException
	 *             Substring bounds error if there are not n characters
	 *             remaining in the source string.
	 */
	private String next(int n) throws IOException {
		if (n == 0) {
			return "";
		}

		char[] chars = new char[n];
		int pos = 0;

		while (pos < n) {
			chars[pos] = this.next();
			if (this.end()) {
				throw this.langError("Substring bounds error");
			}
			pos += 1;
		}
		return new String(chars);
	}

	/**
	 * Get the next char in the string, skipping whitespace.
	 * 
	 * @throws IOException
	 * @return A character, or 0 if there are no more characters.
	 */
	private char nextClean() throws IOException {
		for (;;) {
			char c = this.next();
			if (c == 0 || c > ' ') {
				return c;
			}
		}
	}

	/**
	 * Return the characters up to the next close quote character. Backslash
	 * processing is done. The formal JSON format does not allow strings in
	 * single quotes, but an implementation is allowed to accept them.
	 * 
	 * @param quote
	 *            The quoting character, either <code>"</code>
	 *            &nbsp;<small>(double quote)</small> or <code>'</code>
	 *            &nbsp;<small>(single quote)</small>.
	 * @return A String.
	 * @throws IOException
	 *             Unterminated string.
	 */
	private String nextString(char quote) throws IOException {
		char c;
		StringBuffer sb = new StringBuffer();
		for (;;) {
			c = this.next();
			switch (c) {
			case 0:
			case '\n':
			case '\r':
				throw this.langError("Unterminated string");
			case '\\':
				c = this.next();
				switch (c) {
				case 'b':
					sb.append('\b');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 'u':
					sb.append((char) Integer.parseInt(this.next(4), 16));
					break;
				case '"':
				case '\'':
				case '\\':
				case '/':
					sb.append(c);
					break;
				default:
					throw this.langError("Illegal escape.");
				}
				break;
			default:
				if (c == quote) {
					return sb.toString();
				}
				sb.append(c);
			}
		}
	}

	/**
	 * Get the next value. The value can be a Boolean, Double, Integer,
	 * JSONArray, JSONObject, Long, or String, or the JSONObject.NULL object.
	 * 
	 * @throws IOException
	 *             If syntax error.
	 * 
	 * @return An object.
	 */
	private Object nextValue() throws IOException {
		char c = this.nextClean();
		String string;

		switch (c) {
		case '"':
		case '\'':
			return this.nextString(c);
		case '{':
			this.back();
			Struct struct = new MapStruct();
			fill(struct);
			return struct;
		case '[':
			this.back();
			Serie serie = new ArraySerie();
			fill(serie);
			return serie;
		}

		/*
		 * Handle unquoted text. This could be the values true, false, or null,
		 * or it can be a number. An implementation (such as this one) is
		 * allowed to also accept non-standard forms.
		 * 
		 * Accumulate characters until we reach the end of the text or a
		 * formatting character.
		 */

		StringBuffer sb = new StringBuffer();
		while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
			sb.append(c);
			c = this.next();
		}
		this.back();

		string = sb.toString().trim();
		if ("".equals(string)) {
			throw this.langError("Missing value");
		}
		return Json.parsePrimitive(string);
	}

	private LangError langError(String message) {
		return new LangError(Json.LANG, message + "[char " + character
				+ " line " + line + "]");
	}
}
