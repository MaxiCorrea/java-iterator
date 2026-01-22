package domain;

import java.util.UUID;

public class Authorization {

	private final UUID id;
	private AuthorizationStatus status;
	
	public Authorization(
			final UUID id,
			final AuthorizationStatus status) {
		this.id = id;
		this.status = status;
	}
	
	public UUID getId() {
		return id;
	}
	
	public AuthorizationStatus getStatus() {
		return status;
	}
	
	public void approve() {
		this.status = AuthorizationStatus.APPROVED;
	}
	
}

