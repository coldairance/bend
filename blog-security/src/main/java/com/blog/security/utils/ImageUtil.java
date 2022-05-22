package com.blog.security.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    private ImageUtil() {

    }

    public static void cut(File pic,int sx,int sy,int width,int height) throws IOException {
        BufferedImage picture = ImageIO.read(pic);
        // 起始x、起始y、截取宽度、截取高度
        BufferedImage newPic = picture.getSubimage(sx, sy, width, height);
        ImageIO.write(newPic, "png", pic);
    }
}
