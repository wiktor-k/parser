package biz.metacode.parser;

import org.junit.Assert;
import org.junit.Test;

public final class ParserTest {

	@Test
	public void simpleOperatorPrecedence() {
		assertExpr("VAR-50*2+VAR").parsesTo("((VAR - (50 * 2)) + VAR)");
	}

	@Test
	public void parenPrecedence() {
		assertExpr("((VAR-50)*2)+VAR").parsesTo("(((VAR - 50) * 2) + VAR)");
	}

	@Test
	public void parenAndOperatorPrecedence() {
		assertExpr("(VAR-50)*2+VAR").parsesTo("(((VAR - 50) * 2) + VAR)");
	}

	@Test
	public void operatorPrecedence() {
		assertExpr("VAR+2*VAR-50").parsesTo("((VAR + (2 * VAR)) - 50)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void missingParen() {
		Parser.parse("VAR+(2*VAR-50");
	}

	@Test(expected = IllegalArgumentException.class)
	public void missingUnderscore() {
		Parser.parse("-4VLO EXT");
	}

	@Test(expected = IllegalArgumentException.class)
	public void missingOperator() {
		Parser.parse("VAR+(2 4)-50");
	}

	@Test(expected = IllegalArgumentException.class)
	public void missingOperatorNumber() {
		Parser.parse("VAR+(VAR 4)-50");
	}

	@Test
	public void easyMultiplication() {
		assertExpr("VAR+(4 VAR)-50").parsesTo("((VAR + (4 * VAR)) - 50)");
	}

	@Test
	public void easyMultiplicationOfParens() {
		assertExpr("VAR+2(4 VAR)").parsesTo("(VAR + (2 * (4 * VAR)))");
	}

	@Test
	public void negation() {
		assertExpr("-VAR+2").parsesTo("((- VAR) + 2)");
	}

	@Test
	public void negationAndSubstraction() {
		assertExpr("-VAR+2*VAR-50").parsesTo("(((- VAR) + (2 * VAR)) - 50)");
	}

	@Test
	public void negationAndMultiplication() {
		assertExpr("-VAR-2*-VAR-50").parsesTo(
				"(((- VAR) - (2 * (- VAR))) - 50)");
	}

	@Test
	public void easyMultiplicationAndAddition() {
		assertExpr("12VAR+VAR").parsesTo("((12 * VAR) + VAR)");
	}

	@Test
	public void simpleNegationWithEasyMultiplication() {
		assertExpr("-4VAR").parsesTo("(- (4 * VAR))");
	}

	@Test
	public void negationWithParens() {
		assertExpr("-(4+VAR)").parsesTo("(- (4 + VAR))");
	}

	@Test
	public void easyMultiplicationAndNumberInName() {
		assertExpr("-4V1").parsesTo("(- (4 * V1))");
	}

	@Test
	public void floatingPointNumber() {
		assertExpr("-4.4VAR1A").parsesTo("(- (4.4 * VAR1A))");
	}

	@Test
	public void floatingPointNumberAndUnderscore() {
		assertExpr("-4.4 / VAR_2").parsesTo("((- 4.4) / VAR_2)");
	}

	@Test(expected = IllegalArgumentException.class)
	public void brokenMultiplication() {
		Parser.parse("2*");
	}

	private ExprAssertion assertExpr(String expression) {
		return new ExprAssertion(expression);
	}

	private static class ExprAssertion {
		private final String expression;

		public ExprAssertion(String expression) {
			this.expression = expression;
		}

		public void parsesTo(String canonical) {
			String parsed = Parser.parse(expression).toString();
			Assert.assertEquals(canonical, parsed);
		}
	}
}
