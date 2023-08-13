package ad.bingo;

public class Config {
    public static int TicketsInStrip = 6;
    public static int ColumnsPerTicket = 9;
    public static int RowsPerTicket = 3;
    public static int[] NumbersPerStripColumn = new int[]{9, 10, 10, 10, 10, 10, 10, 10, 11};
    public static int[] NumbersPerStripTicket = new int[]{15, 15, 15, 15, 15, 15};
    public static int[] NumbersPerTicketRow = new int[]{5, 5, 5};
    public static int MaximumNumbersPerColumn = 3;
    public static int MinimumNumbersPerColumn = 1;
}
