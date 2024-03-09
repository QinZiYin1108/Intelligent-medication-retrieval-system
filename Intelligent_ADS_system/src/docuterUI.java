import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

class dcListening implements Runnable{

    String dcCode;
    JPanel leftPanel;
    JPanel rightPanel;

    public dcListening(String dcCode, JPanel leftPanel, JPanel rightPanel) {
        this.dcCode = dcCode;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
    }

    @Override
    public void run() {
        String sqlMh = "SELECT COUNT(*) FROM machine WHERE hp_code = ?";
        String sqlUser = "SELECT COUNT(*) from users";
        String sqlDrug = "SELECT COUNT(*) FROM drug";
        String sqlRe = "SELECT COUNT(*) from record where dc_code = ?";
        String sqlRd = "SELECT count(*) FROM record_drug where re_code IN (SELECT re_code FROM record where dc_code = ?)";

        String hpCode = AllData.docuterData.get(dcCode).getHpCode();

        while (AllData.docuterKey){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                PreparedStatement preparedRe = AllData.connection.prepareStatement(sqlRe);
                PreparedStatement preparedRd = AllData.connection.prepareStatement(sqlRd);
                PreparedStatement preparedMh = AllData.connection.prepareStatement(sqlMh);
                PreparedStatement preparedUser = AllData.connection.prepareStatement(sqlUser);
                PreparedStatement preparedDrug = AllData.connection.prepareStatement(sqlDrug);

                preparedRd.setString(1,dcCode);
                ResultSet reRd = preparedRd.executeQuery();
                reRd.next();
                int rdCount = reRd.getInt(1);
                reRd.close();
                preparedRd.close();

                preparedRe.setString(1,dcCode);
                ResultSet reRe = preparedRe.executeQuery();
                reRe.next();
                int reCount = reRe.getInt(1);
                reRe.close();
                preparedRe.close();

                preparedMh.setString(1,hpCode);
                ResultSet reMh = preparedMh.executeQuery();
                reMh.next();
                int mhCount = reMh.getInt(1);
                reMh.close();
                preparedMh.close();

                ResultSet reDrug = preparedDrug.executeQuery();
                reDrug.next();
                int drugCount = reDrug.getInt(1);
                reDrug.close();
                preparedDrug.close();

                ResultSet reUser = preparedUser.executeQuery();
                reUser.next();
                int userCount = reUser.getInt(1);
                reUser.close();
                preparedUser.close();

                if (mhCount!=AllData.machineCode.size()){
                    AllData.readMachine(hpCode);
                }
                if (drugCount!=AllData.drugCode.size()){
                    AllData.readDrug();
                }
                if (userCount!=AllData.userCode.size()){
                    AllData.readUser();
                }
                if (reCount!=AllData.recordCode.size()){
                    AllData.readRecord(dcCode);
                }
                if (rdCount!=AllData.recordDrugCode.size()){
                    AllData.readRecordDrug(dcCode);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class docuterUI extends JFrame {
    int bottomIndex = -1;
    String userCodeZon = "";
    ArrayList<String> dgCodesZon;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Dimension screenSize = ge.getMaximumWindowBounds().getSize();
    boolean drawLines = false;
    JPanel rightPanel = new JPanel();
    JPanel leftPanel = new JPanel(){
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            if (drawLines){
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1));

                g2d.drawLine(0,50,1620,50);
                g2d.drawLine(250,50,250,400);
                g2d.drawLine(0,400,1620,400);

            }
        }
    };

    public docuterUI(String dcCode) throws HeadlessException {

        setTitle("医生界面");
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
                        AllData.docuterKey = false;
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

        JMenuItem homeItem = new JMenuItem("新开药单");

        JMenuItem settingItem = new JMenuItem("个人信息");
        JMenuItem settingChangeItem = new JMenuItem("修改信息");

        JMenuItem viewItem = new JMenuItem("药品信息");
        JMenuItem oldItem = new JMenuItem("历史药单");


        leftPanel.setBounds(300,0,1620,1080);// 设置尺寸
        leftPanel.setBackground(Color.white); // 设置背景色为白色

        // 创建右侧 JPanel

        rightPanel.setBounds(0,0,300, 1080); // 设置尺寸
        rightPanel.setBackground(new Color(240,231,226));

        changeFirstDc(dcCode,leftPanel,rightPanel);

        homeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeFirstDc(dcCode,leftPanel,rightPanel);
            }
        });

        add(rightPanel);
        add(leftPanel);

        homeMenu.add(homeItem);
        settingMenu.add(settingItem);
        settingMenu.add(settingChangeItem);
        viewMenu.add(viewItem);
        viewMenu.add(oldItem);

        menuBar.add(homeMenu);
        menuBar.add(settingMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        setLayout(null);
        setVisible(true);
    }

    private void changeFirstDc(String dcCode, JPanel leftPanel, JPanel rightPanel) {
        leftPanel.removeAll();
        rightPanel.removeAll();

        leftPanel.setLayout(null);
        rightPanel.setLayout(null);

        drawLines = false;

        JLabel jHpName = new JLabel(AllData.hospitalData.get(AllData.docuterData.get(dcCode).getHpCode()).getHp_name());
        jHpName.setFont(new Font("SimSun",Font.BOLD,25));

        JLabel jDcName = new JLabel(AllData.docuterData.get(dcCode).getDcName());
        jDcName.setFont(new Font("SimSun",Font.BOLD,25));

        JButton addDrugButton = new JButton("添加药单");
        addDrugButton.setFont(new Font("SimSun",Font.BOLD,30));

        addDrugButton.setEnabled(true);

        jHpName.setBounds(50,100,600,30);
        jDcName.setBounds(90,170,600,30);
        addDrugButton.setBounds(30,250,200,50);

        addDrugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPanel.removeAll();

                addDrugButton.setEnabled(false);

                drawLines = true;

                JButton jaddDrugButton = new JButton("添加药品");
                jaddDrugButton.setFont(new Font("SimSun",Font.BOLD,13));
                JButton deleteDrugButton = new JButton("删除药品");
                deleteDrugButton.setFont(new Font("SimSun",Font.BOLD,13));
                JButton overDrugButton = new JButton("完成");
                overDrugButton.setFont(new Font("SimSun",Font.BOLD,13));
                JButton abandonDrugButton = new JButton("放弃药单");
                abandonDrugButton.setFont(new Font("SimSun",Font.BOLD,13));

                jaddDrugButton.setBounds(20,10,100,30);
                deleteDrugButton.setBounds(170,10,100,30);
                overDrugButton.setBounds(320,10,100,30);
                abandonDrugButton.setBounds(470,10,100,30);

                JLabel jUserId = new JLabel("患者ID");
                jUserId.setFont(new Font("SimSun",Font.BOLD,30));

                JTextField jUserIdText = new JTextField(10);
                jUserIdText.setFont(new Font("SimSun",Font.BOLD,30));

                JButton overUserButton = new JButton("查询");
                overUserButton.setFont(new Font("SimSun",Font.BOLD,30));

                JLabel jUserName = new JLabel();
                jUserName.setFont(new Font("SimSun",Font.BOLD,25));

                JLabel jUserAge = new JLabel();
                jUserAge.setFont(new Font("SimSun",Font.BOLD,25));

                JLabel jUserSex = new JLabel();
                jUserSex.setFont(new Font("SimSun",Font.BOLD,25));

                JLabel jUserPhone = new JLabel();
                jUserPhone.setFont(new Font("SimSun",Font.BOLD,25));

                DefaultListModel<Object> listModel = new DefaultListModel<>();
                JList<Object> list = new JList<>(listModel);
                JScrollPane jScrollPane = new JScrollPane(list);
                dgCodesZon = new ArrayList<>();

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
                list.setCellRenderer(renderer);

                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setVisibleRowCount(5);


                jUserId.setBounds(70,120,200,30);
                jUserIdText.setBounds(30,190,180,36);
                overUserButton.setBounds(70,270,100,36);

                jUserName.setBounds(300,150,300,30);
                jUserPhone.setBounds(600,150,600,30);
                jUserAge.setBounds(300,250,300,30);
                jUserSex.setBounds(600,250,300,30);

                jScrollPane.setBounds(-1,400,1410,470);

                jaddDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!userCodeZon.isEmpty()){

                            int windowWidth = 350;
                            int windowHeight = 250;

                            JDialog jfAddDrug = new JDialog(docuterUI.this,"添加药品",true);
                            jfAddDrug.setSize(windowWidth,windowHeight);
                            jfAddDrug.setLayout(null);

                            int screenWidth = screenSize.width;
                            int screenHeight = screenSize.height;

                            int x = (screenWidth-windowWidth) / 2;
                            int y = (screenHeight-windowHeight) / 2;

                            jfAddDrug.setLocation(x,y);

                            jfAddDrug.setAlwaysOnTop(true);
                            jfAddDrug.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

                            JButton overDrugLog = new JButton("确定");
                            overDrugLog.setFont(new Font("SimSun",Font.BOLD,20));

                            JLabel nameDrugLog = new JLabel("药品名称");
                            nameDrugLog.setFont(new Font("SimSun",Font.BOLD,17));

                            JLabel typeDrugLog = new JLabel("药品种类");
                            typeDrugLog.setFont(new Font("SimSun",Font.BOLD,17));

                            JLabel numberDrugLog = new JLabel("药品数量");
                            numberDrugLog.setFont(new Font("SimSun",Font.BOLD,17));

                            JTextField nameDrugText = new JTextField(20);
                            nameDrugText.setFont(new Font("SimSun",Font.BOLD,17));

                            JTextField numberDrugText = new JTextField(10);
                            numberDrugText.setFont(new Font("SimSun",Font.BOLD,17));

                            JComboBox<String> nameDrugComboBox = new JComboBox<>();

                            ArrayList<String> dgNames = new ArrayList<>();
                            ArrayList<String> dgCodes = new ArrayList<>();

                            for (String mdCode : AllData.machineCode){
                                for (Map.Entry<String,Integer> entry : AllData.machineData.get(mdCode).getMachineDrug().entrySet()){
                                    dgCodes.add(entry.getKey());
                                    dgNames.add(AllData.drugData.get(entry.getKey()).getDgName());
                                    nameDrugComboBox.addItem(AllData.drugData.get(entry.getKey()).getDgName());
                                }
                            }

                            String selectedItem = (String) nameDrugComboBox.getSelectedItem();
                            int index = dgNames.indexOf(selectedItem);

                            JLabel typeDrug = new JLabel(AllData.drugData.get(dgCodes.get(index)).dgType);
                            typeDrug.setFont(new Font("SimSun",Font.BOLD,17));


                            nameDrugComboBox.addItemListener(new ItemListener() {
                                @Override
                                public void itemStateChanged(ItemEvent e) {
                                    if (e.getStateChange() == ItemEvent.SELECTED) {
                                        int index = nameDrugComboBox.getSelectedIndex();
                                        typeDrug.setText(AllData.drugData.get(dgCodes.get(index)).getDgType());
                                    }
                                }
                            });

                            nameDrugText.getDocument().addDocumentListener(new DocumentListener() {
                                @Override
                                public void insertUpdate(DocumentEvent e) {
                                    updateSuggestions();
                                }

                                @Override
                                public void removeUpdate(DocumentEvent e) {
                                    updateSuggestions();
                                }

                                @Override
                                public void changedUpdate(DocumentEvent e) {
                                    updateSuggestions();
                                }

                                private void updateSuggestions() {
                                    String input = nameDrugText.getText();
                                    nameDrugComboBox.removeAllItems();
                                    for (String option : dgNames) {
                                        if (option.startsWith(input)) {
                                            nameDrugComboBox.addItem(option);
                                        }
                                    }
                                    nameDrugComboBox.setPopupVisible(true);
                                }
                            });

                            overDrugLog.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String text = numberDrugText.getText();
                                    if (isNumber(text)){
                                        int number = Integer.parseInt(text);
                                        String selectedItem = (String) nameDrugComboBox.getSelectedItem();
                                        int index = dgNames.indexOf(selectedItem);
                                        String dgCode = dgCodes.get(index);
                                        dgCodesZon.add(dgCode);
                                        listModel.addElement("  药品名称："+AllData.drugData.get(dgCode).getDgName()+"   |   药品种类："+AllData.drugData.get(dgCode).getDgType()+"   |   添加数量："+number);
                                        listModel.addElement("");
                                        list.updateUI();
                                        jfAddDrug.setVisible(false);
                                        jfAddDrug.dispose();
                                        JOptionPane.showConfirmDialog(null, "添加成功", "关闭确认", JOptionPane.DEFAULT_OPTION);

                                    }else {
                                        jfAddDrug.setVisible(false);
                                        jfAddDrug.dispose();
                                        JOptionPane.showConfirmDialog(null, "药品数量只能填写阿拉伯数字", "关闭确认", JOptionPane.DEFAULT_OPTION);
                                    }
                                }

                                private boolean isNumber(String text) {
                                    for (int i = 0; i < text.length(); i++) {
                                        if (!Character.isDigit(text.charAt(i))) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                            });

                            nameDrugLog.setBounds(10,30,140,20);
                            typeDrugLog.setBounds(170,30,100,20);
                            numberDrugLog.setBounds(260,30,100,20);

                            nameDrugText.setBounds(10,60,140,25);
                            typeDrug.setBounds(160,60,100,25);
                            numberDrugText.setBounds(270,60,50,25);

                            nameDrugComboBox.setBounds(10,100,140,25);

                            overDrugLog.setBounds(110,150,100,40);

                            jfAddDrug.add(overDrugLog);
                            jfAddDrug.add(nameDrugLog);
                            jfAddDrug.add(typeDrugLog);
                            jfAddDrug.add(numberDrugLog);
                            jfAddDrug.add(nameDrugText);
                            jfAddDrug.add(numberDrugText);

                            jfAddDrug.add(nameDrugComboBox);
                            jfAddDrug.add(typeDrug);


                            jfAddDrug.setVisible(true);
                        }else {
                            JOptionPane.showConfirmDialog(null, "请先确认患者", "关闭确认", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                });

                list.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getClickCount()>=1){
                            int index = list.getSelectedIndex();
                            bottomIndex = index;
                        }
                    }
                });

                deleteDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            listModel.remove(bottomIndex);
                            listModel.remove(bottomIndex);
                            dgCodesZon.remove(bottomIndex/2);
                            list.updateUI();
                        }catch (ArrayIndexOutOfBoundsException e1){
                            System.out.println("No");
                        }
                    }
                });

                overDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int choice;
                        choice = JOptionPane.showConfirmDialog(null, "您确定要提交该药单吗？", "关闭确认", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            LocalDateTime now = LocalDateTime.now();
                            Timestamp timestamp = Timestamp.valueOf(now);

                            String reCode = newReCode();
                            String sqlRe = "INSERT INTO record VALUES (?,?,?,?,?)";
                            String sqlRd = "INSERT INTO record_drug VALUES (?,?,?,?,?)";
                            try {
                                PreparedStatement pre1 = AllData.connection.prepareStatement(sqlRe);
                                pre1.setString(1,reCode);
                                pre1.setString(2,dcCode);
                                pre1.setString(3,userCodeZon);
                                pre1.setTimestamp(4,timestamp);
                                pre1.setString(5,"未支付");

                                pre1.executeUpdate();

                                record record = new record(dcCode, userCodeZon, timestamp);
                                AllData.recordData.put(reCode,record);
                                AllData.recordCode.add(reCode);

                                for (int i=0;i<listModel.getSize();i+=2){
                                    String rdCode = newRdCode();
                                    System.out.println(dgCodesZon.toString());
                                    String dgCode = dgCodesZon.get(i/2);
                                    String elementAt = String.valueOf(listModel.getElementAt(i));
                                    int add = Integer.parseInt(elementAt.split("添加数量：")[1]);

                                    PreparedStatement pre2 = AllData.connection.prepareStatement(sqlRd);
                                    pre2.setString(1,rdCode);
                                    pre2.setString(2,reCode);
                                    pre2.setString(3,dgCode);
                                    pre2.setInt(4,add);
                                    pre2.setString(5,"未付款");

                                    pre2.executeUpdate();

                                    recordDrug recordDrug = new recordDrug(reCode, dgCode, add);
                                    AllData.recordDrugData.put(rdCode,recordDrug);
                                    AllData.recordDrugCode.add(rdCode);

                                    pre2.close();
                                }

                                pre1.close();

                                JOptionPane.showConfirmDialog(null, "提交成功", "关闭确认", JOptionPane.DEFAULT_OPTION);
                                changeFirstDc(dcCode,leftPanel,rightPanel);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }

                    private String newRdCode() {
                        if (AllData.recordDrugCode.isEmpty()){
                            return "#RD"+userCodeZon+"1";
                        }else {
                            int size = AllData.recordDrugCode.size();
                            String string = AllData.recordDrugCode.get(size - 1);
                            return "#RD"+userCodeZon + (Integer.parseInt(string.substring(6))+1);
                        }
                    }

                    private String newReCode() {
                        if (AllData.recordCode.isEmpty()){
                            return "#RE"+userCodeZon+1;
                        }else {
                            int size = AllData.recordCode.size();
                            String string = AllData.recordCode.get(size - 1);
                            int i = Integer.parseInt(string.substring(6));
                            System.out.println(i);
                            return "#RE" +userCodeZon+ (i+1);
                        }
                    }
                });

                abandonDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changeFirstDc(dcCode,leftPanel,rightPanel);
                    }
                });

                overUserButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String userCode = jUserIdText.getText();
                            jUserName.setText("名称："+AllData.userData.get(userCode).getUserName());
                            jUserAge.setText("年龄："+ AllData.userData.get(userCode).getUserAge());
                            jUserSex.setText("性别："+AllData.userData.get(userCode).getUserSex());
                            jUserPhone.setText("联系方式："+AllData.userData.get(userCode).getUserPhone());

                            jUserName.repaint();
                            jUserAge.repaint();
                            jUserSex.repaint();
                            jUserPhone.repaint();

                            userCodeZon = userCode;

                            JOptionPane.showConfirmDialog(null, "查询成功", "关闭确认", JOptionPane.DEFAULT_OPTION);
                        }catch (NullPointerException e1){
                            jUserName.setText("");
                            jUserAge.setText("");
                            jUserSex.setText("");
                            jUserPhone.setText("");

                            jUserName.repaint();
                            jUserAge.repaint();
                            jUserSex.repaint();
                            jUserPhone.repaint();

                            JOptionPane.showConfirmDialog(null, "未找到该用户", "关闭确认", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                });

                leftPanel.add(jScrollPane);

                leftPanel.add(jUserName);
                leftPanel.add(jUserAge);
                leftPanel.add(jUserSex);
                leftPanel.add(jUserPhone);

                leftPanel.add(jUserId);
                leftPanel.add(jUserIdText);
                leftPanel.add(overUserButton);

                leftPanel.add(jaddDrugButton);
                leftPanel.add(deleteDrugButton);
                leftPanel.add(overDrugButton);
                leftPanel.add(abandonDrugButton);

                leftPanel.revalidate();
                leftPanel.repaint();

            }
        });

        rightPanel.add(jHpName);
        rightPanel.add(jDcName);
        rightPanel.add(addDrugButton);

        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}