package biz.metacode.parser;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * Converts an input text into an enumeration of tokens.
 */
final class Lexer {

	private Lexer() {
	}

	/**
	 * Return an iterator that enumerates tokens in given text.
	 *
	 * @param text
	 *            Text to be lexed.
	 * @return A sequence of tokens.
	 */
	@Nonnull
	public static Iterator<String> lex(String text) {
		return new LexerIterator(text);
	}

	private static final class LexerIterator implements Iterator<String> {

		private static final String REAL = "[0-9]+\\.[0-9]+";
		private static final String INTEGER = "[0-9]+";
		private static final String IDENTIFIER = "[A-Za-z_][A-Za-z0-9_]*";
		private static final String OPERATOR = "[()+-/*]";
		private static final String WHITESPACE = "\\s+";

		private static final Pattern pattern = Pattern.compile(REAL + "|"
				+ INTEGER + "|" + IDENTIFIER + "|" + OPERATOR + "|"
				+ WHITESPACE);

		private final Matcher matcher;
		private boolean hasNext;

		public LexerIterator(String text) {
			matcher = pattern.matcher(text);
			hasNext = matcher.find();
		}

		public boolean hasNext() {
			return hasNext;
		}

		@Nonnull
		public String next() {
			String group = matcher.group();
			if (group == null) {
				throw new IllegalStateException(
						"Called next when hasNext is false");
			}
			hasNext = matcher.find();
			return group;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
