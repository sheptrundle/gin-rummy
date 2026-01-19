package Database;

public class HistoryRow {
    private final String name1;
    private final int wins1;
    private final String name2;
    private final int wins2;

    public HistoryRow(String name1, int wins1, String name2, int wins2) {
        this.name1 = name1;
        this.wins1 = wins1;
        this.name2 = name2;
        this.wins2 = wins2;
    }

    public String getName1() { return name1; }
    public int getWins1() { return wins1; }
    public String getName2() { return name2; }
    public int getWins2() { return wins2; }
}
