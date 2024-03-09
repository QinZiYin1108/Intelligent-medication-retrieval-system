import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class reDocuter extends JFrame {
    private int windowWidth = 400;
    private int windowHeight = 580;

    public reDocuter(){
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

        JLabel jLabel = new JLabel("欢 迎 注 册");
        jLabel.setFont(new Font("黑体",Font.BOLD,25));
        jLabel.setBounds(120,20,300,25);

        JLabel jLabel1 = new JLabel("天 翼 医 疗 系 统");
        jLabel1.setFont(new Font("黑体",Font.BOLD,25));
        jLabel1.setBounds(80,60,300,25);

        int x1 = 20;
        int x2 = 200;

        int y1 = 100;
        int y2 = 40;

        int width=180;

        JLabel emailJLable = new JLabel("账号邮箱：");
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

        JLabel nameJLable = new JLabel("医师姓名：");
        nameJLable.setFont(new Font("SimSun",Font.BOLD,15));
        nameJLable.setBounds(x1,y1+y2*3,100,25);

        JTextField nameText = new JTextField(30);
        nameText.setFont(new Font("SimSun",Font.BOLD,15));
        nameText.setBounds(x2,y1+y2*3,width,25);

        JLabel idJLable = new JLabel("执业医师资格证编号：");
        idJLable.setFont(new Font("SimSun",Font.BOLD,15));
        idJLable.setBounds(x1,y1+y2*4,300,25);

        JTextField idText = new JTextField(30);
        idText.setFont(new Font("SimSun",Font.BOLD,15));
        idText.setBounds(x2,y1+y2*4,width,25);

        JLabel idCardJLable = new JLabel("个人身份证号：");
        idCardJLable.setFont(new Font("SimSun",Font.BOLD,15));
        idCardJLable.setBounds(x1,y1+y2*5,300,25);

        JTextField idCardText = new JTextField(30);
        idCardText.setFont(new Font("SimSun",Font.BOLD,15));
        idCardText.setBounds(x2,y1+y2*5,width,25);

        JLabel ageJLable = new JLabel("年龄：");
        ageJLable.setFont(new Font("SimSun",Font.BOLD,15));
        ageJLable.setBounds(x1,y1+y2*6,300,25);

        JTextField ageText = new JTextField(30);
        ageText.setFont(new Font("SimSun",Font.BOLD,15));
        ageText.setBounds(x2,y1+y2*6,width,25);

        JLabel sexJLable = new JLabel("性别：");
        sexJLable.setFont(new Font("SimSun",Font.BOLD,15));
        sexJLable.setBounds(x1,y1+y2*7,300,25);

        JTextField sexText = new JTextField(30);
        sexText.setFont(new Font("SimSun",Font.BOLD,15));
        sexText.setBounds(x2,y1+y2*7,width,25);

        JLabel phoneJLable = new JLabel("联系电话：");
        phoneJLable.setFont(new Font("SimSun",Font.BOLD,15));
        phoneJLable.setBounds(x1,y1+y2*8,300,25);

        JTextField phoneText = new JTextField(30);
        phoneText.setFont(new Font("SimSun",Font.BOLD,15));
        phoneText.setBounds(x2,y1+y2*8,width,25);

        JLabel hpCodeJLable = new JLabel("所属医院：");
        hpCodeJLable.setFont(new Font("SimSun",Font.BOLD,15));
        hpCodeJLable.setBounds(x1,y1+y2*9,300,25);

        JButton reButton = new JButton("确定");
        reButton.setFont(new Font("SimSun",Font.BOLD,15));
        reButton.setBounds(130,y1+y2*10,100,25);

        String[] names = new String[AllData.hospitalCode.size()];

        for(int i=0;i<AllData.hospitalCode.size();i++){
            names[i] = AllData.hospitalData.get(AllData.hospitalCode.get(i)).getHp_name();
        }

        JComboBox<String> hpCodeBox = new JComboBox<>(names);
        hpCodeBox.setFont(new Font("SimSun",Font.BOLD,15));
        hpCodeBox.setBounds(x2,y1+y2*9,170,25);

        reButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailText.getText();
                String password = new String(passwordText.getPassword());
                String passwordConfirm = new String(passwordConfirmText.getPassword());
                String name = nameText.getText();
                String id = idText.getText();
                String idCard = idCardText.getText();
                int age = Integer.parseInt(ageText.getText());
                String sex = sexText.getText();
                String phone = phoneText.getText();
                int index = hpCodeBox.getSelectedIndex();
                String hpCode = AllData.hospitalCode.get(index);

                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);

                String dateString = "2014-05-27 00:00:00";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = LocalDateTime.parse(dateString, formatter);
                Timestamp timestamp1 = Timestamp.valueOf(date);

                if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || name.isEmpty() ||
                        id.isEmpty() || idCard.isEmpty() || ageText.getText().isEmpty() || sex.isEmpty() || phone.isEmpty()){
                    JOptionPane.showMessageDialog(reDocuter.this, "每项内容均不能为空");
                }else if (!isWaitingEmail(email)){
                    JOptionPane.showMessageDialog(reDocuter.this, "账户邮箱已申请，请耐心等待，不要重复注册");
                }else if (!isEmail(email)){
                    JOptionPane.showMessageDialog(reDocuter.this, "账户邮箱已注册");
                }else if(!password.equals(passwordConfirm)){
                    JOptionPane.showMessageDialog(reDocuter.this, "确认密码不相等");
                }else if(password.length()>16){
                    JOptionPane.showMessageDialog(reDocuter.this, "密码只能为小于16位的英文字母和数字组成");
                }else if(idCard.length()!=18){
                    JOptionPane.showMessageDialog(reDocuter.this, "请填写正确的身份证号");
                }else if(id.length()>27 || id.length()<24){
                    JOptionPane.showMessageDialog(reDocuter.this, "请填写正确的执业医师资格证编号");
                }else if (!Objects.equals(sex, "男") && !Objects.equals(sex, "女")) {
                    JOptionPane.showMessageDialog(reDocuter.this, "请输入正确的性别");
                } else if (phone.length()!=11) {
                    JOptionPane.showMessageDialog(reDocuter.this, "请输入正确的手机号");
                }else{
                    String docuterCode = isDcCode();
                    String requestCode = isRequestCode();
                    try {
                        PreparedStatement keyOut = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
                        PreparedStatement keyPut = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1");
                        PreparedStatement prepared = AllData.connection.prepareStatement("INSERT INTO iamd.docuter_waiting VALUES (?,?,?,?,?,?,?,?,?,?)");
                        PreparedStatement prepared1 = AllData.connection.prepareStatement("INSERT INTO iamd.request_employment(re_code, em_code, hp_code, re_timeOn,re_timeOK) VALUES (?,?,?,?,?)");

                        keyOut.execute();

                        prepared.setString(1,docuterCode);
                        prepared.setString(2,hpCode);
                        prepared.setString(3,email);
                        prepared.setString(4,password);
                        prepared.setString(5,name);
                        prepared.setString(6,id);
                        prepared.setString(7,idCard);
                        prepared.setInt(8,age);
                        prepared.setString(9,sex);
                        prepared.setString(10,phone);
                        prepared.executeUpdate();

                        prepared1.setString(1,requestCode);
                        prepared1.setString(2,docuterCode);
                        prepared1.setString(3,hpCode);
                        prepared1.setTimestamp(4,timestamp);
                        prepared1.setTimestamp(5,timestamp1);
                        prepared1.executeUpdate();

                        keyPut.execute();

                        request request = new request(docuterCode, hpCode,"未通过", now, date);
                        AllData.requestData.put(requestCode,request);
                        AllData.requestCode.add(requestCode);

                        docuter docuter = new docuter(hpCode, email, password, name, id, idCard, age, sex, phone);
                        AllData.docuterWaitingData.put(docuterCode,docuter);
                        AllData.docuterWaitingCode.add(docuterCode);

                        JOptionPane.showMessageDialog(reDocuter.this, "以向目标医院发出申请，请耐心等待");

                        new logUI();
                        setVisible(false);
                        dispose();

                        prepared.close();
                        prepared1.close();
                        keyPut.close();
                        keyOut.close();

                    } catch (SQLException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            private String isRequestCode() {
                if (AllData.requestCode.isEmpty()){
                    return "#REQ1";
                }else {
                    int size = AllData.requestCode.size();
                    String string = AllData.requestCode.get(size-1);
                    return "#REQ" + (Integer.parseInt(string.substring(4,string.length()))+1);
                }
            }

            private String isDcCode() {
                if (AllData.docuterWaitingCode.isEmpty()){
                    return "#DW1";
                }else{
                    int size = AllData.docuterWaitingCode.size();
                    String string = AllData.docuterWaitingCode.get(size-1);
                    return "#DW" + (Integer.parseInt(string.substring(3,string.length())) + 1);
                }
            }

            private boolean isEmail(String email) {
                for (Map.Entry<String,docuter> entry: AllData.docuterData.entrySet()){
                    if (entry.getValue().getDcEmial().equals(email)){
                        return false;
                    }
                }
                return true;
            }

            private boolean isWaitingEmail(String email){
                for (Map.Entry<String, docuter> entry:AllData.docuterWaitingData.entrySet()){
                    if (entry.getValue().getDcEmial().equals(email)){
                        return false;
                    }
                }
                return true;
            }
        });

        add(emailText);
        add(emailJLable);
        add(passwordText);
        add(passwordJLable);
        add(passwordConfirmText);
        add(passwordConfirmJLable);
        add(nameText);
        add(nameJLable);
        add(idText);
        add(idJLable);
        add(idCardText);
        add(idCardJLable);
        add(ageText);
        add(ageJLable);
        add(sexText);
        add(sexJLable);
        add(phoneText);
        add(phoneJLable);
        add(hpCodeBox);
        add(hpCodeJLable);
        add(reButton);
        add(jLabel);
        add(jLabel1);

        setVisible(true);
    }
}
