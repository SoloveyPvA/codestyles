package ru.pva.code.style.checks.assertions;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

@Getter
public class StubAuditListener implements AuditListener {

	private final List<AuditEvent> auditStartedEvents = new ArrayList<>();
	private final List<AuditEvent> auditFinishedEvents = new ArrayList<>();
	private final List<AuditEvent> fileStartedEvents = new ArrayList<>();
	private final List<AuditEvent> fileFinishedEvents = new ArrayList<>();
	private final List<AuditEvent> errorEvents = new ArrayList<>();
	private final List<Pair<AuditEvent, Throwable>> exceptionEvents = new ArrayList<>();

	@Override
	public void auditStarted(final AuditEvent auditEvent) {
		auditStartedEvents.add(auditEvent);
	}

	@Override
	public void auditFinished(final AuditEvent auditEvent) {
		auditFinishedEvents.add(auditEvent);
	}

	@Override
	public void fileStarted(final AuditEvent auditEvent) {
		fileStartedEvents.add(auditEvent);
	}

	@Override
	public void fileFinished(final AuditEvent auditEvent) {
		fileFinishedEvents.add(auditEvent);
	}

	@Override
	public void addError(final AuditEvent auditEvent) {
		errorEvents.add(auditEvent);
	}

	@Override
	public void addException(final AuditEvent auditEvent, final Throwable throwable) {
		exceptionEvents.add(Pair.of(auditEvent, throwable));
	}
}
