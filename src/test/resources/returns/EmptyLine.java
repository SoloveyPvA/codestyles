package ru.pva.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class EmptyLine extends AbstractCheck {

	public void returnVoid() {
		return;
	}

	public void notLiteralReturn() {
		System.out.println("notLiteralReturn");
	}

	public int lineNumberInMethodLessThenThree() {
		System.out.println("lineNumberInMethodLessThenThree");
		return 1;
	}

	public int errorWhenNotEmptyLineBeforeReturn() {
		System.out.println("errorWhenNotEmptyLineBeforeReturn");
		System.out.println("errorWhenNotEmptyLineBeforeReturn");
		return 1;
	}
}
