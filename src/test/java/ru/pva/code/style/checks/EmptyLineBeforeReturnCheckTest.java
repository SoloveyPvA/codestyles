package ru.pva.code.style.checks;

import static com.puppycrawl.tools.checkstyle.ConfigurationLoader.loadConfiguration;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.Main;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.File;
import java.io.IOException;
import lombok.val;
import org.testng.annotations.Test;

@Test
public class EmptyLineBeforeReturnCheckTest {

	@Test(enabled = false)
	public void emptyStringBeforeReturnInMethodDef() throws IOException {
		Main.main("-c", "src/test/resources/returns/return_check.xml", "src/test/resources/returns/EmptyLine.java");
	}

	@Test
	public void checkFile() throws CheckstyleException {
		val checkingDirectory = new File("src/test/resources/returns/EmptyLine.java");
		val checker = new Checker();
		val listener = mock(AuditListener.class);
		val config = loadConfiguration(
				"src/test/resources/returns/return_check.xml",
				new PropertiesExpander(System.getProperties())
		);

		checker.setModuleClassLoader(Checker.class.getClassLoader());
		checker.configure(config);
		checker.addListener(listener);

		val errorCounter = checker.process(singletonList(checkingDirectory));

		assertThat(errorCounter).isEqualTo(1);
		verify(listener).addError(any(AuditEvent.class));
	}
}