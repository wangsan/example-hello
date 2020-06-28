package com.wangsan.study.javacv;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.util.StopWatch;

/**
 * TODO：处理视频.（1.将视频提取成帧图片）
 *
 * @author ChenP
 */
public class VideoProcessing {

    //视频文件路径
    private static String videoPath = "/Users/wangqingpeng/test/cv/video";

    //视频帧图片存储路径
    public static String videoFramesPath = "/Users/wangqingpeng/test/cv/img";

    /**
     * TODO 将视频文件帧处理并以“jpg”格式进行存储。
     * 依赖FrameToBufferedImage方法：将frame转换为bufferedImage对象
     *
     * @param videoFileName
     */
    public static void grabberVideoFramer(String videoFileName) {
        //标识
        int flag = 0;
        /*
            获取视频文件
         */
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(videoPath + "/" + videoFileName);

        //        FrameGrabber grabber = new FrameGrabber("rtsp:/192.168.0.0");

        // 增加超时参数 http://ffmpeg.org/ffmpeg-all.html
        fFmpegFrameGrabber.setOption("stimeout", "20000000");

        try {
            fFmpegFrameGrabber.start();

            /*
            .getFrameRate()方法：获取视频文件信息,总帧数
             */
            int lengthInFrames = fFmpegFrameGrabber.getLengthInFrames();
            final double fps = fFmpegFrameGrabber.getFrameRate();
            //            System.out.println(fFmpegFrameGrabber.grabKeyFrame());
            System.out.println("时长(秒) " + lengthInFrames / fps);

            double lenInSeconds = lengthInFrames / fps;

            System.out.println("开始运行视频提取帧，耗时较长");
            //            while (flag <= lengthInFrames) {
            //                String fileName = videoFramesPath + "/img_" + String.valueOf(flag) + ".jpg";
            //                File outPut = new File(fileName);
            //                Frame frame = fFmpegFrameGrabber.grabImage();
            //                //                System.out.println(frame);
            //                if (frame != null) {
            //                    ImageIO.write(frameToBufferedImage(frame), "jpg", outPut);
            //                }
            //                flag++;
            //            }

            StopWatch stopWatch = new StopWatch("all");
            for (int i = 0; i < lengthInFrames; i = i + 5*30) {
                stopWatch.start("task-" + i);
                fFmpegFrameGrabber.setFrameNumber(i);
                String fileName = videoFramesPath + "/img_" + i + ".jpg";
                File outPut = new File(fileName);
                Frame frame = fFmpegFrameGrabber.grabImage();
                if (frame != null) {
                    ImageIO.write(frameToBufferedImage(frame), "jpg", outPut);
                }
                stopWatch.stop();
            }
            System.out.println(stopWatch.prettyPrint());

            fFmpegFrameGrabber.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    /*
        测试.....
     */
    public static void main(String[] args) {
        String videoFileName = "test.mp4";
        grabberVideoFramer(videoFileName);
    }

    public static String getVideoPath() {
        return videoPath;
    }

    public static void setVideoPath(String videoPath) {
        VideoProcessing.videoPath = videoPath;
    }
}