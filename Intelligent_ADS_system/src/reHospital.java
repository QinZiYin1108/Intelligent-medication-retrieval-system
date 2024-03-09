import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class reHospital extends JFrame{
    private int windowWidth = 400;
    private int windowHeight = 500;

    public reHospital(){
        setTitle("账号注册");
        setLayout(null);
        setSize(windowWidth,windowHeight);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int x = (screenWidth-windowWidth) / 2;
        int y = (screenHeight-windowHeight) / 2;

        this.setLocation(x,y);

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

        int x1 = 20;
        int x2 = 200;

        int y1 = 100;
        int y2 = 40;

        int width=180;

        JLabel jLabel = new JLabel("欢 迎 注 册");
        jLabel.setFont(new Font("黑体",Font.BOLD,25));
        jLabel.setBounds(120,20,300,25);

        JLabel jLabel1 = new JLabel("天 翼 医 疗 系 统");
        jLabel1.setFont(new Font("黑体",Font.BOLD,25));
        jLabel1.setBounds(80,60,300,25);

        JLabel emailJLable = new JLabel("账户邮箱：");
        emailJLable.setFont(new Font("SimSun",Font.BOLD,15));
        emailJLable.setBounds(x1,y1,100,25);

        JTextField emailText = new JTextField(30);
        emailText.setFont(new Font("SimSun",Font.BOLD,15));
        emailText.setBounds(x2,y1,width,25);

        JLabel passwordJLable = new JLabel("账户密码：");
        passwordJLable.setFont(new Font("SimSun",Font.BOLD,15));
        passwordJLable.setBounds(x1,y1+y2,240,25);

        JPasswordField passwordText = new JPasswordField(30);
        passwordText.setFont(new Font("SimSun",Font.BOLD,15));
        passwordText.setBounds(x2,y1+y2,width,25);

        JLabel passwordConfirmJLable = new JLabel("确认密码：");
        passwordConfirmJLable.setFont(new Font("SimSun",Font.BOLD,15));
        passwordConfirmJLable.setBounds(x1,y1+y2*2,100,25);

        JPasswordField passwordConfirmText = new JPasswordField(30);
        passwordConfirmText.setFont(new Font("SimSun",Font.BOLD,15));
        passwordConfirmText.setBounds(x2,y1+y2*2,width,25);

        JLabel nameJLable = new JLabel("医院名称：");
        nameJLable.setFont(new Font("SimSun",Font.BOLD,15));
        nameJLable.setBounds(x1,y1+y2*3,100,25);

        JTextField nameText = new JTextField(30);
        nameText.setFont(new Font("SimSun",Font.BOLD,15));
        nameText.setBounds(x2,y1+y2*3,width,25);

        JLabel licenseJLable = new JLabel("医疗机构执业许可证编号：");
        licenseJLable.setFont(new Font("SimSun",Font.BOLD,15));
        licenseJLable.setBounds(x1,y1+y2*4,300,25);

        JTextField licenseText = new JTextField(30);
        licenseText.setFont(new Font("SimSun",Font.BOLD,15));
        licenseText.setBounds(x2,y1+y2*4,width,25);

        JLabel busLiJLable = new JLabel("营业执照编号：");
        busLiJLable.setFont(new Font("SimSun",Font.BOLD,15));
        busLiJLable.setBounds(x1,y1+y2*5,300,25);

        JTextField busLiText = new JTextField(30);
        busLiText.setFont(new Font("SimSun",Font.BOLD,15));
        busLiText.setBounds(x2,y1+y2*5,width,25);

        JLabel legalPersonNameJLable = new JLabel("医院法人名称：");
        legalPersonNameJLable.setFont(new Font("SimSun",Font.BOLD,15));
        legalPersonNameJLable.setBounds(x1,y1+y2*6,300,25);

        JTextField legalPersonNameText = new JTextField(30);
        legalPersonNameText.setFont(new Font("SimSun",Font.BOLD,15));
        legalPersonNameText.setBounds(x2,y1+y2*6,width,25);

        JLabel legalPersonIDJLable = new JLabel("医院法人身份证号：");
        legalPersonIDJLable.setFont(new Font("SimSun",Font.BOLD,15));
        legalPersonIDJLable.setBounds(x1,y1+y2*7,300,25);

        JTextField legalPersonIDText = new JTextField(30);
        legalPersonIDText.setFont(new Font("SimSun",Font.BOLD,15));
        legalPersonIDText.setBounds(x2,y1+y2*7,width,25);

        JButton reButton = new JButton("确定");
        reButton.setFont(new Font("SimSun",Font.BOLD,15));
        reButton.setBounds(130,y1+y2*8,100,25);

        reButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailText.getText();
                String password = new String(passwordText.getPassword());
                String passwordConfirm = new String(passwordConfirmText.getPassword());
                String name = nameText.getText();
                String license = licenseText.getText();
                String busLi = busLiText.getText();
                String legalPersonName = legalPersonNameText.getText();
                String legalPersonID = legalPersonIDText.getText();

                if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || name.isEmpty() ||
                        license.isEmpty() || busLi.isEmpty() || legalPersonName.isEmpty() || legalPersonID.isEmpty()){
                    JOptionPane.showMessageDialog(reHospital.this, "每项内容均不能为空");
                } else if (!isEmail(email)){
                    JOptionPane.showMessageDialog(reHospital.this, "账户邮箱已注册");
                }else if(!password.equals(passwordConfirm)){
                    JOptionPane.showMessageDialog(reHospital.this, "确认密码不相等");
                }else if(password.length()>16){
                    JOptionPane.showMessageDialog(reHospital.this, "密码只能为小于16位的英文字母和数字组成");
                }else if(legalPersonID.length()!=18){
                    JOptionPane.showMessageDialog(reHospital.this, "请填写正确的法人身份证号");
                }else if(license.length()!=15){
                    JOptionPane.showMessageDialog(reHospital.this, "请填写正确的医疗机构执业许可证编号");
                }else if(busLi.length() != 18){
                    JOptionPane.showMessageDialog(reHospital.this, "请填写正确的营业执照编号");
                }else{
                    String hospitalCode = isHpCode();
                    try {
                        PreparedStatement keyOut = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
                        PreparedStatement keyPut = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
                        PreparedStatement prepared = AllData.connection.prepareStatement("INSERT INTO iamd.hospital VALUES (?,?,?,?,?,?,?,?)");

                        keyOut.execute();

                        prepared.setString(1,hospitalCode);
                        prepared.setString(2,email);
                        prepared.setString(3,password);
                        prepared.setString(4,name);
                        prepared.setString(5,license);
                        prepared.setString(6,busLi);
                        prepared.setString(7,legalPersonName);
                        prepared.setString(8,legalPersonID);
                        prepared.execute();

                        keyPut.execute();

                        hospital hospital = new hospital(email, password, name, license, busLi, legalPersonName, legalPersonID);
                        AllData.hospitalData.put(hospitalCode,hospital);
                        AllData.hospitalCode.add(hospitalCode);

                        JOptionPane.showMessageDialog(reHospital.this, "注册成功");

                        new logUI();
                        setVisible(false);
                        dispose();

                        prepared.close();
                        keyPut.close();
                        keyOut.close();

                    } catch (SQLException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            private String isHpCode() {
                if (AllData.hospitalCode.isEmpty()){
                    return "#H1";
                }else {
                    int size = AllData.hospitalCode.size();
                    String string = AllData.hospitalCode.get(size - 1);
                    int i = Integer.parseInt(string.substring(2,string.length()))+1;
                    return "#H" + i;
                }

            }

            private boolean isEmail(String email) {
                for (Map.Entry<String, hospital> entry:AllData.hospitalData.entrySet()) {
                    if (entry.getValue().getHp_email().equals(email)){
                        return false;
                    }
                }
                return true;
            }
        });



        add(jLabel1);
        add(jLabel);
        add(reButton);
        add(legalPersonIDText);
        add(legalPersonIDJLable);
        add(legalPersonNameText);
        add(legalPersonNameJLable);
        add(busLiText);
        add(busLiJLable);
        add(licenseText);
        add(licenseJLable);
        add(nameText);
        add(nameJLable);
        add(passwordConfirmText);
        add(passwordConfirmJLable);
        add(passwordJLable);
        add(passwordText);
        add(emailText);
        add(emailJLable);
        setVisible(true);
    }
}
