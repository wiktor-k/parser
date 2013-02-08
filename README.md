Simple expression parser
------------------------

An implementation of very simple expression parser. It parses expression like
this: `3/2+2+5(8-9)*6` into a tree that respects operator precedence:
`(((3 / 2) + 2) + ((5 * (8 - 9)) * 6))`


For a list of supported expressions see `biz.metacode.parser.ParserTest` class.
For a simple usage see `biz.metacode.parser.sample.Main` class.

To run code from Main class use:

    mvn clean exec:java

To run unit tests use:

    mvn test

To see code coverage report use:

    mvn cobertura:cobertura

Report should be in `target/site/cobertura/index.html` file.