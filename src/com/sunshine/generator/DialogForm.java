package com.sunshine.generator;

import javax.swing.*;

/**
 * author : sunny
 * email : zicai346@gmail.com
 * github : https://github.com/sunshinePrince
 * blog : http://mrjoker.wang
 */
public class DialogForm {
    private JLabel checkLabel;
    private JLabel nameLabel;
    private JLabel packageLabel;
    private JLabel reserveLabel;
    private JCheckBox checkBox1;
    private JTextField nameFiled;

    public static void main(String[] args) {
        JFrame frame = new JFrame("DialogForm");
        frame.setContentPane(new DialogForm().mPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JTextField packageField;
    private JTextField reserveField;
    private JPanel mPanel;
}
