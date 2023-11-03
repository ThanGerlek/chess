package dataAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChessDatabase extends Database {

    public void update(String sqlString) throws DataAccessException {
        update(sqlString, sp -> {
        });
    }

    public void update(String sqlString, StatementPreparer sp) throws DataAccessException {
        Connection conn = getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            sp.prepare(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to run executeUpdate() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            returnConnection(conn);
        }
    }

    public void updateWithParam(String sqlString, String param) throws DataAccessException {
        update(sqlString, preparedStatement -> {
            preparedStatement.setString(1, param);
        });
    }

    public void updateWithParam(String sqlString, int param) throws DataAccessException {
        update(sqlString, preparedStatement -> {
            preparedStatement.setInt(1, param);
        });
    }

    public boolean booleanQueryWithParam(String sqlString, String param) throws DataAccessException {
        return booleanQuery(sqlString, preparedStatement -> {
            preparedStatement.setString(1, param);
        });
    }

    public boolean booleanQuery(String sqlString, StatementPreparer sp) throws DataAccessException {
        ResultSet rs = query(sqlString, sp);
        try {
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Failed to read ResultSet on boolean SQL query: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        }
    }

    public ResultSet query(String sqlString, StatementPreparer sp) throws DataAccessException {
        Connection conn = getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            sp.prepare(preparedStatement);
            try (var rs = preparedStatement.executeQuery()) {
                return rs;
            }
        } catch (SQLException e) {
            System.out.println("Failed to run executeQuery() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            returnConnection(conn);
        }
    }

    public boolean booleanQueryWithParam(String sqlString, int param) throws DataAccessException {
        return booleanQuery(sqlString, preparedStatement -> {
            preparedStatement.setInt(1, param);
        });
    }
}