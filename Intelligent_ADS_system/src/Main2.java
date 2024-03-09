import java.sql.SQLException;

/**
 * 加载进度条
 */
public class Main2{
    public static void main(String[] args) throws SQLException {
        MainUI t = new MainUI();
        listening listening = new listening();
        new Thread(t).start();
        new Thread(listening).start();
    }
}
