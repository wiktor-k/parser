package biz.metacode.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Parses given text into a tree expression.
 */
public final class Parser {

	/**
	 * Parses given text into tree expression. May throw
	 * {@link IllegalArgumentException} if text contains expression that cannot
	 * be parsed.
	 *
	 * @param text
	 *            Text to be parsed.
	 */
	public static Expression parse(String text) {
		return parse(Lexer.lex(text));
	}

	private static Expression parse(@Nonnull Iterator<String> tokens) {
		return new Parser(tokens).getTopLevelExpression();
	}

	/**
	 * Map of valid operators with their precedences (higher number - higher
	 * precedence and binds more tightly). This map should return {@code null}
	 * for keys that are not operators.
	 */
	private static final Map<String, Integer> validOperators = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1433924067475794534L;
		{
			put("+", 20);
			put("-", 20);
			put("/", 40);
			put("*", 40);
		}
	};

	private final Iterator<String> tokens;

	private String currentToken;

	private Parser(@Nonnull final Iterator<String> tokens) {
		this.tokens = tokens;
		this.nextToken();
	}

	@Nonnull
	private Expression getTopLevelExpression() {
		Expression expression = this.nextExpression();
		// if we still have tokens to be parsed there is probably
		// operator missing or a missing opening parenthesis
		// example expression: 2 4
		if (this.currentToken != null) {
			throw new IllegalArgumentException("Missing operator.");
		}
		return expression;
	}

	@Nonnull
	private Expression nextPrimary() {
		String token = this.currentToken;
		if (token == null) {
			throw new IllegalArgumentException("Expression ended unexpectedly.");
		}
		if ("(".equals(token)) {
			return this.nextParens();
		} else if ("-".equals(token)) {
			return this.nextUnaryOperator();
		} else {
			return this.nextIdentifierExpression();
		}
	}

	@Nonnull
	private Expression nextIdentifierExpression() {
		Identifier identifier = this.nextIdentifier();
		// check if next token is also identifier then this is a
		// multiplication of a number and an identifier
		String token = this.currentToken;
		if (token != null && isIdentifier(token)) {
			if (isNumber(identifier.getName()) && !isNumber(token)) {
				Identifier rightIdentifier = new Identifier(token);
				this.nextToken();
				return new BinaryOperator(identifier, "*", rightIdentifier);
			} else {
				throw new IllegalArgumentException("Missing operator between "
						+ identifier + " and " + token);
			}
		}
		// check if next token is an opening parenthesis, this way we can
		// support 2(VAR+5) expressions
		if ("(".equals(token)) {
			Expression parenthesis = this.nextParens();
			return new BinaryOperator(identifier, "*", parenthesis);
		}
		return identifier;
	}

	@Nonnull
	private Identifier nextIdentifier() {
		String name = this.currentToken;
		if (name == null) {
			throw new IllegalArgumentException("Identifier name is empty.");
		}
		Identifier identifier = new Identifier(name);
		this.nextToken();
		return identifier;
	}

	@Nonnull
	private Expression nextExpression() {
		Expression leftOperand = this.nextPrimary();
		return this.nextBinaryOperator(0, leftOperand);
	}

	@Nonnull
	private Expression nextBinaryOperator(int exprPrecedence,
			@Nonnull Expression leftOperand) {
		while (true) {

			Integer tokenPrecedence = validOperators.get(this.currentToken);

			// no more tokens or current operator binds less tightly (like +)
			// than the entire expression. This breaks recursion inside
			// rightOperand = this.nextBinary...
			if (tokenPrecedence == null || tokenPrecedence < exprPrecedence) {
				return leftOperand;
			}

			// okay this is an operator
			String operator = this.currentToken;

			if (operator == null) {
				throw new IllegalArgumentException("Operator name is empty.");
			}

			this.nextToken();

			// read right side of the expression
			Expression rightOperand = this.nextPrimary();

			// read next operator (the second one)
			Integer nextTokenPrecedence = validOperators.get(this.currentToken);

			// right operator binds tightly (like *) than left (like +)
			if (nextTokenPrecedence != null
					&& tokenPrecedence < nextTokenPrecedence) {
				rightOperand = this.nextBinaryOperator(tokenPrecedence + 1,
						rightOperand);
			}

			// connect light bound operands and repeat the process
			leftOperand = new BinaryOperator(leftOperand, operator,
					rightOperand);
		}
	}

	private void nextToken() {
		this.currentToken = tokens.hasNext() ? tokens.next() : null;
		// ignore whitespaces
		while (this.currentToken != null && "".equals(this.currentToken.trim())) {
			this.nextToken();
		}
	}

	@Nonnull
	private Expression nextUnaryOperator() {
		String operator = this.currentToken;
		if (operator == null) {
			throw new IllegalArgumentException("Operator name is empty.");
		}
		this.nextToken();
		Expression operand = this.nextPrimary();
		return new UnaryOperator(operator, operand);
	}

	@Nonnull
	private Expression nextParens() {
		this.nextToken(); // eat (
		Expression expression = this.nextExpression();
		if (!")".equals(this.currentToken)) {
			throw new IllegalArgumentException(") is missing.");
		}
		this.nextToken(); // eat )
		return expression;
	}

	private static boolean isIdentifier(String id) {
		if (id == null || "".equals(id)) {
			return false;
		}
		return Character.isDigit(id.charAt(0))
				|| Character.isLetter(id.charAt(0));
	}

	private static boolean isNumber(String id) {
		if (id == null || "".equals(id)) {
			return false;
		}
		return Character.isDigit(id.charAt(0));
	}

}
