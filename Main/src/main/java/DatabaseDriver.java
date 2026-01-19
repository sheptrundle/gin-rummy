import java.sql.*;

public class DatabaseDriver {
    private final String sqliteFilename;
    private Connection connection;

    public DatabaseDriver() {
        this.sqliteFilename = "gin-rummy-history.sqlite";
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        // Tables for history
        String createTable = """
                CREATE TABLE IF NOT EXISTS History(
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Name1 TEXT NOT NULL,
                    Wins1 Integer NOT NULL,
                    Name2 TEXT NOT NULL,
                    Wins2 Integer NOT NULL,
                    UNIQUE (Name1, Name2)
                );
            """;
        statement.executeUpdate(createTable);
    }

    public void increment(String p1, String p2, String winningPlayer) throws SQLException {

        String sql;

        if (winningPlayer.equals(p1)) {
            sql = """
            UPDATE History
            SET 
                Wins1 = Wins1 + 1
            WHERE Name1 = ? AND Name2 = ?
            OR (Name1 = ? AND Name2 = ?)
        """;
        } else if (winningPlayer.equals(p2)) {
            sql = """
            UPDATE History
            SET 
                Wins2 = Wins2 + 1
            WHERE Name1 = ? AND Name2 = ?
            OR (Name1 = ? AND Name2 = ?)
        """;
        } else {
            throw new IllegalArgumentException("winningPlayer must be p1 or p2");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p1);
            ps.setString(2, p2);
            ps.setString(3, p2);
            ps.setString(4, p1);
            ps.executeUpdate();
        }
    }
}