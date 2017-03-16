package com.brufstudios.drivelikescanner.common;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageEditor {

    Mat image;

    public ImageEditor(byte[] data) {
        image = new MatOfByte(data);
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

    public byte[] test() {

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

        byte[] return_buff = new byte[(int) (finalImage.total() * finalImage.channels())];
        finalImage.get(0, 0, return_buff);
        return return_buff;
    }


}
