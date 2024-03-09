import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * 加载进度条
 */
public class Main{
    public static void main(String[] args) throws SQLException {
        MainUI t = new MainUI();
        listening listening = new listening();
        new Thread(t).start();
        new Thread(listening).start();
    }
}

class listening implements Runnable{

    @Override
    public void run() {
        while (AllData.logKey){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String sql = "SELECT COUNT(*) FROM hospital ";
            String sqlDc = "SELECT count(*) FROM docuter";
            String sqlKp = "SELECT  COUNT(*) from keeper";
            String sqlDcWait = "SELECT count(*) FROM docuter_waiting";
            String sqlKpWait = "SELECT COUNT(*) FROM keeper_waiting";
            try {
                ResultSet reKp = AllData.statement.executeQuery(sqlKp);
                reKp.next();
                int kpCount = reKp.getInt(1);
                reKp.close();

                ResultSet reDc = AllData.statement.executeQuery(sqlDc);
                reDc.next();
                int dcCount = reDc.getInt(1);
                reDc.close();

                ResultSet reHp = AllData.statement.executeQuery(sql);
                reHp.next();
                int hpCount = reHp.getInt(1);
                reHp.close();

                ResultSet reDcWait = AllData.statement.executeQuery(sqlDcWait);
                reDcWait.next();
                int dcWaitCount = reDcWait.getInt(1);
                reDcWait.close();

                ResultSet reKpWait = AllData.statement.executeQuery(sqlKpWait);
                reKpWait.next();
                int kpWaitCount = reKpWait.getInt(1);
                reKpWait.close();

                if (hpCount!=AllData.hospitalCode.size()){
                    AllData.readHospital();
                }
                if (dcCount!=AllData.docuterCode.size()){
                    AllData.readDocuter();
                }
                if (kpCount!=AllData.keeperCode.size()){
                    AllData.readKeeper();
                }
                if (dcWaitCount!=AllData.docuterWaitingCode.size()){
                    AllData.readDocuterWaiting();
                }
                if (kpWaitCount!=AllData.keeperWaitingCode.size()){
                    AllData.readKeeperWaiting();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class MainUI extends JWindow implements Runnable {

    /*** 定义加载窗口大小*/
    public static final int LOAD_WIDTH = 600;// 页面宽度
    public static final int LOAD_HEIGHT = 400;// 页面高度

    /*** 获取屏幕窗口大小*/
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    /*** 定义进度条组件*/

    /*** 定义标签组件*/

    public JLabel jLabel;

    // 构造函数
    public MainUI() {
        setAlwaysOnTop(true);
        // 创建标签,并在标签上放置一张背景图
        JLabel jtable = new JLabel("欢迎登录天翼医药系统");
        jtable.setFont(new Font("SimSun",Font.BOLD,40));
        jtable.setBounds(100,130,600,50);
        // 创建进度条
        jLabel = new JLabel("加载中........");
        jLabel.setFont(new Font("SimSun",Font.BOLD,15));
        jLabel.setBounds(225,240,200,30);
        // 添加组件
        this.add(jtable);
        this.add(jLabel);
        // 设置布局为空
        this.setLayout(null);
        // 设置窗口初始位置
        this.setLocation((WIDTH - LOAD_WIDTH) / 2, (HEIGHT - LOAD_HEIGHT) / 2);
        // 设置窗口大小
        this.setSize(LOAD_WIDTH, LOAD_HEIGHT);
        // 设置窗口显示
        this.setVisible(true);
    }

    @Override
    public void run() {
        try {
            AllData.readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 如果这里不继续执行代码，将关闭本次运行
        try {
            new logUI();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.dispose();
    }

}