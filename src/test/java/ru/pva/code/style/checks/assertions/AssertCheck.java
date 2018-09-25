package ru.pva.code.style.checks.assertions;

import static com.puppycrawl.tools.checkstyle.ConfigurationLoader.loadConfiguration;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.File;
import java.util.Objects;
import lombok.val;

public class AssertCheck {

	public static AuditEventAssertion assertThatCheckFor(final String fileConfigPath, final String checkPath)
			throws CheckstyleException {
		val checkingDirectory = new File(checkPath);
		val checker = new Checker();
		val listener = new StubAuditListener();
		val config = loadConfiguration(
				fileConfigPath,
				new PropertiesExpander(System.getProperties())
		);

		checker.setModuleClassLoader(Checker.class.getClassLoader());
		checker.configure(config);
		checker.addListener(listener);

		val errorCounter = checker.process(singletonList(checkingDirectory));

		return new AuditEventAssertion(listener, errorCounter);
	}

	public static class AuditEventAssertion {

		private final StubAuditListener listener;
		private final int errorCount;

		AuditEventAssertion(final StubAuditListener listener, final int errorCounter) {
			this.errorCount = errorCounter;
			this.listener = listener;
		}

		public AuditEventAssertion hasErrorsCount(final int expected) {
			assertThat(errorCount).isEqualTo(expected);
			assertThat(listener.getErrorEvents()).hasSize(expected);

			return this;
		}

		public ErrorAuditEventAssertion hasError() {
			assertThat(listener.getErrorEvents()).isNotEmpty();
			return new ErrorAuditEventAssertion(listener.getErrorEvents(), this);
		}

		public AuditEventAssertion containsErrorAuditEventWithMessage(final String expected) {
			assertThat(listener.getErrorEvents()).isNotEmpty();

			for (final AuditEvent event : listener.getErrorEvents()) {
				if (Objects.equals(event.getMessage(), expected)) {
					return this;
				}
			}

			throw new AssertionError(format("Audit listener not registered a error audit event with message: \"%s\"", expected));
		}
	}
}
