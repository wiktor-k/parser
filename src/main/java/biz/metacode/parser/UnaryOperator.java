package biz.metacode.parser;

import javax.annotation.Nonnull;

/**
 * Represents an operator with only one operand.
 */
final class UnaryOperator extends Expression {

	@Nonnull
	private final Expression operand;

	@Nonnull
	private final String operator;

	/**
	 * Creates unary operator for given expression.
	 *
	 * @param operand
	 *            Operand.
	 * @param operator
	 *            Operator.
	 */
	public UnaryOperator(@Nonnull String operator, @Nonnull Expression operand) {
		this.operator = operator;
		this.operand = operand;
	}

	/**
	 * Returns operand.
	 *
	 * @return Operand.
	 */
	@Nonnull
	public Expression getOperand() {
		return operand;
	}

	/**
	 * Returns operator.
	 *
	 * @return Operator.
	 */
	@Nonnull
	public String getOperator() {
		return operator;
	}

	/**
	 * Returns this operator in text form.
	 */
	@Override
	public String toString() {
		return "(" + this.getOperator() + " " + this.getOperand() + ")";
	}
}
