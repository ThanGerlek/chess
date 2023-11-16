package dataAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    /**
     * Returns true if the SQL query has at least one result.
     *
     * @param sqlString the SQL query to execute
     * @param sp        a lambda that inserts any parameters
     * @return true if the SQL query has at least one result
     */
    public boolean booleanQuery(String sqlString, StatementPreparer sp) throws DataAccessException {
        Connection conn = getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            sp.prepare(preparedStatement);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
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

    /**
     * Return a list of resultColumnLabel values from the rows returned by the SQL query.
     *
     * @param sqlString         the SQL query to execute
     * @param sp                a lambda that inserts any parameters
     * @param resultColumnLabel the SQL column to return values from
     * @return a list of resultColumnLabel values from rows returned by the query
     */
    public ArrayList<String> queryForString(String resultColumnLabel, String sqlString, StatementPreparer sp)
            throws DataAccessException {
        Connection conn = getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            sp.prepare(preparedStatement);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                ArrayList<String> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rs.getString(resultColumnLabel));
                }
                return results;
            }


        } catch (SQLException e) {
            System.out.println("Failed to run executeQuery() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            returnConnection(conn);
        }
    }
}