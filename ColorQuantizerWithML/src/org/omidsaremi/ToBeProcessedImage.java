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

import org.ejml.simple.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author omidsaremi
 */
public class ToBeProcessedImage {

    /**
     * @param args the command line arguments
     */
    private int width;
    private int height;
    private int numPixels;
    private int kf;
    private boolean hasAlphaChannel;
    public SimpleMatrix rgbMat, centers;
    public BufferedImage bufferedImage;

    public ToBeProcessedImage(String imgPath) {
        BufferedImage img = null;
        File imageFile = new File(imgPath);
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
        }
        bufferedImage = img;
        width = img.getWidth();
        height = img.getHeight();
        numPixels = width * height;
        hasAlphaChannel = img.getColorModel().hasAlpha();

        int[] pixels;
        byte[] pixelByte = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        pixels = byteArrayIntoIntArray(pixelByte);

        SimpleMatrix rgbMatTemp = new SimpleMatrix(numPixels, 3);
        int count = 0;
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                rgbMatTemp.set(count, 2, pixels[3 * (width * h + w)]);
                rgbMatTemp.set(count, 1, pixels[3 * (width * h + w) + 1]);
                rgbMatTemp.set(count, 0, pixels[3 * (width * h + w) + 2]);
                count++;
            }
        }
        rgbMat = rgbMatTemp;
    }

    private static int[] byteArrayIntoIntArray(byte[] s) {
        int[] conv = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            conv[i] = s[i] & 0xFF;
        }
        return conv;
    }

    /* This method generates the initial DISTINCT cluster centers */
    public SimpleMatrix effectiveInitialCenters(int numCenters) {
        int[] randCent = new int[numCenters];
        int[] key = new int[numPixels];
        int ind;

        Random rand = new Random();
        int c = 0;
        while (c < numCenters) {
            ind = rand.nextInt(numPixels);
            if (key[ind] == 0) {
                randCent[c] = ind;
                key[ind] = 1;
                c++;
            }
        }

        HashMap hash = new HashMap();
        SimpleMatrix extractedRGB;
        int intRGB;
        Color color;

        for (int k1 = 0; k1 < numCenters; k1++) {
            extractedRGB = rgbMat.extractMatrix(randCent[k1], randCent[k1] + 1, 0, 3);
            color = new Color((int) extractedRGB.get(0, 0), (int) extractedRGB.get(0, 1), (int) extractedRGB.get(0, 2));
            intRGB = color.getRGB();

            if (!hash.containsValue(intRGB)) {
                hash.put(k1, intRGB);
            }
        }
        kf = this.effectiveNumOfCenters(hash);

        Collection coll = hash.keySet();

        int ccc = 0;
        int intObject;
        SimpleMatrix effCenters = new SimpleMatrix(kf, 3);
        SimpleMatrix extractedRGB2;

        for (Object object : coll) {
            intObject = (int) object;
            extractedRGB2 = rgbMat.extractMatrix(randCent[intObject], randCent[intObject] + 1, 0, 3);
            effCenters.insertIntoThis(ccc, 0, extractedRGB2);
            ccc++;
        }
        return effCenters;
    }

    /* Labels pixels based of their "color Euclidean distance" from the cluster centers */
    private SimpleMatrix labelsOfPixels(SimpleMatrix rgbRowVecs, SimpleMatrix centerRowVecs, int numCenters) {

        SimpleMatrix labelVec = new SimpleMatrix(1, numPixels);
        double minDist, d;
        for (int i = 0; i < numPixels; i++) {
            int lab = 0;
            minDist = 100.00 * (256.0 * 256.0);
            for (int j = 0; j < numCenters; j++) {
                SimpleMatrix rgbVec = rgbRowVecs.extractMatrix(i, i + 1, 0, 3);
                SimpleMatrix centroid = centerRowVecs.extractMatrix(j, j + 1, 0, 3);
                SimpleMatrix dif = rgbVec.minus(centroid);
                SimpleMatrix euclideanDist = dif.mult(dif.transpose());
                d = euclideanDist.get(0, 0);
                if (d < minDist) {
                    minDist = d;
                    lab = j;
                }

            }
            labelVec.set(0, i, lab);
        }
        return labelVec;
    }

    /* Given a set of labels for the pixels, this method updates the cluster centers using arithmatic mean */
    private SimpleMatrix newCenters(SimpleMatrix labels, SimpleMatrix rgbRowVec, int numCenters) {

        SimpleMatrix mu = new SimpleMatrix(numCenters, 3);

        for (int i = 0; i < numCenters; i++) {
            int c = 0;
            SimpleMatrix muTemp = new SimpleMatrix(1, 3);
            for (int w = 0; w < numPixels; w++) {

                if (labels.get(0, w) == i) {
                    muTemp = muTemp.plus(rgbRowVec.extractMatrix(w, w + 1, 0, 3));
                    c = c + 1;
                }
            }

            muTemp = muTemp.divide(c);
            mu.insertIntoThis(i, 0, muTemp);
        }
        return mu;
    }

    public void quantizeTheImage(SimpleMatrix initCenters) {
        SimpleMatrix labels;
        centers = initCenters;
        labels = labelsOfPixels(rgbMat, centers, kf);
        centers = newCenters(labels, rgbMat, kf);
    }

    /*Given the labels and currents set of centers, this methods colors the pixels and generates an image */
    private BufferedImage newImage(SimpleMatrix myLabels, SimpleMatrix myCenters) {

        BufferedImage imgOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        SimpleMatrix extractedCol;
        int rgbInt;
        int counter = 0;
        Color colObj;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int s = (int) myLabels.get(0, counter);
                extractedCol = myCenters.extractMatrix(s, s + 1, 0, 3);
                colObj = new Color((int) extractedCol.get(0, 0), (int) extractedCol.get(0, 1), (int) extractedCol.get(0, 2));
                rgbInt = colObj.getRGB();
                imgOut.setRGB(j, i, rgbInt);
                counter++;
            }
        }
        return imgOut;
    }

    public BufferedImage generateTheFinalImage() {
        BufferedImage outputImage;
        SimpleMatrix labelsFinal = labelsOfPixels(rgbMat, centers, kf);
        SimpleMatrix centersFinal = newCenters(labelsFinal, rgbMat, kf);
        outputImage = newImage(labelsFinal, centersFinal);
        return outputImage;
    }

    private int effectiveNumOfCenters(HashMap A) {
        return A.size();
    }

    public SimpleMatrix getCenters() {
        return centers;
    }
}
