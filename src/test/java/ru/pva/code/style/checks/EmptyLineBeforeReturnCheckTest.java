package ru.pva.code.style.checks;

import static ru.pva.code.style.checks.assertions.AssertCheck.assertThatCheckFor;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import org.testng.annotations.Test;

@Test
public class EmptyLineBeforeReturnCheckTest {

	@Test
	public void checkFile() throws CheckstyleException {
		assertThatCheckFor("src/test/resources/returns/return_check.xml", "src/test/resources/returns/EmptyLine.java")
				.hasErrorsCount(1)
				.hasError()
				.inFileWithName("EmptyLine.java")
				.inLine(24)
				.inColumn(9)
				.forModule("ru.pva.code.style.checks.EmptyLineBeforeReturnCheck")
				.withErrorMessage("Empty string must be before return");
	}
}