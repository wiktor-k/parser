package biz.metacode.parser;

import javax.annotation.Nonnull;

/**
 * Operator with two operands.
 */
final class BinaryOperator extends Expression {
	private final @Nonnull Expression leftOperand;
	private final @Nonnull String operator;
	private final @Nonnull Expression rightOperand;

	/**
	 * Creates a new binary operator with two operands.
	 *
	 * @param leftOperand
	 *            Left operand of this operation.
	 * @param operator
	 *            Operator that joins two operands.
	 * @param rightOperand
	 *            Right operand of this operation.
	 */
	public BinaryOperator(@Nonnull Expression leftOperand, @Nonnull String operator,
			@Nonnull Expression rightOperand) {
		this.leftOperand = leftOperand;
		this.operator = operator;
		this.rightOperand = rightOperand;
	}

	/**
	 * Returns left operand.
	 *
	 * @return Left operand.
	 */
	@Nonnull
	public Expression getLeftOperand() {
		return leftOperand;
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
	 * Returns right operator.
	 *
	 * @return Right operator.
	 */
	@Nonnull
	public Expression getRightOperand() {
		return rightOperand;
	}

	/**
	 * Returns this binary operator in text form.
	 */
	@Override
	public String toString() {
		return "(" + this.getLeftOperand() + " " + this.getOperator() + " "
				+ this.getRightOperand() + ")";
	}
}
