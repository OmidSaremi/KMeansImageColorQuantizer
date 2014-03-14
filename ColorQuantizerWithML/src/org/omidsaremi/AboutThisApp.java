/*
 The MIT License (MIT)

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author omidsaremi
 */
public class AboutThisApp extends JFrame {

    JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5;
    JPanel row1, row2, row3, row4, row5;

    public AboutThisApp(){
        super("About This Application");
        GridLayout gridLayout = new GridLayout(5, 1);
        setLayout(gridLayout);
        setSize(355, 300);

        row1 = new JPanel(new FlowLayout());
        row2 = new JPanel(new FlowLayout());
        row3 = new JPanel(new FlowLayout());
        row4 = new JPanel(new FlowLayout());
        row5 = new JPanel(new FlowLayout());

        row1.setBackground(new Color(0, 128, 255));
        jLabel1 = new JLabel("K-Means Image Quantizer");
        jLabel1.setForeground(new Color(255, 255, 255));
        jLabel1.setFont(new Font("Georgia", Font.HANGING_BASELINE, 15));
        row1.add(jLabel1);
        add(row1);

        jLabel2 = new JLabel("K-Means Clustering Algorithm (Unsupervised Learning)");
        row2.add(jLabel2);
        add(row2);

        jLabel3 = new JLabel("Utilizes EJML library (Apache v2.0 License)");
        row3.add(jLabel3);
        add(row3);

        jLabel4 = new JLabel("Developed in Java");
        row4.add(jLabel4);
        add(row4);

        jLabel5 = new JLabel("Copyright \u00A9 2014 Omid Saremi, The MIT License");
        row5.add(jLabel5);
        add(row5);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

    }

}
