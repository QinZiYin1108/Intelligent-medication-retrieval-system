import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Map;

public class logUI extends JFrame {
    private int windowWidth = 600;
    private int windowHeight = 400;
    public logUI() throws HeadlessException, SQLException, ClassNotFoundException {
        setTitle("欢迎登录");
        setSize(windowWidth,windowHeight);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int x = (screenWidth-windowWidth) / 2;
        int y = (screenHeight-windowHeight) / 2;

        this.setLocation(x,y);

        JLabel titalLabel = new JLabel("欢迎登录天翼医药系统");
        titalLabel.setFont(new Font("SimSun", Font.BOLD,30));
        titalLabel.setBounds(140,70,400,30);
        add(titalLabel);

        JPanel panel = new JPanel();
        add(panel);
        componentEffects(panel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = 0;
                choice = JOptionPane.showConfirmDialog(null, "您确定要关闭窗口吗？", "关闭确认", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        AllData.statement.close();
                        AllData.connection.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }

    private void componentEffects(JPanel panel) throws SQLException {
        panel.setLayout(null);

        panel.setBounds(140,120,300,200);
        panel.setBackground(Color.white);

        JLabel emailLabel = new JLabel("注册邮箱：");
        emailLabel.setFont(new Font("SimSun",Font.BOLD,15));
        emailLabel.setBounds(20,30,200,25);

        JTextField emailText = new JTextField(30);
        emailText.setFont(new Font("SimSun",Font.BOLD,15));
        emailText.setBounds(100,30,150,25);

        JLabel passwordLabel = new JLabel("账户密码：");
        passwordLabel.setFont(new Font("SimSun",Font.BOLD,15));
        passwordLabel.setBounds(20,70,200,25);

        JPasswordField jPasswordField = new JPasswordField(16);
        jPasswordField.setFont(new Font("SimSun",Font.BOLD,15));
        jPasswordField.setBounds(100,70,150,25);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(20, 110, 70, 25);

        JButton registerButton = new JButton("注册");
        registerButton.setBounds(130, 110, 70, 25);

        JLabel message = new JLabel();
        message.setFont(new Font("SimSun",Font.BOLD,15));
        message.setBounds(20, 150, 300, 25);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emial = emailText.getText();
                String password = new String(jPasswordField.getPassword());

                if (emial.isEmpty() || password.isEmpty()){
                    message.setText("账户邮箱和账户密码不能为空");
                }else{
                    int key=0;
                    for (Map.Entry<String, hospital> entry: AllData.hospitalData.entrySet()) {
                        if (entry.getValue().getHp_email().equals(emial)){
                            key++;
                            if (entry.getValue().getHp_password().equals(password)){
                                try {
                                    AllData.readMachine(entry.getKey());
                                    AllData.readManage(entry.getKey());

                                    hospitalUI hospitalUI = new hospitalUI(entry.getKey());
                                    hplistening hplistening = new hplistening(entry.getKey(), hospitalUI.leftPanel,hospitalUI.rightPanel);

                                    new Thread(hplistening).start();
                                    AllData.logKey = false;
                                    AllData.hospitalKey = true;
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                setVisible(false);
                                dispose();
                            }else{
                                message.setText("账户密码不正确");
                            }
                            break;
                        }
                    }

                    for(Map.Entry<String,docuter> entry: AllData.docuterData.entrySet()){
                        if (entry.getValue().getDcEmial().equals(emial)){
                            key++;
                            if (entry.getValue().getDcPassword().equals(password)){
                                try {
                                    AllData.docuterKey = true;
                                    AllData.readMachine(AllData.docuterData.get(entry.getKey()).getHpCode());
                                    AllData.readRecord(entry.getKey());
                                    AllData.readRecordDrug(entry.getKey());

                                    docuterUI docuterUI = new docuterUI(entry.getKey());
                                    dcListening dcListening = new dcListening(entry.getKey(), docuterUI.leftPanel, docuterUI.rightPanel);

                                    new Thread(dcListening).start();
                                    AllData.logKey = false;
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                setVisible(false);
                                dispose();
                            }else{
                                message.setText("账户密码不正确");
                            }
                        }
                    }

                    for (Map.Entry<String,keeper> entry:AllData.keeperData.entrySet()){
                        if (entry.getValue().getKpEmail().equals(emial)){
                            key++;
                            if (entry.getValue().getKpPassword().equals(password)){
                                try {
                                    AllData.keeperKey = true;
                                    AllData.readMachine(AllData.keeperData.get(entry.getKey()).getHpCode());
                                    AllData.readManage(AllData.keeperData.get(entry.getKey()).getHpCode());

                                    keeperUI keeperUI = new keeperUI(entry.getKey());
                                    kpListening kpListening = new kpListening(entry.getKey(), keeperUI.leftPanel, keeperUI.rightPanel);

                                    new Thread(kpListening).start();
                                    AllData.logKey = false;
                                    setVisible(false);
                                    dispose();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }else{
                                message.setText("账户密码不正确");
                            }
                        }
                    }
                    if (key==0){
                        message.setText("账户未注册");
                    }
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                new registerUI();
            }
        });


        panel.add(registerButton);
        panel.add(message);
        panel.add(loginButton);
        panel.add(jPasswordField);
        panel.add(passwordLabel);
        panel.add(emailText);
        panel.add(emailLabel);
    }
}
