package org.scaffold.commons.util;

import com.mortennobel.imagescaling.ResampleOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 接收输入流输生成图片
     * @param input
     * @param writePath
     * @param width
     * @param height
     * @param format
     * @return
     */
    public boolean resizeImage(InputStream input, String writePath,
                               Integer width, Integer height, String format) {
        try {
            BufferedImage inputBufImage = ImageIO.read(input);
            log.info("转前图片高度和宽度：" + inputBufImage.getHeight() + ":"+ inputBufImage.getWidth());
            ResampleOp resampleOp = new ResampleOp(width, height);// 转换
            BufferedImage rescaledTomato = resampleOp.filter(inputBufImage,
                    null);
            ImageIO.write(rescaledTomato, format, new File(writePath));
            log.info("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":"+ rescaledTomato.getWidth());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 接收File输出图片
     * @param file
     * @param writePath
     * @param width
     * @param height
     * @param format
     * @return
     */
    public boolean resizeImage(File file, String writePath, Integer width,
                               Integer height, String format) {
        try {
            BufferedImage inputBufImage = ImageIO.read(file);
            inputBufImage.getType();
            log.info("转前图片高度和宽度：" + inputBufImage.getHeight() + ":"+ inputBufImage.getWidth());
            ResampleOp resampleOp = new ResampleOp(width, height);// 转换
            BufferedImage rescaledTomato = resampleOp.filter(inputBufImage,
                    null);
            ImageIO.write(rescaledTomato, format, new File(writePath));
            log.info("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":"+ rescaledTomato.getWidth());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 接收字节数组生成图片
     * @param RGBS
     * @param writePath
     * @param width
     * @param height
     * @param format
     * @return
     */
    public boolean resizeImage(byte[] RGBS, String writePath, Integer width,
                               Integer height, String format) {
        InputStream input = new ByteArrayInputStream(RGBS);
        return this.resizeImage(input, writePath, width, height, format);
    }

    public byte[] readBytesFromIS(InputStream is) throws IOException {
        int total = is.available();
        byte[] bs = new byte[total];
        is.read(bs);
        return bs;
    }


    //测试：只测试了字节流的方式，其它的相对简单，没有一一测试
    public static void main(String[] args) throws IOException {
        int width = 150;
        int height = 150;
        File inputFile = new File("C:\\temp\\fj.jpg");
        File outFile = new File("c:\\temp\\to.jpg");
        String outPath = outFile.getAbsolutePath();
        ImageUtil myImage = new ImageUtil();
        InputStream input = new FileInputStream(inputFile);
        byte[] byteArrayImage=myImage.readBytesFromIS(input);
        input.read(byteArrayImage);
        myImage.resizeImage(byteArrayImage, outPath, width, height, "jpg");
    }
}
