package biz.metacode.parser;

import javax.annotation.Nonnull;

/**
 * Represents an identifier that can be a direct numeric value or a name of a
 * variable.
 */
final class Identifier extends Expression {

	private final @Nonnull
	String name;

	/**
	 * Creates new identifier with given name.
	 *
	 * @param name
	 *            Identifier name
	 */
	public Identifier(@Nonnull String name) {
		this.name = name;
	}

	/**
	 * Returns this identifier name.
	 *
	 * @return Identifier name.
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of this modifier.
	 */
	@Override
	public String toString() {
		return this.getName();
	}
}
