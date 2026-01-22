package application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import application.port.PendingAuthorizationCursor;
import domain.Authorization;
import domain.AuthorizationStatus;

class ProcessPendingAuthorizationUseCaseTest {

	class InMemoryCursor implements PendingAuthorizationCursor {

		Iterator<Authorization> ite;

		public InMemoryCursor(final List<Authorization> data) {
			this.ite = data.iterator();
		}

		@Override
		public boolean hasNext() {
			return ite.hasNext();
		}

		@Override
		public Authorization next() {
			return ite.next();
		}

		@Override
		public void close() {
		}

	}

	@Test
	void shouldProcessAllPendingAuthorizations() {
		List<Authorization> data = List.of(
				new Authorization(UUID.randomUUID(), AuthorizationStatus.PENDING),
				new Authorization(UUID.randomUUID(), AuthorizationStatus.PENDING));
		PendingAuthorizationCursor cursor = new InMemoryCursor(data);
		ProcessPendingAuthorizationUseCase usecase = new ProcessPendingAuthorizationUseCase(cursor);
		usecase.execute();
		data.forEach(e -> assertEquals(AuthorizationStatus.APPROVED, e.getStatus()));
	}

}
