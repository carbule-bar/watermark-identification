package com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
 
public class PicTest {
    public final static int THRESHOLD = 90;//阈值百分比
    public static void main(String[] args) throws Exception {
        //用于记录不同点
        int[][] comparyArray = new int[32][32];
        //两张图片
        BufferedImage img1 = ImageIO.read(new File("C:\\Users\\I\\Desktop\\watermark-identification\\Images\\pic1.jpg"));
        BufferedImage img2 = ImageIO.read(new File("C:\\Users\\I\\Desktop\\watermark-identification\\Images\\pic2.jpg"));
        //两张图片的切片
        BufferedImage img1Sub;
        BufferedImage img2Sub;
        float percent;
        //双循环用来取图片的切片坐标
        for(int i = 0;i<32;i++){
            for(int j = 0;j<32;j++){
                //取相同点的坐标
                img1Sub = img1.getSubimage(j*32, i*32, 32, 32);
                img2Sub = img2.getSubimage(j*32, i*32, 32, 32);
                //比较获得相似度
                percent = compare(getData(img1Sub), getData(img2Sub));
                if(percent>THRESHOLD){//比阈值大，则记录1表示相同
                    comparyArray[i][j] = 1;
                    System.out.print(1+" ");
                }else{//比阈值小，则记录0表示不同
                    comparyArray[i][j] = 0;
                    System.out.print(0+" ");
                }
            }
            System.out.println();
        }
         
    }
     
    //直方图作对比返回相似度
    public static float compare(int[] s, int[] t) {
        float result = 0F;
        for (int i = 0; i < 256; i++) {
            int abs = Math.abs(s[i] - t[i]);
            int max = Math.max(s[i], t[i]);
            result += (1 - ((float) abs / (max == 0 ? 1 : max)));
        }
        return (result / 256) * 100;
    }
    //根据图片获取直方图数据
    public static int[] getData(BufferedImage img) throws Exception {
        BufferedImage slt = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        slt.getGraphics().drawImage(img, 0, 0, 100, 100, null);
        // ImageIO.write(slt,"jpeg",new File("slt.jpg"));
        int[] data = new int[256];
        for (int x = 0; x < slt.getWidth(); x++) {
            for (int y = 0; y < slt.getHeight(); y++) {
                int rgb = slt.getRGB(x, y);
                Color myColor = new Color(rgb);
                int r = myColor.getRed();
                int g = myColor.getGreen();
                int b = myColor.getBlue();
                data[(r + g + b) / 3]++;
            }
        }
        // data 就是所谓图形学当中的直方图的概念
        return data;
    }
}