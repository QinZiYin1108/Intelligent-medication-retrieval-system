import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class registerUI extends JFrame {
    private int windowWidth = 300;
    private int windowHeight = 200;

    public registerUI(){
        setTitle("请选择.....");
        setLayout(null);
        setSize(windowWidth,windowHeight);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int x = (screenWidth-windowWidth) / 2;
        int y = (screenHeight-windowHeight) / 2;

        this.setLocation(x,y);

        JButton hospitalButton = new JButton("注册医院账户");
        hospitalButton.setFont(new Font("SimSun",Font.BOLD,15));
        hospitalButton.setBounds(40,110,200,25);

        JButton docuterButton = new JButton("注册医生账户");
        docuterButton.setFont(new Font("SimSun",Font.BOLD,15));
        docuterButton.setBounds(40,70,200,25);

        JButton keeperButton = new JButton("注册机器管理员账户");
        keeperButton.setFont(new Font("SimSun",Font.BOLD,15));
        keeperButton.setBounds(40,30,200,25);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    new logUI();
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        hospitalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new reHospital();
                setVisible(false);
                dispose();
            }
        });

        docuterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new reDocuter();
                setVisible(false);
                dispose();
            }
        });

        keeperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new reKeeper();
                setVisible(false);
                dispose();
            }
        });

        add(docuterButton);
        add(keeperButton);
        add(hospitalButton);
        setVisible(true);
    }

}
