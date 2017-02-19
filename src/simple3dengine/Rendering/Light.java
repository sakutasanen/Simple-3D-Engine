/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import simple3dengine.Math.Transformation3D;
import simple3dengine.Math.Vector4f;

/**
 *
 * @author Saku
 */
public class Light {

    private Transformation3D lightTransformation;
    private Vector4f posVector;
    private static Bitmap lightBuffer;

    private static float fovX;
    private static float fovY;
    private static float zNear;
    private static float zFar;
    
    private static int width;
    private static int height;

    public static void initializeLight(int bufferWidth, int bufferHeight,
            float fovX, float fovY, float zNear, float zFar) {
            Light.fovX = fovX;
            Light.fovY = fovY;
            Light.zNear = zNear;
            Light.zFar = zFar;
            
            // if we want to make this support multiple lights, make this array
            lightBuffer = new Bitmap(bufferWidth, bufferHeight);
            Light.width = bufferWidth;
            Light.height = bufferHeight;
    }

    public Light(float x, float y, float z, float angleHorizontal,
            float angleVertical, float zAngle) {
        posVector = new Vector4f(x, y, z, 1);
        lightTransformation = new Transformation3D();
        lightTransformation.addRotationAroundX(-angleVertical);
        lightTransformation.addRotationAroundY(-angleHorizontal);
        lightTransformation.addRotationAroundZ(-zAngle);
        lightTransformation.addPerspectiveProjection(fovX, fovY, zNear, zFar);
    }
    
    protected Bitmap getLightBuffer() {
        return lightBuffer;
    }
    
    public void clearLightBuffer() {
        lightBuffer.clearDepthBuffer();
    }

    public Vector4f getPositionVector() {
        return posVector;
    }
    
    public Transformation3D getTransformation() {
        return lightTransformation;
    }
    
    public void drawShadowMap(Graphics2D g2d) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        byte[] displayComponents = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = (int) (-127.5 * lightBuffer.getDepthBufferValue(x, y) + 127.5);
                lightBuffer.drawPixel(x, y, (byte) 255, (byte) color, (byte) color, (byte) color);
            }
        }
        
        lightBuffer.copyToByteArray(displayComponents);
        g2d.drawImage(image, 0, 0, null);
        
        for (int y = 0; y < height; y++) {
            lightBuffer.drawScanLine(y, 0, width - 1, Color.black);
        }
    }
    
}
