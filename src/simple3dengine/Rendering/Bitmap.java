/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Rendering;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;
import simple3dengine.Math.Gradient;

/**
 *
 * @author Saku
 */
//THIS CLASS IS PROTECTED
class Bitmap {
//NO PUBLIC MODIFIER HERE
//Only to  used within rendering package

    private final byte components[];
    private float zBuffer[];
    private final int width;
    private final int height;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        components = new byte[width * height * 4];
        zBuffer = new float[width * height];

        Arrays.fill(components, (byte) 0x0);
        Arrays.fill(zBuffer, 1);
    }

    public Bitmap(BufferedImage img) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        components = new byte[width * height * 4];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                byte[] bytes = ByteBuffer.allocate(4).putInt(img.getRGB(x, y)).array();
                drawPixel(x, y, bytes[0], bytes[3], bytes[2], bytes[1]);
            }
        }
    }

    public void clearDepthBuffer() {
        Arrays.fill(zBuffer, 1);
    }

    public void drawPixel(int x, int y, byte a, byte b, byte g, byte r) {
        int index = (x + y * width) * 4;
        components[index] = a;
        components[index + 1] = b;
        components[index + 2] = g;
        components[index + 3] = r;
    }

    public void drawScanLine(int yCoord, int xMin, int xMax, Color color) {
        if (yCoord <= height - 1 && yCoord >= 0) {
            for (int i = xMin; i <= xMax; i++) {
                if (i <= width - 1 && i >= 0) {
                    drawPixel(i, yCoord, (byte) color.getAlpha(), (byte) color.getBlue(), (byte) color.getGreen(), (byte) color.getRed());
                }
            }
        }
    }

    public void drawScanLine(int yCoord, int xMin, int xMax, float dB, Gradient gd) {
        //Color color = Color.white;
        if (yCoord <= height - 1 && yCoord >= 0) {
            for (int i = xMin; i <= xMax; i++) {
                if (i <= width - 1 && i >= 0) {
                    if (zBuffer[yCoord * width + i] >= dB) {
                        if (dB <= 1 && dB >= -1) {
                            zBuffer[yCoord * width + i] = (float) dB;
                        }

                    }
                }
                dB += gd.getDX();
            }
        }
    }

    public void drawScanLine(int yCoord, int xMin, int xMax, float u, float v, Gradient gu, Gradient gv, Texture texture, Gradient gd, float dB, Gradient gz, float z,
            Gradient gLX, Gradient gLY, Gradient gLZ, float lx, float ly, float lz, Light light) {
        int finalu;
        int finalv;

        int w = texture.getWidth();
        int h = texture.getHeight();

        Bitmap lightBuffer = light.getLightBuffer();
        int lightX;
        int lightY;
        //System.out.println(lx + " " + ly);
        if (yCoord <= height - 1 && yCoord >= 0) {
            for (int i = xMin; i <= xMax; i++) {
                if (i <= width - 1 && i >= 0) {
                    if (zBuffer[yCoord * width + i] >= dB) {
                        if (dB <= 1 && dB >= -1) {
                            finalu = (int) ((u / z) * w);
                            finalv = (int) (h - (v / z) * h);
                            lightX = (int) lx;
                            lightY = (int) ly;
                            //System.out.println(lightBuffer.getDepthBufferValue(lightX, lightY));
                            try {
                                if (checkTextureCoordinates(finalu, finalv, w, h)) {
                                    byte[] color = texture.getColor(finalu, finalv);
                                    drawPixel(i, yCoord, color[0], color[3], color[2], color[1]);
                                }

                                zBuffer[yCoord * width + i] = (float) dB;

                                // Nämä ovat varjostusta varten..ei toimi vielä
                                /// Selvitä miksi getColor funktio ei hyväksy 0,0 tai maksimikoordinaatteja!!!
                                /*if (checkTextureCoordinates(finalu, finalv, w, h)) {
                                    if (lightX >= 0 && lightY >= 0 && lightX <= 1000 && lightY <= 800) {
                                        if (lightBuffer.getDepthBufferValue(lightX, lightY) + 0.01 >= lz) {
                                            byte[] color = texture.getColor(finalu, finalv);
                                            drawPixel(i, yCoord, color[0], color[3], color[2], color[1]);
                                        } else {
                                            byte[] color = texture.getDarkColor(finalu, finalv, 0.4f);
                                            drawPixel(i, yCoord, color[0], color[3], color[2], color[1]);
                                        }
                                    } else {
                                        byte[] color = texture.getColor(finalu, finalv);
                                        drawPixel(i, yCoord, color[0], color[3], color[2], color[1]);
                                    }

                                    zBuffer[yCoord * width + i] = (float) dB;
                                }*/
                            } catch (Exception e) {
                                // Katso ettei tätä kutsuta kovin usein...

                                //System.out.println(e);
                                //System.out.println(lightX + " " + lightY);
                            }
                        }

                    }
                }
                u += gu.getDX();
                v += gv.getDX();
                dB += gd.getDX();
                z += gz.getDX();
                lx += gLX.getDX();
                ly += gLY.getDX();
                lz += gLZ.getDX();
            }
        }
    }

    public void copyToByteArray(byte[] dest) {
        for (int i = 0; i < width * height; i++) {
            dest[i * 3] = components[i * 4 + 1];
            dest[i * 3 + 1] = components[i * 4 + 2];
            dest[i * 3 + 2] = components[i * 4 + 3];
        }
    }

    public void setDepthBufferValue(int x, int y, float value) {
        zBuffer[y * width + x] = value;
    }

    public float getDepthBufferValue(int x, int y) {
        return zBuffer[y * width + x];
    }

    private boolean checkTextureCoordinates(float u, float v, int width, int height) {
        return (u < width && u > 0 && v < height && v > 0);
    }

}
