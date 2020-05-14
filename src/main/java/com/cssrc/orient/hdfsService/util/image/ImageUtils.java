package com.cssrc.orient.hdfsService.util.image;

import com.cssrc.orient.hdfsService.util.tool.StringUtil;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author rain
 * @date 2020/4/27 16:20
 */
public class ImageUtils {

    /**
     * 按指定高度 等比例缩放图片
     *
     * @param imageFile
     * @param newWidth  新图的宽度
     * @throws IOException
     */
    public static void zoomImageScale(File imageFile, int newWidth) throws IOException {
        if (!imageFile.canRead())
            return;
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (null == bufferedImage)
            return;

        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        double scale = (double) originalWidth / (double) newWidth;    // 缩放的比例

        int newHeight = (int) (originalHeight / scale);
        String newPath = StringUtils.substringBeforeLast(imageFile.getAbsolutePath(), ".") + "_s." + StringUtils.substringAfterLast(imageFile.getAbsolutePath(), ".");
        zoomImageUtils(imageFile, newPath, bufferedImage, newWidth, newHeight,"");
    }

    public static void zoomImageScale(File imageFile, int newWidth,String fileType) throws IOException {
        if (!imageFile.canRead())
            return;
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (null == bufferedImage)
            return;

        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        double scale = (double) originalWidth / (double) newWidth;    // 缩放的比例

        int newHeight = (int) (originalHeight / scale);
        String newPath = StringUtils.substringBeforeLast(imageFile.getAbsolutePath(), ".") + "_s." + fileType;
        zoomImageUtils(imageFile, newPath, bufferedImage, newWidth, newHeight,fileType);
    }


    private static void zoomImageUtils(File imageFile, String newPath, BufferedImage bufferedImage, int width, int height,String fileType)
            throws IOException {

        String suffix = StringUtil.isEmpty(fileType) ? StringUtils.substringAfterLast(imageFile.getName(), "."):fileType;

        // 处理 png 背景变黑的问题
        if (suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))) {
            BufferedImage to = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = to.createGraphics();
            Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            ImageIO.write(to, suffix, new File(newPath));
        } else {
            BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
            Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(newImage, suffix, new File(newPath));
        }
    }


    /**
     * HDFS中图片缩放
     *
     * @param inputStream
     * @param newPath
     * @param newWidth
     * @param fileType
     * @throws IOException
     */
    public static void zoomImageScale(InputStream inputStream, String newPath, int newWidth, String fileType) throws IOException {
        if ( null == inputStream)
            return;
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        if (null == bufferedImage)
            return;

        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        double scale = (double) originalWidth / (double) newWidth;    // 缩放的比例

        int newHeight = (int) (originalHeight / scale);
        zoomImageUtils( newPath, bufferedImage, newWidth, newHeight,fileType);
    }

    private static void zoomImageUtils(String newPath, BufferedImage bufferedImage, int width, int height,String fileType)
            throws IOException {

        String suffix = fileType;

        // 处理 png 背景变黑的问题
        if (suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))) {
            BufferedImage to = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = to.createGraphics();
            Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            ImageIO.write(to, suffix, new File(newPath));
        } else {
            BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
            Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(newImage, suffix, new File(newPath));
        }
    }

    /**
     * 等比例改变图片尺寸
     *
     * @param nw       新图片的宽度
     * @param oldImage 原图片
     * @throws IOException
     */
    public static void constrainProportios(int nw, String oldImage) throws IOException {
        AffineTransform transform = new AffineTransform();
        BufferedImage bis = ImageIO.read(new File(oldImage));
        int w = bis.getWidth();
        int h = bis.getHeight();
        int nh = (nw * h) / w;
        double sx = (double) nw / w;
        double sy = (double) nh / h;
        transform.setToScale(sx, sy);
        AffineTransformOp ato = new AffineTransformOp(transform, null);
        BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
        ato.filter(bis, bid);
        String newPath = StringUtils.substringBeforeLast(oldImage, ".") + "_s." + StringUtils.substringAfterLast(oldImage, ".");
        ImageIO.write(bid, "jpeg", new File(newPath));
    }

}
