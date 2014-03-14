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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author omidsaremi
 */
public class ResultFrame extends JFrame {

    public ResultFrame(BufferedImage img1, BufferedImage img2, long timeInterval) {
        super();
        setTitle("Output");
        setLookAndFeel();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        ImageIcon imageIconComp = new ImageIcon(img1);
        ImageIcon imageIconOrig = new ImageIcon(img2);
        JLabel labelComp = new JLabel(imageIconComp);
        JLabel labelOrig = new JLabel(imageIconOrig);

        double sec = timeInterval / 1000.0;
        sec = Double.parseDouble(new DecimalFormat("##.##").format(sec));
        String time_string = String.valueOf(sec);
        String timeLabel = "Running time: " + time_string + " seconds";
        JLabel runningTime = new JLabel(timeLabel);
        runningTime.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 15));
        runningTime.setOpaque(true);
        runningTime.setForeground(new Color(0, 128, 255));
        c.ipadx = 10;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(labelComp, c);

        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 10;
        panel.add(labelOrig, c);

        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 1;

        panel.add(runningTime, c);
        add(panel);

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public void setLookAndFeel() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception exc) {

        }
    }
}
