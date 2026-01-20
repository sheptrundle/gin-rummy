package Database;

public class TotalsRow {
    private final String name;
    private final int wins;
    private final int losses;
    private final double pct;

    public TotalsRow(String name, int wins, int losses, double pct) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.pct = pct;
    }

    public String getName() {return name;}
    public int getWins() {return wins;}
    public int getLosses() {return losses;}
    public double getPct() {return pct;}
}
