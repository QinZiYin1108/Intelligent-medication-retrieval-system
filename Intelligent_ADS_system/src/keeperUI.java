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
import java.util.HashMap;
import java.util.Map;

class kpListening implements Runnable{
    String kpCode;
    JPanel leftPanel;
    JPanel rightPanel;

    public kpListening(String kpCode, JPanel leftPanel, JPanel rightPanel) {
        this.kpCode = kpCode;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
    }

    @Override
    public void run() {
        String sqlDrug = "SELECT COUNT(*) FROM drug";
        String sqlMg = "SELECT COUNT(*) FROM manage WHERE kp_code IN (SELECT kp_code FROM keeper WHERE hp_code = ?)";
        String sqlMh = "SELECT COUNT(*) FROM machine WHERE hp_code = ?";

        String hpCode = AllData.keeperData.get(kpCode).getHpCode();

        while (AllData.keeperKey){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                PreparedStatement preparedMh = AllData.connection.prepareStatement(sqlMh);
                PreparedStatement preparedMg = AllData.connection.prepareStatement(sqlMg);
                PreparedStatement preparedDrug = AllData.connection.prepareStatement(sqlDrug);

                preparedMg.setString(1,hpCode);
                ResultSet reMg = preparedMg.executeQuery();
                reMg.next();
                int mgCount = reMg.getInt(1);

                preparedMh.setString(1,hpCode);
                ResultSet reMh = preparedMh.executeQuery();
                reMh.next();
                int mhCount = reMh.getInt(1);

                ResultSet reDrug = preparedDrug.executeQuery();
                reDrug.next();
                int drugCount = reDrug.getInt(1);

                if (mgCount!=AllData.manageCode.size()){
                    AllData.readManage(hpCode);
                }
                if (mhCount!=AllData.machineCode.size()){
                    AllData.readMachine(hpCode);
                }
                if (drugCount!=AllData.drugCode.size()){
                    AllData.readDrug();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

public class keeperUI extends JFrame {
    ArrayList<String> dgCodesZon = new ArrayList<>();
    int bottomIndex = -1;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Dimension screenSize = ge.getMaximumWindowBounds().getSize();

    boolean drawLines = false;

    String mhCodeZon = "";

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

    public keeperUI(String kpCode){
        setTitle("管理员界面");
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
                        AllData.keeperKey = false;
                        AllData.statement.close();
                        AllData.connection.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                }
            }
        });

        leftPanel.setBounds(300,0,1620,1080);// 设置尺寸
        leftPanel.setBackground(Color.white); // 设置背景色为白色

        // 创建右侧 JPanel

        rightPanel.setBounds(0,0,300, 1080); // 设置尺寸
        rightPanel.setBackground(new Color(240,231,226));

        JMenuBar menuBar = new JMenuBar();
        JMenu homeMenu = new JMenu("首页");
        JMenu settingMenu = new JMenu("设置");
        JMenu viewMenu = new JMenu("查看");

        JMenuItem homeItem = new JMenuItem("增添药品");

        JMenuItem settingItem = new JMenuItem("个人信息");
        JMenuItem settingChangeItem = new JMenuItem("修改信息");

        JMenuItem viewItem = new JMenuItem("机器信息");

        changFirst(kpCode,leftPanel,rightPanel);

        homeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changFirst(kpCode,leftPanel,rightPanel);
            }
        });

        homeMenu.add(homeItem);
        settingMenu.add(settingItem);
        settingMenu.add(settingChangeItem);
        viewMenu.add(viewItem);

        menuBar.add(homeMenu);
        menuBar.add(settingMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        add(leftPanel);
        add(rightPanel);

        setLayout(null);
        setVisible(true);
    }

    private void changFirst(String kpCode, JPanel leftPanel, JPanel rightPanel) {
        leftPanel.removeAll();
        rightPanel.removeAll();

        leftPanel.setLayout(null);
        rightPanel.setLayout(null);

        drawLines = false;

        JLabel jHpName = new JLabel(AllData.hospitalData.get(AllData.keeperData.get(kpCode).getHpCode()).getHp_name());
        jHpName.setFont(new Font("SimSun",Font.BOLD,25));

        JLabel jKpName = new JLabel(AllData.keeperData.get(kpCode).getKpName());
        jKpName.setFont(new Font("SimSun",Font.BOLD,25));

        JButton addDrugButton = new JButton("添加记录");
        addDrugButton.setFont(new Font("SimSun",Font.BOLD,30));

        addDrugButton.setEnabled(true);

        jHpName.setBounds(50,100,600,30);
        jKpName.setBounds(90,170,600,30);
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
                JButton abandonDrugButton = new JButton("放弃记录");
                abandonDrugButton.setFont(new Font("SimSun",Font.BOLD,13));

                jaddDrugButton.setBounds(20,10,100,30);
                deleteDrugButton.setBounds(170,10,100,30);
                overDrugButton.setBounds(320,10,100,30);
                abandonDrugButton.setBounds(470,10,100,30);

                JLabel jUserId = new JLabel("机器ID");
                jUserId.setFont(new Font("SimSun",Font.BOLD,30));

                JTextField jUserIdText = new JTextField(10);
                jUserIdText.setFont(new Font("SimSun",Font.BOLD,30));

                JButton overUserButton = new JButton("查询");
                overUserButton.setFont(new Font("SimSun",Font.BOLD,30));

                DefaultListModel<Object> listModel = new DefaultListModel<>();
                JList<Object> list = new JList<>(listModel);
                JScrollPane jScrollPane = new JScrollPane(list);

                DefaultListModel<Object> listModelTop = new DefaultListModel<>();
                JList<Object> listTop = new JList<>(listModelTop);
                JScrollPane jScrollPaneTop = new JScrollPane(listTop);

                ListCellRenderer<Object> rendererTop = new DefaultListCellRenderer(){
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (c instanceof JLabel) {
                            JLabel label = (JLabel) c;
                            if (index%2 ==0){
                                label.setFont(label.getFont().deriveFont(15)); // 设置字体大小
                                label.setPreferredSize(new Dimension(150, 30)); // 设置元素大小
                                label.setBackground(Color.LIGHT_GRAY);
                                label.setForeground(Color.BLACK); // 设置非选中时的前景色
                            }else {
                                label.setPreferredSize(new Dimension(150, 10));
                                label.setBackground(Color.white);
                                label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                            }
                        }
                        return c;
                    }
                };

                listTop.setCellRenderer(rendererTop);

                listTop.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listTop.setVisibleRowCount(5);

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

                jScrollPaneTop.setBounds(250,50,1160,352);
                jScrollPane.setBounds(-1,400,1410,470);

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

                jaddDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!mhCodeZon.isEmpty()){
                            int windowWidth = 350;
                            int windowHeight = 250;

                            JDialog jfAddDrug = new JDialog(keeperUI.this,"添加药品",true);
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

                            for (Map.Entry<String,Integer> entry : AllData.machineData.get(mhCodeZon).getMachineDrug().entrySet()){
                                dgCodes.add(entry.getKey());
                                dgNames.add(AllData.drugData.get(entry.getKey()).getDgName());
                                nameDrugComboBox.addItem(AllData.drugData.get(entry.getKey()).getDgName());
                            }

                            String selectedItem = (String) nameDrugComboBox.getSelectedItem();
                            int index = dgNames.indexOf(selectedItem);

                            JLabel typeDrug = new JLabel(AllData.drugData.get(dgCodes.get(index)).dgType);
                            typeDrug.setFont(new Font("SimSun",Font.BOLD,17));

                            nameDrugComboBox.addItemListener(new ItemListener() {
                                @Override
                                public void itemStateChanged(ItemEvent e) {
                                    if (e.getStateChange() == ItemEvent.SELECTED) {
                                        String selectedItem = (String) nameDrugComboBox.getSelectedItem();
                                        int index = dgNames.indexOf(selectedItem);
                                        typeDrug.setText(AllData.drugData.get(dgCodes.get(index)).dgType);
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
                                        if (number+AllData.machineData.get(mhCodeZon).getMachineDrug().get(dgCode)<=AllData.machineData.get(mhCodeZon).maxDrug.get(dgCode) && number>0){
                                            dgCodesZon.add(dgCode);
                                            listModel.addElement("  药品名称："+AllData.drugData.get(dgCode).getDgName()+"   |   药品种类："+AllData.drugData.get(dgCode).getDgType()+"   |   添加数量："+number);
                                            listModel.addElement("");
                                            list.updateUI();
                                            jfAddDrug.setVisible(false);
                                            jfAddDrug.dispose();
                                            JOptionPane.showConfirmDialog(null, "添加成功", "关闭确认", JOptionPane.DEFAULT_OPTION);
                                        }else {
                                            JOptionPane.showConfirmDialog(null, "药品数量不在限制范围内", "关闭确认", JOptionPane.DEFAULT_OPTION);
                                        }
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
                            typeDrug.setBounds(155,60,200,25);
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
                            JOptionPane.showConfirmDialog(null, "请先选择机器", "关闭确认", JOptionPane.DEFAULT_OPTION);
                        }

                    }
                });

                overDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int choice;
                        choice = JOptionPane.showConfirmDialog(null, "您确定要提交该记录吗？", "关闭确认", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            LocalDateTime now = LocalDateTime.now();
                            Timestamp timestamp = Timestamp.valueOf(now);

                            for (int i = 0;i<listModel.getSize();i+=2){
                                try {
                                    PreparedStatement preSelect = AllData.connection.prepareStatement("SELECT md_code FROM machine_drug where dg_code = ? and mh_code = ?");
                                    String dgcode = dgCodesZon.get(i/2);
                                    preSelect.setString(1,dgcode);
                                    preSelect.setString(2,mhCodeZon);
                                    ResultSet resultSet = preSelect.executeQuery();
                                    resultSet.next();
                                    String mdCode = resultSet.getString(1);
                                    resultSet.close();
                                    preSelect.close();

                                    String elementAt = String.valueOf(listModel.getElementAt(i));
                                    int add = Integer.parseInt(elementAt.split("添加数量：")[1]);
                                    int number = add + AllData.machineData.get(mhCodeZon).getMachineDrug().get(dgcode);
                                    PreparedStatement pre1 = AllData.connection.prepareStatement("UPDATE machine_drug set md_number = ? where md_code = ?");
                                    pre1.setInt(1,number);
                                    pre1.setString(2,mdCode);
                                    pre1.executeUpdate();
                                    pre1.close();

                                    String mgCode = newMgCode();

                                    PreparedStatement pre2 = AllData.connection.prepareStatement("INSERT INTO manage VALUES (?,?,?,?,?)");
                                    pre2.setString(1,mgCode);
                                    pre2.setString(2,kpCode);
                                    pre2.setString(3,mdCode);
                                    pre2.setTimestamp(4,timestamp);
                                    pre2.setInt(5,add);
                                    pre2.executeUpdate();
                                    pre2.close();

                                    AllData.machineData.get(mhCodeZon).getMachineDrug().put(dgcode,number);

                                    HashMap<String, Integer> drugChange = new HashMap<>();
                                    drugChange.put(dgcode,add);

                                    manage manage = new manage(kpCode, now, mhCodeZon, drugChange);
                                    AllData.manageData.put(mgCode,manage);
                                    AllData.manageCode.add(mgCode);

                                    JOptionPane.showConfirmDialog(null, "提交成功", "关闭确认", JOptionPane.DEFAULT_OPTION);
                                    changFirst(kpCode,leftPanel,rightPanel);

                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }

                    private String newMgCode() {
                        if (AllData.manageCode.isEmpty()){
                            return "#MG1";
                        }else {
                            int size = AllData.manageCode.size();
                            String string = AllData.manageCode.get(size-1);
                            return "#MG" + (Integer.parseInt(string.substring(3))+1);
                        }
                    }
                });

                deleteDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            listModel.remove(bottomIndex);
                            dgCodesZon.remove(bottomIndex/2);
                            list.updateUI();
                        }catch (ArrayIndexOutOfBoundsException e1){
                            System.out.println("No");
                        }
                    }
                });

                abandonDrugButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changFirst(kpCode,leftPanel,rightPanel);
                    }
                });

                overUserButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            String mhCode = jUserIdText.getText();
                            for (Map.Entry<String,Integer> entry : AllData.machineData.get(mhCode).getMachineDrug().entrySet()){
                                listModelTop.addElement("药品名称："+AllData.drugData.get(entry.getKey()).getDgName()+"   |   药品种类："+AllData.drugData.get(entry.getKey()).getDgType()+"   |   现有数量："+entry.getValue());
                                listModelTop.addElement("");
                            }

                            listTop.updateUI();

                            mhCodeZon = mhCode;

                            JOptionPane.showConfirmDialog(null, "查询成功", "关闭确认", JOptionPane.DEFAULT_OPTION);
                        }catch (NullPointerException e1){
                            listModelTop.clear();
                            listTop.updateUI();
                            JOptionPane.showConfirmDialog(null, "未找到该用户", "关闭确认", JOptionPane.DEFAULT_OPTION);
                        }
                    }
                });

                leftPanel.add(jScrollPane);
                leftPanel.add(jScrollPaneTop);

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

        rightPanel.add(jKpName);
        rightPanel.add(jHpName);
        rightPanel.add(addDrugButton);

        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
