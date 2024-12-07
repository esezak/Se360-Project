package com.esezak.client.UI.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageDownloader {
    public static boolean DownloadImage(String path, String fileName) {
        try{
            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            Image resizedimg = image.getScaledInstance(170,250,Image.SCALE_DEFAULT);
            BufferedImage bufferedResizedImage = new BufferedImage(170, 250, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = bufferedResizedImage.createGraphics();
            g2d.drawImage(resizedimg, 0, 0,170,250, null);
            g2d.dispose();

            ImageIO.write(bufferedResizedImage, "jpg", new File("Posters/"+fileName+".jpg"));
            return true;
        } catch (IOException e) {
            System.err.println("Error while Downloading Image");
            return false;
        }
    }
    public static void main(String[] args) {
        String url = "https://artworks.thetvdb.com/banners/movies/131199/posters/25872041.jpg";
        String fileName = "002";
        DownloadImage(url,fileName);
    }
}
