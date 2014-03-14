/*
 *The MIT License (MIT)

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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author omidsaremi
 */
class KMeansColorQuantizer extends JFrame implements ActionListener, ItemListener, ChangeListener {

    /**
     * @param args the command line arguments
     */
    private JComboBox jComboBox;
    private JButton compressButton, questionButton;
    private JSlider jSlider;
    private String folderName, firstFilePath;
    private int compressionVal;

    public KMeansColorQuantizer() {
        /* First creates the Dialogue box which gets the folder name throws error messages if the 
         specified folder does not exists or it does not contain images */
        super();
        setTitle("Machine Learning");
        setSize(540, 440);
        setLookAndFeel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String myFolderName = "";
        boolean check1 = false;
        boolean check2 = false;

        ImageFolder imageFolder = new ImageFolder();
        while (!check1 | !check2) {
            myFolderName = imageFolder.getTheFolderName();
            System.out.println(myFolderName);
            if (myFolderName != null) {
                check1 = new File(myFolderName).exists();
                if (check1) {
                    check2 = imageFolder.isThereImageFile(new File(myFolderName));
                }
                System.out.println(check1 + "  " + check2);
                if (!check1 | !check2) {
                    if (!check1) {
                        imageFolder.addWarningLabel("Folder doesn't exist! Enter a valid folder name.");
                    } else {
                        imageFolder.addWarningLabel("Add images or enter another folder name.");
                    }
                }
            }
        }
        /* The main window is created */

        firstFilePath = imageFolder.getFirstFilePath();
        folderName = myFolderName;
        imageFolder.closeTheDialogueBox();

        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel();
        JPanel row4 = new JPanel();
        JPanel row5 = new JPanel();
        JPanel row6 = new JPanel();
        JLabel jLabCompression, jLabel1, jLabel2, browse;

        GridLayout generalLayOut = new GridLayout(6, 1);
        setLayout(generalLayOut);

        FlowLayout layout1 = new FlowLayout(FlowLayout.CENTER);
        row1.setLayout(layout1);

        jLabel1 = new JLabel("K-means Color Quantizer");
        jLabel1.setFont(new Font("Georgia", Font.BOLD, 20));
        row1.setOpaque(true);
        row1.setBackground(new Color(0, 128, 255));

        ImageIcon question = new ImageIcon(getClass().getResource("/resources/images/question6.jpg"));

        questionButton = new JButton(question);
        row1.add(questionButton);
        row1.add(jLabel1);
        add(row1);
        questionButton.addActionListener(this);

        jComboBox = new JComboBox();
        browse = new JLabel("Browse");
        browse.setFont(new Font("Georgia", Font.BOLD, 15));
        FlowLayout layout2 = new FlowLayout();
        row2.setLayout(layout2);

        String[] namesOfFiles = ImageFolder.getFileNames(folderName);

        for (int c1 = 0; c1 < namesOfFiles.length; c1++) {
            jComboBox.addItem(namesOfFiles[c1]);
        }
        row2.add(jComboBox);
        row2.add(browse);
        add(row2);
        jComboBox.addItemListener(this);

        FlowLayout layout3 = new FlowLayout();
        row3.setLayout(layout3);
        /* Compression level is initialized to 8, in case state of the JSlider is not changed by user action */
        compressionVal = 8;
        jSlider = new JSlider(8, 32, 8);
        jSlider.setMinorTickSpacing(1);
        jSlider.setPaintTicks(true);
        jSlider.setSnapToTicks(true);
        jSlider.setPaintLabels(true);
        jSlider.setLabelTable(jSlider.createStandardLabels(24));
        row3.add(jSlider);
        add(row3);
        jSlider.addChangeListener(this);

        FlowLayout layout4 = new FlowLayout();
        row4.setLayout(layout4);
        jLabCompression = new JLabel("Compression");
        jLabCompression.setFont(new Font("Georgia", Font.BOLD, 15));
        jLabCompression.setForeground(Color.BLUE);
        row4.add(jLabCompression);
        add(row4);

        FlowLayout layout5 = new FlowLayout();
        row5.setLayout(layout5);
        compressButton = new JButton("Compress");
        compressButton.setBackground(new Color(100, 128, 255));
        row5.add(compressButton);
        add(row5);
        compressButton.addActionListener((ActionListener) this);

        FlowLayout layout6 = new FlowLayout();
        row6.setLayout(layout6);

        jLabel2 = new JLabel("");
        jLabel2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jLabel2.setForeground(Color.BLUE);
        row6.add(jLabel2);
        add(row6);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception exc) {
        }
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        int choice = jComboBox.getSelectedIndex();
        firstFilePath = (String) jComboBox.getItemAt(choice);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == compressButton) {
            ToBeProcessedImage img = new ToBeProcessedImage(firstFilePath);
            SimpleMatrix myInitCenters = img.effectiveInitialCenters(compressionVal);
            /* Makes an estimate of the running time and warns accordingly */
            long timeInit = System.currentTimeMillis();
            long t1, t2;
            for (int i = 0; i < 20; i++) {
                t1 = System.currentTimeMillis();
                img.quantizeTheImage(myInitCenters);
                t2 = System.currentTimeMillis();
                if (i == 0 & t2 - t1 > 1000) {
                    JOptionPane.showMessageDialog(null, "Warning: This might take long!");
                }
            }
            long timeFinal = System.currentTimeMillis();
            BufferedImage initialImage = img.bufferedImage;
            BufferedImage finalImage = img.generateTheFinalImage();
            ResultFrame finalFrame = new ResultFrame(finalImage, initialImage, timeFinal - timeInit);
        }
        if (source == questionButton) {
            AboutThisApp about = new AboutThisApp();
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        compressionVal = (int) jSlider.getValue();
    }

    public static void main(String[] args) {
        //System.out.println(System.getProperty("java.class.path"));
        KMeansColorQuantizer gui;
        gui = new KMeansColorQuantizer();

    }
}
