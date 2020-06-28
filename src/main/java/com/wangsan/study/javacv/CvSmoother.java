package com.wangsan.study.javacv;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.GaussianBlur;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

/**
 * created by wangsan on 2020/6/23.
 *
 * @author wangsan
 */
public class CvSmoother {
    public static void main(String[] args) {
        String classPath = CvSmoother.class.getClassLoader().getResource("").getPath();
        String filename = classPath + "/test.jpg";
        String fileSmoother = classPath + "/test-smoother.jpg";

        Mat image = imread(filename);
        if (image != null) {
            GaussianBlur(image, image, new Size(3, 3), 0);
            imwrite(fileSmoother, image);
        }
    }
}
