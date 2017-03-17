package com.brufstudios.drivelikescanner.common;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageEditor {
    File file;

    public ImageEditor(File file) {
        this.file = file;
    }

    public void perspectiveTransform() {

//        Mat transformed = new Mat();
//
//        Point p1 = new Point(0,0);
//        Point p2 = new Point(0,0);
//        Point p3 = new Point(0,0);
//        Point p4 = new Point(0,0);
//
//        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(image, transformed);
//        Imgproc.warpPerspective(image, transformed);
    }

    public void test() {

        Mat image = Imgcodecs.imread(file.getAbsolutePath());
        Mat gray = new Mat();
        Mat binary = new Mat();
        Mat finalImage = new Mat();

        Mat hierarchy =  new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        MatOfPoint biggerCountour = null;

        Imgproc.cvtColor(image, gray, Imgproc.COLOR_GRAY2BGR);
        Imgproc.threshold(gray, binary, 20, 255, Imgproc.THRESH_BINARY);
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

        for (MatOfPoint contour: contours) {
            Double area = Imgproc.contourArea(contour);
            if (Math.pow(10, 2) > area || Math.pow(10, 2) < area) {
                if (null == biggerCountour || Imgproc.contourArea(contour) > Imgproc.contourArea(biggerCountour)) {
                    biggerCountour = contour;
                }
            }
        }

        Mat maskImage = new Mat(image.size(), CvType.CV_8U, new Scalar(0));
        Imgproc.drawContours(maskImage, contours, contours.indexOf(biggerCountour), new Scalar(255), Core.FILLED);

        gray.copyTo(finalImage, maskImage);
        finalImage.convertTo(finalImage, -1, 1.1, 0);

        Imgcodecs.imwrite(file.getPath(), finalImage);

    }


}
