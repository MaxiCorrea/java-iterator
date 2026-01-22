package infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import application.port.PendingAuthorizationCursor;
import domain.Authorization;
import domain.AuthorizationStatus;

public class JdbcPendingAuthorizationCursor implements PendingAuthorizationCursor {

	private static final int PAGE_SIZE = 100;
	
	private final DataSource dataSource;
	private int offset = 0;
	
	private ResultSet resultSet;
	private Connection connection;
	private PreparedStatement statement;
	
	public JdbcPendingAuthorizationCursor(
			final DataSource dataSource) {
		this.dataSource = dataSource;
		fetchNextPage();
	}
	
	@Override
	public boolean hasNext() {
		try {
			if(resultSet != null && resultSet.next()) 
				return true;
			fetchNextPage();
			return resultSet != null && resultSet.next();
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Authorization next() {
		try {
			return new Authorization(UUID.fromString(
					resultSet.getString("id")), 
					AuthorizationStatus.valueOf(resultSet.getString("status")));
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void close() {
		closeResources();
	}

	private void fetchNextPage() {
		closeResources();
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement("SELECT id, status FROM authorization WHERE status='PENDING' ORDER BY id LIMIT ? OFFSET ?");
			statement.setInt(1, PAGE_SIZE);
			statement.setInt(2, offset);
			resultSet = statement.executeQuery();
			offset += PAGE_SIZE;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void closeResources() {
		try{
			if(resultSet != null) resultSet.close();
			if(statement != null) statement.close();
			if(connection != null) connection.close();
		}catch(SQLException ignored) {}
	}
	
}
