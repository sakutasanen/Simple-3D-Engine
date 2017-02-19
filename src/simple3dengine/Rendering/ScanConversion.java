/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Rendering;

import java.awt.Color;
import simple3dengine.Math.Gradient;
import simple3dengine.Math.Vector2f;
import simple3dengine.Math.Vector3f;

/**
 *
 * @author Saku
 */
/// THIS CLASS IS PROTECTED 
class ScanConversion {
/// NO PUBLIC MODIFIER HERE

    protected final Bitmap bitmap;

    protected final int width;
    protected final int height;
    protected final int centerX;
    protected final int centerY;

    protected ScanConversion(int width, int height) {
        //store width and height values
        this.width = width;
        this.height = height;
        this.centerX = width / 2;
        this.centerY = height / 2;

        bitmap = new Bitmap(width, height);
    }

    //this algorithm rasterizes triangle. Not the best algorithm but works
    protected void rasterizeTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Color color, Texture texture, Light light) {

        Vector2f[] vecArray = sortVectors(v1, v2, v3);
        v1 = vecArray[0];
        v2 = vecArray[1];
        v3 = vecArray[2];

        Gradient uGradient;
        Gradient vGradient;
        Gradient zGradient;
        Gradient lightXGradient;
        Gradient lightYGradient;
        Gradient lightZGradient;
        Gradient depthBufferGradient = new Gradient(v1, v2, v3,
                v1.getDepthBufferValue(),
                v2.getDepthBufferValue(),
                v3.getDepthBufferValue());

        if (light != null) {
            Vector3f l1 = v1.getLightVector();
            Vector3f l2 = v2.getLightVector();
            Vector3f l3 = v3.getLightVector();
            
            lightXGradient = new Gradient(v1, v2, v3, l1.getX(), l2.getX(), l3.getX());
            lightYGradient = new Gradient(v1, v2, v3, l1.getY(), l2.getY(), l3.getY());
            lightZGradient = new Gradient(v1, v2, v3, l1.getZ(), l2.getZ(), l3.getZ());
        } else {
            lightXGradient = null;
            lightYGradient = null;
            lightZGradient = null;
        }

        if (texture != null) {
            uGradient = new Gradient(v1, v2, v3, v1.getU() / v1.getWorldZ(), v2.getU() / v2.getWorldZ(), v3.getU() / v3.getWorldZ());
            vGradient = new Gradient(v1, v2, v3, v1.getV() / v1.getWorldZ(), v2.getV() / v2.getWorldZ(), v3.getV() / v3.getWorldZ());
            zGradient = new Gradient(v1, v2, v3, 1.0f / v1.getWorldZ(), 1.0f / v2.getWorldZ(), 1.0f / v3.getWorldZ());
        } else {
            uGradient = null;
            vGradient = null;
            zGradient = null;
        }

        if (v2.getY() == v3.getY()) {
            fillBottomFlatTriangle(v1, v2, v3, color, texture, uGradient, vGradient, depthBufferGradient, zGradient, lightXGradient, lightYGradient, lightZGradient, light);
        } /* check for trivial case of top-flat triangle */ else if (v1.getY() == v2.getY()) {
            fillTopFlatTriangle(v1, v2, v3, color, texture, uGradient, vGradient, depthBufferGradient, zGradient, lightXGradient, lightYGradient, lightZGradient, light);
        } else {
            /* general case - split the triangle in a topflat and bottom-flat one */
            Vector2f v4 = new Vector2f(
                    (v1.getX() + (((v2.getY() - v1.getY()) / (v3.getY() - v1.getY())) * (v3.getX() - v1.getX()))), v2.getY());
            v1.roundComponents();
            v2.roundComponents();
            v3.roundComponents();
            v4.roundComponents();
            fillBottomFlatTriangle(v1, v2, v4, color, texture, uGradient, vGradient, depthBufferGradient, zGradient, lightXGradient, lightYGradient, lightZGradient, light);
            fillTopFlatTriangle(v2, v4, v3, color, texture, uGradient, vGradient, depthBufferGradient, zGradient, lightXGradient, lightYGradient, lightZGradient, light);
            //System.out.println(v2.getY() + " " + v4.getX());
        }
    }

    // This is used primarly for drawing shadow map!
    protected void rasterizeTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Bitmap bitmap) {
        Vector2f[] vecArray = sortVectors(v1, v2, v3);
        v1 = vecArray[0];
        v2 = vecArray[1];
        v3 = vecArray[2];

        Gradient gd = new Gradient(v1, v2, v3, v1.getDepthBufferValue(), v2.getDepthBufferValue(), v3.getDepthBufferValue());

        if (v2.getY() == v3.getY()) {
            fillBottomFlatTriangle(v1, v2, v3, bitmap, gd);
        } /* check for trivial case of top-flat triangle */ else if (v1.getY() == v2.getY()) {
            fillTopFlatTriangle(v1, v2, v3, bitmap, gd);
        } else {
            /* general case - split the triangle in a topflat and bottom-flat one */
            Vector2f v4 = new Vector2f(
                    (v1.getX() + (((v2.getY() - v1.getY()) / (v3.getY() - v1.getY())) * (v3.getX() - v1.getX()))), v2.getY());
            v1.roundComponents();
            v2.roundComponents();
            v3.roundComponents();
            v4.roundComponents();
            fillBottomFlatTriangle(v1, v2, v4, bitmap, gd);
            fillTopFlatTriangle(v2, v4, v3, bitmap, gd);
        }
    }

    ///v1 has the highest y value and v2,v3 are on the same line
    protected void fillBottomFlatTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Color color, Texture texture, Gradient gu, Gradient gv, Gradient gd, Gradient gz,
    Gradient gLX, Gradient gLY, Gradient gLZ, Light light) {
        if (v2.getX() > v3.getX()) {
            Vector2f tmp1;
            Vector2f tmp2;
            tmp1 = v2;
            tmp2 = v3;
            v2 = tmp2;
            v3 = tmp1;
        }
        float invslope1 = ((v2.getX() - v1.getX()) / (v2.getY() - v1.getY()));
        float invslope2 = ((v3.getX() - v1.getX()) / (v3.getY() - v1.getY()));

        float curx1 = v1.getX();
        float curx2 = v1.getX();

        float u = v1.getU() / v1.getWorldZ();
        float v = v1.getV() / v1.getWorldZ();

        float dB = v1.getDepthBufferValue();
        float z = 1.0f / v1.getWorldZ();
        
        float lx = v1.getLightVector().getX();
        float ly = v1.getLightVector().getY();
        float lz = v1.getLightVector().getZ();

        for (float scanlineY = v1.getY(); scanlineY <= v2.getY(); scanlineY++) {
            if (color != null) {
                bitmap.drawScanLine((int) scanlineY, (int) (curx1), (int) (curx2), color);
            } else {
                bitmap.drawScanLine((int) scanlineY, (int) (curx1), (int) (curx2), u, v, gu, gv, texture, gd, dB, gz, z,
                        gLX, gLY, gLZ, lx, ly, lz, light);
                
                u += gu.getDY();
                v += gv.getDY();
                dB += gd.getDY();
                z += gz.getDY();
                lx += gLX.getDY();
                ly += gLY.getDY();
                lz += gLZ.getDY();

                u += gu.getDX() * invslope1;
                v += gv.getDX() * invslope1;
                dB += gd.getDX() * invslope1;
                z += gz.getDX() * invslope1;
                lx += gLX.getDX() * invslope1;
                ly += gLY.getDX() * invslope1;
                lz += gLZ.getDX() * invslope1;
            }

            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

    // This is used primarly for drawing shadow map!
    protected void fillBottomFlatTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Bitmap bitmap, Gradient gd) {
        if (v2.getX() > v3.getX()) {
            Vector2f tmp1;
            Vector2f tmp2;
            tmp1 = v2;
            tmp2 = v3;
            v2 = tmp2;
            v3 = tmp1;
        }
        float invslope1 = ((v2.getX() - v1.getX()) / (v2.getY() - v1.getY()));
        float invslope2 = ((v3.getX() - v1.getX()) / (v3.getY() - v1.getY()));

        float curx1 = v1.getX();
        float curx2 = v1.getX();

        float dB = v1.getDepthBufferValue();

        for (float scanlineY = v1.getY(); scanlineY <= v2.getY(); scanlineY++) {
            bitmap.drawScanLine((int) scanlineY, (int) (curx1), (int) (curx2), dB, gd);            

            dB += gd.getDY();
            dB += gd.getDX() * invslope1;
            
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

    //v3 has the lowest y value and v1,v2 are on the same line 
    protected void fillTopFlatTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Color color, Texture texture, Gradient gu, Gradient gv, Gradient gd, Gradient gz,
            Gradient gLX, Gradient gLY, Gradient gLZ, Light light) {
        if (v1.getX() > v2.getX()) {
            Vector2f tmp1;
            Vector2f tmp2;
            tmp1 = v1;
            tmp2 = v2;
            v1 = tmp2;
            v2 = tmp1;
        }
        float invslope1 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
        float invslope2 = (v3.getX() - v2.getX()) / (v3.getY() - v2.getY());

        float curx1 = v3.getX();
        float curx2 = v3.getX();

        float u = v3.getU() / v3.getWorldZ();
        float v = v3.getV() / v3.getWorldZ();

        float dB = v3.getDepthBufferValue();
        float z = 1.0f / v3.getWorldZ();
        
        float lx = v3.getLightVector().getX();
        float ly = v3.getLightVector().getY();
        float lz = v3.getLightVector().getZ();

        for (float scanlineY = v3.getY(); scanlineY >= v1.getY(); scanlineY--) {
            
            if (color != null) {
                bitmap.drawScanLine((int) scanlineY, (int) (curx1), (int) (curx2), color);
            } else {
                bitmap.drawScanLine((int) scanlineY, (int) (curx1), (int) (curx2), u, v, gu, gv, texture, gd, dB, gz, z,
                        gLX, gLY, gLZ, lx, ly, lz, light);
                
                u -= gu.getDY();
                v -= gv.getDY();
                dB -= gd.getDY();
                z -= gz.getDY();
                lx -= gLX.getDY();
                ly -= gLY.getDY();
                lz -= gLZ.getDY();

                u -= gu.getDX() * invslope1;
                v -= gv.getDX() * invslope1;
                dB -= gd.getDX() * invslope1;
                z -= gz.getDX() * invslope1;
                lx -= gLX.getDX() * invslope1;
                ly -= gLY.getDX() * invslope1;
                lz -= gLZ.getDX() * invslope1;
            }

            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    // This is used primarly for drawing shadow map!
    protected void fillTopFlatTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Bitmap bitmap, Gradient gd) {
        if (v1.getX() > v2.getX()) {
            Vector2f tmp1;
            Vector2f tmp2;
            tmp1 = v1;
            tmp2 = v2;
            v1 = tmp2;
            v2 = tmp1;
        }
        float invslope1 = (v3.getX() - v1.getX()) / (v3.getY() - v1.getY());
        float invslope2 = (v3.getX() - v2.getX()) / (v3.getY() - v2.getY());

        float curx1 = v3.getX();
        float curx2 = v3.getX();

        float dB = v3.getDepthBufferValue();

        for (float scanlineY = v3.getY(); scanlineY >= v1.getY(); scanlineY--) {
            bitmap.drawScanLine((int) scanlineY, (int) (curx1), (int) (curx2), dB, gd);

            dB -= gd.getDY();
            dB -= gd.getDX() * invslope1;

            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    private Vector2f[] sortVectors(Vector2f v1, Vector2f v2, Vector2f v3) {
        Vector2f[] vecArray = new Vector2f[3];

        // we want v1 to be topmost vector so we sort these to order v1,v2,v3 according to y coordinate
        vecArray[0] = v1;
        vecArray[1] = v2;
        vecArray[2] = v3;

        int topMostIndex = 0;
        float topMostY = 0;

        for (int i = 0; i < vecArray.length; i++) {
            if (i == 0) {
                topMostY = vecArray[i].getY();
                topMostIndex = i;
            } else if (vecArray[i].getY() < topMostY) {
                topMostY = vecArray[i].getY();
                topMostIndex = i;
            }
        }
        v1 = vecArray[topMostIndex];
        if (topMostIndex == 0) {
            if (vecArray[1].getY() < vecArray[2].getY()) {
                v2 = vecArray[1];
                v3 = vecArray[2];
            } else {
                v2 = vecArray[2];
                v3 = vecArray[1];
            }
        } else if (topMostIndex == 1) {
            if (vecArray[0].getY() < vecArray[2].getY()) {
                v2 = vecArray[0];
                v3 = vecArray[2];
            } else {
                v2 = vecArray[2];
                v3 = vecArray[0];
            }
        } else if (vecArray[0].getY() < vecArray[1].getY()) {
            v2 = vecArray[0];
            v3 = vecArray[1];
        } else {
            v2 = vecArray[1];
            v3 = vecArray[0];
        }

        Vector2f[] result = {v1, v2, v3};

        return result;

    }

}
