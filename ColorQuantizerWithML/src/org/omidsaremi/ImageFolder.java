/*
 * The MIT License (MIT)

 Copyright (c) 2014 Omid Saremi

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.omidsaremi;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author omidsaremi
 */
public class ImageFolder extends JFrame implements ActionListener {

    private JLabel jLabelWarning;
    private JTextField jTextField;
    public String folder, firstImageFilePath;

    public ImageFolder() {
        super();
        setTitle("Image Folder Path");
        setSize(340, 150);
        setLayout(new FlowLayout());
        setLookAndFeel();

        JLabel jLabel = new JLabel("Enter the image folder path:");
        jLabelWarning = new JLabel("Warning: Folder must contain image files!");
        JButton jButton = new JButton("Set my folder!");
        jButton.setOpaque(true);
        jButton.setBackground(new Color(0, 150, 150));

        jTextField = new JTextField(20);
        add(jLabel);
        add(jTextField);
        add(jButton);
        add(jLabelWarning);

        jButton.addActionListener(this);
        jTextField.addActionListener((ActionListener) this);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setLookAndFeel() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception exc) {

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton) {
            folder = jTextField.getText();
        }

    }

    public String getTheFolderName() {
        return folder;
    }

    public void addWarningLabel(String message) {
        jLabelWarning.setText(message);
        jLabelWarning.setFont(new Font("Serif", Font.BOLD, 14));
        jLabelWarning.setForeground(new Color(255, 30, 0));
        repaint();
    }

    public void closeTheDialogueBox() {
        dispose();
    }

    /*This method checks for the image files in the folder specified by the user */
    public boolean isThereImageFile(File usersFolder) {
        boolean value = false;
        File[] listOfFiles = usersFolder.listFiles();
        String fileNames[] = new String[listOfFiles.length];
        String nm, pff;

        for (File list_of_file : listOfFiles) {
            if (list_of_file.isFile()) {
                nm = list_of_file.getName();
                nm = nm.toLowerCase();
                if (nm.contains(".jpg") | nm.contains(".png") | nm.contains(".gif") | nm.contains("jpeg")) {
                    value = true;
                    pff = list_of_file.getPath();
                    firstImageFilePath = pff;
                    return value;
                }
            }
        }
        return value;
    }

    /* Sets the first image file in the folder as default in case no image is choosen before the compress button is pressed */
    public String getFirstFilePath() {
        return this.firstImageFilePath;
    }

    /* Lists the files in the specified foldered */
    public static String[] getFileNames(String folderName) {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        String fileNames[] = new String[listOfFiles.length];
        String nm;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                nm = listOfFiles[i].getName();
                if (!nm.contains(".DS_Store")) {
                    fileNames[i] = listOfFiles[i].getPath();
                }

            }

        }
        return fileNames;
    }

}
