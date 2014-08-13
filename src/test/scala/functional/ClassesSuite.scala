package functional

import functional.support.Suite

class ClassesSuite extends Suite {
	test("empty, simple class") {
		"public class Foo {}" should portTo("class Foo {}")
	}

	test("superclass") {
		"public class Foo extends Bar {}" should portTo("class Foo : Bar {}")
	}

	test("superclass and interface") {
		val java = "public class Foo extends Bar implements IBar {}"
		val d = "class Foo : Bar, IBar {}"

		java should portTo(d)
	}

	test("interface") {
		"public class Foo implements Bar {}" should portTo("class Foo : Bar {}")
	}

	test("multiple interfaces") {
		val java = "public class Foo implements Bar, Baz {}"
		val d = "class Foo : Bar, Baz {}"

		java should portTo(d)
	}

	test("abstract modifier") {
		"public abstract class Foo {}" should portTo("abstract class Foo {}")
	}

	test("final modifier") {
		"public final class Foo {}" should portTo("final class Foo {}")
	}

	test("public access modifier") {
		"public class Foo {}" should portTo("class Foo {}")
	}

	test("no modifier") {
		"class Foo {}" should portTo("package class Foo {}")
	}

	test("protected access modifier") {
		"protected class Foo {}" should portTo("protected class Foo {}")
	}

	test("private access modifier") {
		"private class Foo {}" should portTo("private class Foo {}")
	}
}