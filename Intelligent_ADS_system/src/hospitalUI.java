import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

class hplistening implements Runnable{
    String hpCode;
    JPanel leftPanel;
    JPanel rightPanel;
    public hplistening(String hpCode,JPanel leftPanel,JPanel rightPanel){
        this.hpCode = hpCode;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
    }
    @Override
    public void run() {
        String sqlRe = "SELECT COUNT(*) FROM request_employment";
        String sql = "SELECT COUNT(*) FROM hospital ";
        String sqlDc = "SELECT count(*) FROM docuter";
        String sqlKp = "SELECT  COUNT(*) from keeper";
        String sqlDcWait = "SELECT count(*) FROM docuter_waiting";
        String sqlKpWait = "SELECT COUNT(*) FROM keeper_waiting";
        String sqlMg = "SELECT COUNT(*) FROM manage WHERE kp_code IN (SELECT kp_code FROM keeper WHERE hp_code = '"+hpCode+"')";
        String sqlMh = "SELECT COUNT(*) FROM machine WHERE hp_code = ' "+hpCode+"' ORDER BY mh_code+0";
        while (AllData.hospitalKey){
            int key = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                ResultSet reMh = AllData.statement.executeQuery(sqlMh);
                reMh.next();
                int machineCount = reMh.getInt(1);
                reMh.close();

                ResultSet reMg = AllData.statement.executeQuery(sqlMg);
                reMg.next();
                int manageCount = reMg.getInt(1);
                reMg.close();

                ResultSet reRequest = AllData.statement.executeQuery(sqlRe);
                reRequest.next();
                int requestCount = reRequest.getInt(1);
                reRequest.close();

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
                    key++;
                }
                if (dcCount!=AllData.docuterCode.size()){
                    AllData.readDocuter();
                    key++;
                }
                if (kpCount!=AllData.keeperCode.size()){
                    AllData.readKeeper();
                    key++;
                }
                if (dcWaitCount!=AllData.docuterWaitingCode.size()){
                    AllData.readDocuterWaiting();
                    key++;
                }
                if (kpWaitCount!=AllData.keeperWaitingCode.size()){
                    AllData.readKeeperWaiting();
                    key++;
                }
                if (requestCount!=AllData.requestCode.size()){
                    AllData.readRequest();
                    key++;
                }
                if (manageCount!=AllData.manageCode.size()){
                    AllData.readManage(hpCode);
                    key++;
                }
                if (machineCount!=AllData.machineCode.size()){
                    AllData.readMachine(hpCode);
                    key++;
                }

                if (key!=0){
                    if (AllData.hpStyle.equals("First")){
                        hospitalUI.changeFirst(hpCode,leftPanel,rightPanel);
                    } else if (AllData.hpStyle.equals("SetUp")) {
                        hospitalUI.changeSetUp(hpCode,leftPanel,rightPanel);
                    } else if (AllData.hpStyle.equals("SetHp")) {
                        hospitalUI.changeSetHp(hpCode,leftPanel,rightPanel);
                    }else if (AllData.hpStyle.equals("Request")) {
                        hospitalUI.changeRequest(hpCode,leftPanel,rightPanel);
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class hospitalUI extends JFrame {
    JPanel rightPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    public hospitalUI(String hpCode) throws HeadlessException, SQLException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Dimension screenSize = ge.getMaximumWindowBounds().getSize();

        setTitle("医院界面");
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice;
                choice = JOptionPane.showConfirmDialog(null, "您确定要关闭窗口吗？", "关闭确认", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        AllData.hospitalKey = false;
                        AllData.statement.close();
                        AllData.connection.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                }
            }
        });


        JMenuBar menuBar = new JMenuBar();
        JMenu homeMenu = new JMenu("首页");
        JMenu settingMenu = new JMenu("设置");
        JMenu viewMenu = new JMenu("查看");

        JMenuItem homeItem = new JMenuItem("打开首页");

        JMenuItem settingItem = new JMenuItem("医院信息");
        JMenuItem settingChangeItem = new JMenuItem("增添机器");

        JMenuItem viewItem = new JMenuItem("任职申请");


        leftPanel.setBounds(0,0,1920,1080);// 设置尺寸
        leftPanel.setBackground(Color.white); // 设置背景色为白色

        // 创建右侧 JPanel

        rightPanel.setBounds(0,0,300, 1080); // 设置尺寸
        rightPanel.setBackground(new Color(240,231,226));


        changeFirst(hpCode,leftPanel,rightPanel);


        homeItem.addActionListener(e -> {
            try {
                AllData.hpStyle = "First";
                changeFirst(hpCode, leftPanel,rightPanel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        settingItem.addActionListener(e -> {
            AllData.hpStyle = "SetUp";
            changeSetUp(hpCode,leftPanel,rightPanel);
        });

        settingChangeItem.addActionListener(e -> {
            AllData.hpStyle = "setHp";
            changeSetHp(hpCode,leftPanel,rightPanel);
        });

        viewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AllData.hpStyle = "Request";
                changeRequest(hpCode,leftPanel,rightPanel);
            }
        });

        add(rightPanel);
        add(leftPanel);

        homeMenu.add(homeItem);
        settingMenu.add(settingItem);
        settingMenu.add(settingChangeItem);
        viewMenu.add(viewItem);

        menuBar.add(homeMenu);
        menuBar.add(settingMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
        setLayout(null);
        setVisible(true);
    }

    public static void changeRequest(String hpCode,JPanel leftPanel,JPanel rightPanel) {
        leftPanel.removeAll();
        rightPanel.removeAll();

        leftPanel.setLayout(null);
        rightPanel.setLayout(null);

        rightPanel.setVisible(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        JScrollPane jScrollPane = new JScrollPane();
        DefaultListModel<Object> listModel = new DefaultListModel<>();
        JLabel jTotal = new JLabel("未办理任职申请");
        jTotal.setFont(new Font("SimSun",Font.BOLD,40));

        ArrayList<String> reCodes = new ArrayList<>();

        for (Map.Entry<String,request> entry:AllData.requestData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode) && entry.getValue().getReState().equals("未通过")){
                String job;
                String name;
                if (entry.getValue().getEmCode().charAt(1) == 'D'){
                    job = "医生";
                    name = AllData.docuterWaitingData.get(entry.getValue().getEmCode()).getDcName();
                }else{
                    job = "机器管理员";
                    name = AllData.keeperWaitingData.get(entry.getValue().getEmCode()).getKpName();
                }
                reCodes.add(entry.getKey());
                reCodes.add("");
                listModel.addElement("    申请人名称："+name+"    |    申请职位："+job+"    |    申请时间"+entry.getValue().getReTimeOn().format(formatter));
                listModel.addElement("");
            }
        }

        JList<Object> objectJList = new JList<>(listModel);

        ListCellRenderer<Object> renderer = new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    if (index%2 ==0){
                        label.setFont(label.getFont().deriveFont(20.0f)); // 设置字体大小
                        label.setPreferredSize(new Dimension(150, 100)); // 设置元素大小
                        label.setBackground(Color.LIGHT_GRAY);
                        if (isSelected) {
                            label.setBackground(Color.DARK_GRAY); // 设置选中时的背景色
                            label.setForeground(Color.WHITE); // 设置选中时的前景色（文字色）
                        } else {
                            label.setBackground(Color.LIGHT_GRAY); // 设置非选中时的背景色
                            label.setForeground(Color.BLACK); // 设置非选中时的前景色
                        }
                    }else {
                        label.setPreferredSize(new Dimension(150, 10));
                        label.setBackground(Color.white);
                        label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                    }
                }
                return c;
            }
        };

        UIManager.put("workerList.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        objectJList.setCellRenderer(renderer);

        objectJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectJList.setVisibleRowCount(5);
        jScrollPane.setViewportView(objectJList);

        objectJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount()>=2){
                    int index = objectJList.getSelectedIndex();
                    if (index >= 0 && index%2==0){
                        AllData.hpStyle = "Decision";
                        changeDecision(reCodes.get(index),leftPanel,rightPanel);
                    }
                }
            }

            private void changeDecision(String reCode, JPanel leftPanel, JPanel rightPanel) {
                leftPanel.removeAll();
                rightPanel.removeAll();

                rightPanel.setVisible(true);
                leftPanel.setLayout(null);
                rightPanel.setLayout(null);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                String pre_Email = null;
                String pre_Name = null;
                String pre_IDcard = null;
                String pre_Sex = null;
                String pre_Job = null;
                int pre_Age;

                if (AllData.requestData.get(reCode).getEmCode().startsWith("#D")){
                    pre_Email = AllData.docuterWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getDcEmial();
                    pre_Name = AllData.docuterWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getDcName();
                    pre_IDcard = AllData.docuterWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getDcIDcard();
                    pre_Sex = AllData.docuterWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getDcSex();
                    pre_Age = AllData.docuterWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getDcAge();
                    pre_Job = "医生";
                }else{
                    pre_Email = AllData.keeperWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getKpEmail();
                    pre_Name = AllData.keeperWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getKpName();
                    pre_IDcard = AllData.keeperWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getKpIDcard();
                    pre_Sex = AllData.keeperWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getKpSex();
                    pre_Age = AllData.keeperWaitingData.get(AllData.requestData.get(reCode).getEmCode()).getKpAge();
                    pre_Job = "机器管理员";
                }

                JLabel jEmail = new JLabel("申请账号邮箱：" + pre_Email);
                jEmail.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jName = new JLabel("申请人名字：" + pre_Name);
                jName.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jJob = new JLabel("申请职位：" + pre_Job);
                jJob.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jIDcard = new JLabel("申请人身份证号（处理后）：" + pre_IDcard.substring(0, 2) + "**********" + pre_IDcard.substring(12));
                jIDcard.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jSex = new JLabel("申请人性别：" + pre_Sex);
                jSex.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jAge = new JLabel("申请人年龄：" + pre_Age);
                jAge.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jTimeOn = new JLabel("申请时间：" + AllData.requestData.get(reCode).getReTimeOn().format(formatter));
                jTimeOn.setFont(new Font("SimSun",Font.BOLD,30));

                int x = 350;
                int y = 100;
                int yJ = 100;

                jEmail.setBounds(x,y,600,30);
                jName.setBounds(x,y+yJ,600,30);
                jJob.setBounds(x,y+yJ*2,600,30);
                jIDcard.setBounds(x,y+y*3,1000,30);
                jSex.setBounds(x,y+yJ*4,600,30);
                jAge.setBounds(x,y+yJ*5,600,30);
                jTimeOn.setBounds(x,y+yJ*6,600,30);

                JLabel jHpName = new JLabel(AllData.hospitalData.get(AllData.requestData.get(reCode).getHpCode()).getHp_name());
                jHpName.setFont(new Font("SimSun",Font.BOLD,20));

                JButton employButton = new JButton("录用");
                employButton.setFont(new Font("SimSun",Font.BOLD,20));

                JButton refuseButton = new JButton("拒绝");
                refuseButton.setFont(new Font("SimSun",Font.BOLD,20));

                JButton backButton = new JButton("返回");
                backButton.setFont(new Font("SimSun",Font.BOLD,20));

                int xr = 50;
                int yr = 100;
                int yJr = 100;

                jHpName.setBounds(xr+30,yr,600,20);
                employButton.setBounds(xr,yr+yJr,200,50);
                refuseButton.setBounds(xr,yr+yJr*2,200,50);
                backButton.setBounds(xr,yr+yJr*3,200,50);

                refuseButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int choice = 0;
                        choice = JOptionPane.showConfirmDialog(null, "您确定要拒绝录用他/她吗？", "关闭确认", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            String code = null;
                            PreparedStatement preparedOver = null;
                            try {
                                preparedOver = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
                                PreparedStatement preparedOut = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");

                                preparedOver.execute();
                                String emCode = AllData.requestData.get(reCode).getEmCode();

                                if (emCode.charAt(1) == 'D'){
                                    PreparedStatement preparedDw = AllData.connection.prepareStatement("DELETE FROM docuter_waiting where dw_code = ?");
                                    preparedDw.setString(1,emCode);
                                    preparedDw.execute();
                                    preparedDw.close();

                                    AllData.docuterWaitingCode.remove(emCode);
                                    AllData.docuterWaitingData.remove(emCode);
                                }else {
                                    PreparedStatement preparedKw = AllData.connection.prepareStatement("DELETE FROM keeper_waiting where kw_code = ?");
                                    preparedKw.setString(1,emCode);
                                    preparedKw.execute();
                                    preparedKw.close();

                                    AllData.keeperWaitingData.remove(emCode);
                                    AllData.keeperWaitingCode.remove(emCode);
                                }

                                PreparedStatement preparedRe = AllData.connection.prepareStatement("DELETE FROM request_employment where re_code = ?");
                                preparedRe.setString(1,reCode);
                                preparedRe.execute();
                                preparedRe.close();

                                AllData.requestData.remove(reCode);
                                AllData.requestCode.remove(reCode);

                                preparedOut.execute();

                                preparedOver.close();
                                preparedOut.close();

                                JOptionPane.showConfirmDialog(null, "拒绝成功", "关闭确认", JOptionPane.DEFAULT_OPTION);
                                changeRequest(hpCode,leftPanel,rightPanel);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
                });

                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changeRequest(hpCode,leftPanel,rightPanel);
                    }
                });

                employButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int choice = 0;
                        choice = JOptionPane.showConfirmDialog(null, "您确定要录用他/她吗？", "关闭确认", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            try {
                                String code = null;
                                PreparedStatement preparedOver = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0;");
                                PreparedStatement preparedOut = AllData.connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1;");

                                preparedOver.execute();
                                String emCode = AllData.requestData.get(reCode).getEmCode();

                                if (AllData.requestData.get(reCode).getEmCode().charAt(1) == 'D'){
                                    code = newDcCode();
                                    String dcHpcode = AllData.docuterWaitingData.get(emCode).getHpCode();
                                    String dcEmial = AllData.docuterWaitingData.get(emCode).getDcEmial();
                                    String dcPassword = AllData.docuterWaitingData.get(emCode).getDcPassword();
                                    String dcName = AllData.docuterWaitingData.get(emCode).getDcName();
                                    String dcID = AllData.docuterWaitingData.get(emCode).getDcID();
                                    String dcIDcard = AllData.docuterWaitingData.get(emCode).getDcIDcard();
                                    int dcAge = AllData.docuterWaitingData.get(emCode).getDcAge();
                                    String dcSex = AllData.docuterWaitingData.get(emCode).getDcSex();
                                    String dcPhone = AllData.docuterWaitingData.get(emCode).getDcPhone();

                                    PreparedStatement preparedDocuter = AllData.connection.prepareStatement("INSERT INTO docuter VALUES (?,?,?,?,?,?,?,?,?,?)");
                                    preparedDocuter.setString(1,code);
                                    preparedDocuter.setString(2,dcHpcode);
                                    preparedDocuter.setString(3,dcEmial);
                                    preparedDocuter.setString(4,dcPassword);
                                    preparedDocuter.setString(5,dcName);
                                    preparedDocuter.setString(6,dcID);
                                    preparedDocuter.setString(7,dcIDcard);
                                    preparedDocuter.setInt(8,dcAge);
                                    preparedDocuter.setString(9,dcSex);
                                    preparedDocuter.setString(10,dcPhone);

                                    preparedDocuter.executeUpdate();
                                    preparedDocuter.close();

                                    docuter docuter = new docuter(dcHpcode, dcEmial, dcPassword, dcName, dcID, dcIDcard, dcAge, dcSex, dcPhone);
                                    AllData.docuterData.put(code,docuter);
                                    AllData.docuterCode.add(code);
                                    AllData.requestData.get(reCode).setEmCode(code);

                                    AllData.docuterWaitingData.remove(emCode);
                                    AllData.docuterWaitingCode.remove(emCode);
                                }else {
                                    code = newKpCode();
                                    String kpHpCode = AllData.keeperWaitingData.get(emCode).getHpCode();
                                    String kpPassword = AllData.keeperWaitingData.get(emCode).getKpPassword();
                                    String kpEmail = AllData.keeperWaitingData.get(emCode).getKpEmail();
                                    String kpName = AllData.keeperWaitingData.get(emCode).getKpName();
                                    String kpIDcard = AllData.keeperWaitingData.get(emCode).getKpIDcard();
                                    int kpAge = AllData.keeperWaitingData.get(emCode).getKpAge();
                                    String kpSex = AllData.keeperWaitingData.get(emCode).getKpSex();

                                    PreparedStatement preparedKeeper = AllData.connection.prepareStatement("INSERT INTO keeper VALUES (?,?,?,?,?,?,?,?)");
                                    preparedKeeper.setString(1,code);
                                    preparedKeeper.setString(2,kpHpCode);
                                    preparedKeeper.setString(3,kpPassword);
                                    preparedKeeper.setString(4,kpEmail);
                                    preparedKeeper.setString(5,kpName);
                                    preparedKeeper.setString(6,kpIDcard);
                                    preparedKeeper.setInt(7,kpAge);
                                    preparedKeeper.setString(8,kpSex);

                                    preparedKeeper.executeUpdate();
                                    preparedKeeper.close();

                                    keeper keeper = new keeper(kpHpCode, kpPassword, kpEmail, kpName, kpIDcard, kpAge, kpSex);
                                    AllData.keeperData.put(code,keeper);
                                    AllData.keeperCode.add(code);

                                    AllData.keeperWaitingData.remove(code);
                                    AllData.keeperWaitingCode.remove(code);

                                }
                                PreparedStatement preparedRequest = AllData.connection.prepareStatement("UPDATE request_employment SET re_state = '通过' , re_timeOK = ? , em_code = ? where re_code = ?");
                                LocalDateTime now = LocalDateTime.now();
                                Timestamp timestamp = Timestamp.valueOf(now);
                                preparedRequest.setTimestamp(1,timestamp);
                                preparedRequest.setString(3,reCode);
                                preparedRequest.setString(2,code);

                                preparedRequest.executeUpdate();
                                preparedRequest.close();

                                preparedOut.execute();
                                preparedOver.close();
                                preparedOut.close();

                                AllData.requestData.get(reCode).setReTimeOk(now);
                                AllData.requestData.get(reCode).setReState("通过");
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                            JOptionPane.showConfirmDialog(null, "录用成功？", "关闭确认", JOptionPane.DEFAULT_OPTION);
                            changeRequest(hpCode,leftPanel,rightPanel);
                        }
                    }

                    private String newKpCode() {
                        if (AllData.keeperData.isEmpty()){
                            return "#K1";
                        }else {
                            int size = AllData.keeperCode.size();
                            String string = AllData.keeperCode.get(size - 1);
                            return "#K" + (Integer.parseInt(string.substring(2))+1);
                        }
                    }

                    private String newDcCode() {
                        if (AllData.docuterData.isEmpty()){
                            return "#D1";
                        }else {
                            int size = AllData.docuterCode.size();
                            String string = AllData.docuterCode.get(size - 1);
                            return "#D" + (Integer.parseInt(string.substring(2)) + 1);
                        }
                    }
                });

                rightPanel.add(jHpName);
                rightPanel.add(employButton);
                rightPanel.add(refuseButton);
                rightPanel.add(backButton);

                leftPanel.add(jTimeOn);
                leftPanel.add(jAge);
                leftPanel.add(jName);
                leftPanel.add(jJob);
                leftPanel.add(jSex);
                leftPanel.add(jEmail);
                leftPanel.add(jIDcard);

                leftPanel.revalidate();
                leftPanel.repaint();
                rightPanel.revalidate();
                rightPanel.repaint();

            }
        });

        jTotal.setBounds(30,20,500,50);
        jScrollPane.setBounds(0,80,1710,790);

        leftPanel.add(jScrollPane);
        leftPanel.add(jTotal);

        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public static void changeSetHp(String hpCode, JPanel leftPanel,JPanel rightPanel) {
        leftPanel.removeAll();
        rightPanel.removeAll();

        leftPanel.setLayout(null);
        rightPanel.setLayout(null);

        rightPanel.setVisible(true);

        JLabel jName = new JLabel(AllData.hospitalData.get(hpCode).getHp_name());
        jName.setFont(new Font("黑体",Font.BOLD,25));
        jName.setBounds(55,60,200,25);

        int requestSize = 0;
        for (Map.Entry<String,request> entry:AllData.requestData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode) && entry.getValue().getReState().equals("未通过")){
                requestSize++;
            }
        }

        LocalDate currentDate = LocalDate.now();

        int manageSize = 0;
        for (Map.Entry<String,manage> entry:AllData.manageData.entrySet()){
            if (entry.getValue().getMgTime().toLocalTime().equals(currentDate) && AllData.keeperData.get(entry.getValue().getKpCode()).getHpCode().equals(hpCode)){
                manageSize++;
            }
        }

        JLabel jRequestNumber = new JLabel("未办理任职申请数量：" + requestSize);
        jRequestNumber.setFont(new Font("SimSun",Font.BOLD,20));
        jRequestNumber.setBounds(20,150,400,25);

        JLabel jManageNumber = new JLabel("今日的管理员操作次数：" + manageSize);
        jManageNumber.setFont(new Font("SimSun",Font.BOLD,20));
        jManageNumber.setBounds(20,200,400,25);


        rightPanel.add(jName);
        rightPanel.add(jManageNumber);
        rightPanel.add(jRequestNumber);

        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    //医院信息
    public static void changeSetUp(String hpCode,JPanel leftPanel,JPanel rightPanel) {
        leftPanel.removeAll();
        rightPanel.removeAll();

        leftPanel.setLayout(null);
        rightPanel.setLayout(null);

        rightPanel.setVisible(true);


        JLabel jName = new JLabel(AllData.hospitalData.get(hpCode).getHp_name());
        jName.setFont(new Font("黑体",Font.BOLD,25));
        jName.setBounds(55,60,200,25);

        int requestSize = 0;
        for (Map.Entry<String,request> entry:AllData.requestData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode) && entry.getValue().getReState().equals("未通过")){
                requestSize++;
            }
        }

        LocalDate currentDate = LocalDate.now();

        int manageSize = 0;
        for (Map.Entry<String,manage> entry:AllData.manageData.entrySet()){
            if (entry.getValue().getMgTime().toLocalTime().equals(currentDate) && AllData.keeperData.get(entry.getValue().getKpCode()).getHpCode().equals(hpCode)){
                manageSize++;
            }
        }

        int docuterSize = 0;
        for (Map.Entry<String,docuter> entry:AllData.docuterData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode)){
                docuterSize++;
            }
        }

        int keeperSize = 0;
        for (Map.Entry<String,keeper> entry:AllData.keeperData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode)){
                keeperSize++;
            }
        }

        int machineSize = 0;
        for (Map.Entry<String,machine> entry:AllData.machineData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode)){
                machineSize++;
            }
        }

        JLabel jRequestNumber = new JLabel("未办理任职申请数量：" + requestSize);
        jRequestNumber.setFont(new Font("SimSun",Font.BOLD,20));
        jRequestNumber.setBounds(20,150,400,25);

        JLabel jManageNumber = new JLabel("今日的管理员操作次数：" + manageSize);
        jManageNumber.setFont(new Font("SimSun",Font.BOLD,20));
        jManageNumber.setBounds(20,200,400,25);

        JLabel jHpID = new JLabel("医院ID：" + hpCode);
        jHpID.setFont(new Font("SimSun",Font.BOLD,30));
        jHpID.setBounds(370,80,600,30);

        JLabel jEmail = new JLabel("医院账号注册邮箱：" + AllData.hospitalData.get(hpCode).getHp_email());
        jEmail.setFont(new Font("SimSun",Font.BOLD,30));
        jEmail.setBounds(370,180,600,30);

        JLabel jLegalPeron = new JLabel("医院法人：" + AllData.hospitalData.get(hpCode).getHp_legal_person());
        jLegalPeron.setFont(new Font("SimSun",Font.BOLD,30));
        jLegalPeron.setBounds(370,280,600,30);

        JLabel jLicense = new JLabel("医疗机构执业许可证编号：" + AllData.hospitalData.get(hpCode).getHp_license());
        jLicense.setFont(new Font("SimSun",Font.BOLD,30));
        jLicense.setBounds(370,380,1000,40);

        JLabel jBusLi = new JLabel("营业执照编号：" + AllData.hospitalData.get(hpCode).getHp_busLi());
        jBusLi.setFont(new Font("SimSun",Font.BOLD,30));
        jBusLi.setBounds(370,480,700,40);

        JLabel jDocuterNumber = new JLabel("在职医生人数：" + docuterSize);
        jDocuterNumber.setFont(new Font("SimSun",Font.BOLD,30));
        jDocuterNumber.setBounds(370,580,400,40);

        JLabel jKeeperNumber = new JLabel("在职机器管理员人数：" + keeperSize);
        jKeeperNumber.setFont(new Font("SimSun",Font.BOLD,30));
        jKeeperNumber.setBounds(370,680,400,40);

        JLabel jMachineNumber = new JLabel("所属机器数量：" + machineSize);
        jMachineNumber.setFont(new Font("SimSun",Font.BOLD,30));
        jMachineNumber.setBounds(360,780,400,40);

        leftPanel.add(jDocuterNumber);
        leftPanel.add(jKeeperNumber);
        leftPanel.add(jMachineNumber);
        leftPanel.add(jLegalPeron);
        leftPanel.add(jLicense);
        leftPanel.add(jEmail);
        leftPanel.add(jBusLi);
        leftPanel.add(jHpID);
        rightPanel.add(jManageNumber);
        rightPanel.add(jRequestNumber);
        rightPanel.add(jName);

       leftPanel.revalidate();
       leftPanel.repaint();
       rightPanel.revalidate();
       rightPanel.repaint();
    }

    //打开首页
    public static void changeFirst(String hpCode, JPanel leftPanel,JPanel rightPanel) throws SQLException {
        leftPanel.removeAll();
        rightPanel.removeAll();

        leftPanel.setLayout(null);
        rightPanel.setLayout(null);

        rightPanel.setVisible(false);

        JLabel jTotal = new JLabel("天翼医疗系统---医院界面");
        jTotal.setFont(new Font("SimSun",Font.BOLD,80));
        jTotal.setBounds(350,200,1000,100);

        int docuterSize = 0;
        for (Map.Entry<String,docuter> entry:AllData.docuterData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode)){
                docuterSize++;
            }
        }

        int keeperSize = 0;
        for (Map.Entry<String,keeper> entry:AllData.keeperData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode)){
                keeperSize++;
            }
        }

        int machineSize = 0;
        for (Map.Entry<String,machine> entry:AllData.machineData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode)){
                machineSize++;
            }
        }

        int requestSize = 0;
        for (Map.Entry<String,request> entry:AllData.requestData.entrySet()){
            if (entry.getValue().getHpCode().equals(hpCode) && entry.getValue().getReState().equals("未通过")){
                requestSize++;
            }
        }

        LocalDate currentDate = LocalDate.now();

        int manageSize = 0;
        for (Map.Entry<String,manage> entry:AllData.manageData.entrySet()){
            if (entry.getValue().getMgTime().toLocalTime().equals(currentDate) && AllData.keeperData.get(entry.getValue().getKpCode()).getHpCode().equals(hpCode)){
                manageSize++;
            }
        }

        JLabel jDocuterNumber = new JLabel("在职医生人数：" + docuterSize);
        jDocuterNumber.setFont(new Font("SimSun",Font.BOLD,25));
        jDocuterNumber.setBounds(270,500,400,30);

        JLabel jKeeperNumber = new JLabel("在职机器管理员人数：" + keeperSize);
        jKeeperNumber.setFont(new Font("SimSun",Font.BOLD,25));
        jKeeperNumber.setBounds(630,500,400,30);

        JLabel jMachineNumber = new JLabel("所属机器数量：" + machineSize);
        jMachineNumber.setFont(new Font("SimSun",Font.BOLD,25));
        jMachineNumber.setBounds(1100,500,400,30);

        JLabel jDrugNumber = new JLabel("未办理任职申请数量：" + requestSize);
        jDrugNumber.setFont(new Font("SimSun",Font.BOLD,25));
        jDrugNumber.setBounds(400,600,400,30);

        JLabel jManage = new JLabel("今日的管理员操作次数：" + manageSize);
        jManage.setFont(new Font("SimSun",Font.BOLD,25));
        jManage.setBounds(830,600,400,30);

        leftPanel.add(jManage);
        leftPanel.add(jDrugNumber);
        leftPanel.add(jDocuterNumber);
        leftPanel.add(jKeeperNumber);
        leftPanel.add(jMachineNumber);
        leftPanel.add(jTotal);

        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

}
