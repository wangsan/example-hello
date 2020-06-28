package com.wangsan.study.javacv;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 * 调用本地摄像头窗口视频
 *
 * @author eguid
 * @version 2016年6月13日
 * @since javacv1.2
 */

public class JavavcCameraTest {
    public static void main(String[] args) throws Exception, InterruptedException {

        //新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();   //开始获取摄像头数据

        CanvasFrame canvas = new CanvasFrame("摄像头预览");//新建一个预览窗口
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);

        while (true) {
            if (!canvas.isDisplayable()) {//窗口是否关闭
                grabber.close();//停止抓取
                return;
            }
            final Frame frame = grabber.grab();
            canvas.showImage(frame);//获取摄像头图像并放到窗口上显示， 这里的Frame
        }
    }
}