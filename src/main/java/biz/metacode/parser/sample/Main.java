package biz.metacode.parser.sample;

import biz.metacode.parser.Expression;
import biz.metacode.parser.Parser;

public final class Main {

	public static void main(String... args) {
		Expression expr = Parser.parse("3/2+2+5(8-9)*-6");
		System.out.println(expr);
	}

}
