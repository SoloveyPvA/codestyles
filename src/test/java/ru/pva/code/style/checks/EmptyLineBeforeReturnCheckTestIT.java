package ru.pva.code.style.checks;

import static ru.pva.code.style.checks.assertions.AssertCheck.assertThatCheckFor;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import org.testng.annotations.Test;

@Test
public class EmptyLineBeforeReturnCheckTestIT {

	@Test
	public void notLoggingErrorWhenEmptyLineNotRequired() throws CheckstyleException {
		assertThatCheckFor(
				"src/test/resources/returns/return_check.xml", 
				"src/test/resources/returns/EmptyLineNotRequiredBeforeReturn.java"
		)
				.hasErrorsCount(0);
	}

	@Test
	public void logErrorWhenEmptyLineNotExistBeforeReturnInMethod() throws CheckstyleException {
		assertThatCheckFor("src/test/resources/returns/return_check.xml", "src/test/resources/returns/NonEmptyLineInMethodDef.java")
				.hasErrorsCount(1)

				.hasError()
				.inFileWithName("NonEmptyLineInMethodDef.java")
				.inLine(11)
				.inColumn(9)
				.forModule("ru.pva.code.style.checks.EmptyLineBeforeReturnCheck")
				.withErrorMessage("Empty string must be before return");
	}

	@Test
	public void logErrorWhenEmptyLineNotExistBeforeReturnInFor() throws CheckstyleException {
		assertThatCheckFor("src/test/resources/returns/return_check.xml", "src/test/resources/returns/NonEmptyLineInForDef.java")
				.hasErrorsCount(1)

				.hasError()
				.inFileWithName("NonEmptyLineInForDef.java")
				.inLine(11)
				.inColumn(13)
				.forModule("ru.pva.code.style.checks.EmptyLineBeforeReturnCheck")
				.withErrorMessage("Empty string must be before return");
	}

	@Test
	public void logErrorWhenEmptyLineNotExistBeforeReturnInIf() throws CheckstyleException {
		assertThatCheckFor("src/test/resources/returns/return_check.xml", "src/test/resources/returns/NonEmptyLineInIfDef.java")
				.hasErrorsCount(1)

				.hasError()
				.inFileWithName("NonEmptyLineInIfDef.java")
				.inLine(11)
				.inColumn(13)
				.forModule("ru.pva.code.style.checks.EmptyLineBeforeReturnCheck")
				.withErrorMessage("Empty string must be before return");
	}
}