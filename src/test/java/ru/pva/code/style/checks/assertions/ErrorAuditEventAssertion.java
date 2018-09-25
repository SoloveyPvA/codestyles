package ru.pva.code.style.checks.assertions;

import static com.puppycrawl.tools.checkstyle.utils.CommonUtils.isBlank;
import static java.lang.String.format;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.val;
import ru.pva.code.style.checks.assertions.AssertCheck.AuditEventAssertion;

public class ErrorAuditEventAssertion {

	private final List<AuditEvent> errorEvents;
	private final AuditEventAssertion auditEventAssertion;

	ErrorAuditEventAssertion(final List<AuditEvent> errorEvents,
			final AuditEventAssertion auditEventAssertion) {
		this.errorEvents = errorEvents;
		this.auditEventAssertion = auditEventAssertion;
	}

	public LineAssertion inFileWithName(@NonNull final String fileName) {
		val filteredEvents = new ArrayList<AuditEvent>();

		for (final AuditEvent event : errorEvents) {
			if (!isBlank(event.getFileName()) && event.getFileName().endsWith(fileName)) {
				filteredEvents.add(event);
			}
		}

		if (filteredEvents.isEmpty()) {
			throw new AssertionError(format("Audit listener not registered a error audit event in file: \"%s\"", fileName));
		}

		return new LineAssertion(filteredEvents);
	}

	public class LineAssertion {

		private final List<AuditEvent> errorEvents;

		LineAssertion(final List<AuditEvent> errorEvents) {
			this.errorEvents = errorEvents;
		}

		public ColumnAssertion inLine(final int line) {
			val filteredEvents = new ArrayList<AuditEvent>();

			for (final AuditEvent event : errorEvents) {
				if (event.getLine() == line) {
					filteredEvents.add(event);
				}
			}

			if (filteredEvents.isEmpty()) {
				throw new AssertionError(format("Audit listener not registered a error audit event in line: \"%s\"", line));
			}

			return new ColumnAssertion(filteredEvents);
		}
	}

	public class ColumnAssertion {

		private final List<AuditEvent> errorEvents;

		ColumnAssertion(final List<AuditEvent> errorEvents) {
			this.errorEvents = errorEvents;
		}

		public ModuleAssertion inColumn(final int column) {
			val filteredEvents = new ArrayList<AuditEvent>();

			for (final AuditEvent event : errorEvents) {
				if (event.getColumn() == column) {
					filteredEvents.add(event);
				}
			}

			if (filteredEvents.isEmpty()) {
				throw new AssertionError(format("Audit listener not registered a error audit event in column: \"%s\"", column));
			}

			return new ModuleAssertion(filteredEvents);
		}
	}

	public class ModuleAssertion {

		private final List<AuditEvent> errorEvents;

		ModuleAssertion(final List<AuditEvent> errorEvents) {
			this.errorEvents = errorEvents;
		}

		public ErrorMessageAssertion forModule(@NonNull final String moduleName) {
			val filteredEvents = new ArrayList<AuditEvent>();

			for (final AuditEvent event : errorEvents) {
				if (moduleName.equals(event.getSourceName())) {
					filteredEvents.add(event);
				}
			}

			if (filteredEvents.isEmpty()) {
				throw new AssertionError(
						format(
								"Audit listener not registered a error audit event during \"%s\" module checking",
								moduleName
						)
				);
			}

			return new ErrorMessageAssertion(filteredEvents);
		}
	}

	public class ErrorMessageAssertion {

		private final List<AuditEvent> errorEvents;

		ErrorMessageAssertion(final List<AuditEvent> errorEvents) {
			this.errorEvents = errorEvents;
		}

		public AuditEventAssertion withErrorMessage(@NonNull final String errorMessage) {

			for (final AuditEvent event : errorEvents) {
				if (errorMessage.equals(event.getMessage())) {
					return auditEventAssertion;
				}
			}

			throw new AssertionError(format("Audit listener not registered a error audit event with message: \"%s\"", errorMessage));
		}
	}
}
