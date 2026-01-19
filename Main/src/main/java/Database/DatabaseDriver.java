package Database;

import Backend.Player;

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

    public Connection getConnection() {
        return connection;
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

    public void increment(Player player1, Player player2, Player winningPlayer) throws SQLException {
        // Normalize: always store matchup alphabetically
        String p1 = player1.getName();
        String p2 = player2.getName();
        String winnerString = winningPlayer.getName();

        String first, second;
        boolean p1IsFirst;

        if (p1.compareTo(p2) < 0) {
            first = p1;
            second = p2;
            p1IsFirst = true;
        } else {
            first = p2;
            second = p1;
            p1IsFirst = false;
        }

        String columnToIncrement;
        if (winnerString.equals(p1)) {
            columnToIncrement = (p1IsFirst) ? "Wins1" : "Wins2";
        } else if (winnerString.equals(p2)) {
            columnToIncrement = (p1IsFirst) ? "Wins2" : "Wins1";
        } else {
            throw new IllegalArgumentException("winningPlayer must be p1 or p2");
        }

        String sqlUpdate = "UPDATE History SET " + columnToIncrement + " = " + columnToIncrement + " + 1 " +
                "WHERE Name1 = ? AND Name2 = ?";

        try (PreparedStatement ps = connection.prepareStatement(sqlUpdate)) {
            ps.setString(1, first);
            ps.setString(2, second);
            int rowsUpdated = ps.executeUpdate();

            // If no row exists, insert a new row
            if (rowsUpdated == 0) {
                int wins1 = columnToIncrement.equals("Wins1") ? 1 : 0;
                int wins2 = columnToIncrement.equals("Wins2") ? 1 : 0;

                String sqlInsert = "INSERT INTO History(Name1, Wins1, Name2, Wins2) VALUES(?, ?, ?, ?)";
                try (PreparedStatement insertPs = connection.prepareStatement(sqlInsert)) {
                    insertPs.setString(1, first);
                    insertPs.setInt(2, wins1);
                    insertPs.setString(3, second);
                    insertPs.setInt(4, wins2);
                    insertPs.executeUpdate();
                }
            }
        }
    }

    public void clearHistory() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM History");
            System.out.println("All matchups have been deleted.");
        }
    }

}