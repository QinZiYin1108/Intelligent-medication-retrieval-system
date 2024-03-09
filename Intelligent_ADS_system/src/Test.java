import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Auto Suggest Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个 JPanel，并设置布局管理器
        JPanel panel = new JPanel(new FlowLayout());

        // 创建一个下拉框和输入框
        JComboBox<String> comboBox = new JComboBox<>();
        JTextField textField = new JTextField(15);

        // 添加下拉框和输入框到 JPanel
        panel.add(comboBox);
        panel.add(textField);

        // 添加一些可选内容到下拉框
        List<String> options = new ArrayList<>();
        options.add("apple");
        options.add("banana");
        options.add("cherry");

        for (String option : options) {
            comboBox.addItem(option);
        }

        // 为输入框添加监听器，实现自动提示功能
        textField.getDocument().addDocumentListener(new DocumentListener() {
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
                String input = textField.getText();
                comboBox.removeAllItems();
                for (String option : options) {
                    if (option.startsWith(input)) {
                        comboBox.addItem(option);
                    }
                }
                comboBox.setPopupVisible(true);
            }
        });

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}