package application.usecase;

import application.port.PendingAuthorizationCursor;
import domain.Authorization;

public class ProcessPendingAuthorizationUseCase {

	private final PendingAuthorizationCursor authorizationCursor;
	
	public ProcessPendingAuthorizationUseCase(
			final PendingAuthorizationCursor authorizationCursor) {
		this.authorizationCursor = authorizationCursor;
	}
	
	public void execute() {
		try(PendingAuthorizationCursor c = authorizationCursor) {
			while(c.hasNext()) {
				Authorization authorization = c.next();
				authorization.approve();
			}
		}
	}
	
}
