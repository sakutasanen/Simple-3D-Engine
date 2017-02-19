/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

/**
 *
 * @author Saku
 */
public class Texture {

    private BufferedImage texture;
    private Bitmap bitmap;
    private int width;
    private int height;

    public Texture(String filePath) {
        try {
            texture = ImageIO.read(new File(filePath));
            bitmap = new Bitmap(texture);
            width = texture.getWidth();
            height = texture.getHeight();
        } catch (IOException e) {
            System.out.println("Failed to load texture");
        }
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public byte[] getColor(int x, int y) {
        return ByteBuffer.allocate(4).putInt(texture.getRGB(x, y)).array();
        
    }
    
    public byte[] getDarkColor(int x, int y, float factor) {
        int color = texture.getRGB(x, y);
        int a = (color & (0xFF << 24)) >> 24;
        int r = (int) (((color & (0xFF << 16)) >> 16) * factor);
        int g = (int) (((color & (0xFF << 8)) >> 8) * factor);
        int b = (int) ((color & 0xFF) * factor);
        
        byte[] byteArray = {(byte)a, (byte)r, (byte)g, (byte)b};
        
        return byteArray;
    }

}
