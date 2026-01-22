package application.port;

import domain.Authorization;

public interface PendingAuthorizationCursor extends AutoCloseable {

	boolean hasNext();

	Authorization next();

	@Override
	void close();
	
}
