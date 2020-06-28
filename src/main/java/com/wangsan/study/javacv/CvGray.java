package com.wangsan.study.javacv;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

//得到灰度图像
public class CvGray {
    public static void main(String[] args) {
        String classPath = CvSmoother.class.getClassLoader().getResource("").getPath();
        String filename = classPath + "/test.jpg";
        String fileGray = classPath + "/test-gray.jpg";
        Mat image = imread(filename, opencv_imgcodecs.IMREAD_GRAYSCALE);

        imwrite(fileGray, image);
        // 读入一个图像文件并转换为灰度图像（由无符号字节构成）
        //        Mat image1 = imread(filename, opencv_imgcodecs.IMREAD_COLOR);
        //        //读取图像，并转换为三通道彩色图像，这里创建的图像中每个像素有3字节
        //        //如果输入图像为灰度图像，这三个通道的值就是相同的
        //        System.out.println("image has " + image1.channels() + " channel(s)");
        // channels方法可用来检查图像的通道数
        //        flip(image, image, 1);//就地处理,参数1表示输入图像，参数2表示输出图像
        //        //在一窗口显示结果
        //        namedWindow("输入图片显示窗口");//定义窗口
        //        imshow("输入图片显示窗口", image);//显示窗口
        //        waitKey(0);//因为他是控制台窗口，会在main函数结束时关闭;0表示永远的等待按键,正数表示等待指定的毫秒数
    }
}