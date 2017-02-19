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
import simple3dengine.Camera3D;
import simple3dengine.Math.MathProvider;
import simple3dengine.Math.Matrix4x4f;
import simple3dengine.Math.Vector2f;
import simple3dengine.Math.Vector3f;
import simple3dengine.Math.Vector4f;
import simple3dengine.Rendering.Model.Model3D;

/**
 *
 * @author Saku
 */
public class FrameBuffer extends ScanConversion {

    protected final BufferedImage frameBuffer;
    private final byte[] displayComponents;

    private final float fovX;
    private final float fovY;
    private final float nearZ;
    private final float farZ;

    public FrameBuffer(int width, int height, float fovX, float fovY, float nearZ, float farZ) {
        super(width, height);
        //we store field of view angles
        this.fovX = fovX;
        this.fovY = fovY;
        this.nearZ = nearZ;
        this.farZ = farZ;

        // we create buffer image where we are going to draw
        frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        // we create bitmap

        // components from framebuffer
        displayComponents = ((DataBufferByte) frameBuffer.getRaster().getDataBuffer()).getData();

        //clear the bitmap for drawing
        bitmap.copyToByteArray(displayComponents);
        bitmap.clearDepthBuffer();
    }

    public void drawLine(Vector2f v1, Vector2f v2, Color color) {
    }

    public void drawTriangle(Vector2f v1, Vector2f v2, Vector2f v3, Color color) {

    }

    public void renderWireframeModel(Model3D model, Camera3D camera, Color color) {

    }

    public void renderColorModel(Model3D model, Camera3D camera, Color color, Light light) {
        renderModel(model, camera, color, null, light);
    }

    public void renderTextureModel(Model3D model, Camera3D camera, Light light) {
        renderModel(model, camera, null, model.getTexture(), light);
    }

    public void renderToShadowMap(Model3D model, Light light) {
        Vector3f[] transformedVertices = model.transformVerticesToShadowMap(light);

        Vector2f[] vectors = new Vector2f[3];
        int[] vertexIndices = model.getVertexIndices();

        for (int i = 0; 3 <= vertexIndices.length - i; i += 3) {
            vectors[0] = transformedVertices[vertexIndices[i] - 1].ignoreZComponent();
            vectors[1] = transformedVertices[vertexIndices[i + 1] - 1].ignoreZComponent();
            vectors[2] = transformedVertices[vertexIndices[i + 2] - 1].ignoreZComponent();

            vectors[0].setDepthBufferValue(transformedVertices[vertexIndices[i] - 1].getZ());
            vectors[1].setDepthBufferValue(transformedVertices[vertexIndices[i + 1] - 1].getZ());
            vectors[2].setDepthBufferValue(transformedVertices[vertexIndices[i + 2] - 1].getZ());

            //System.out.println(vectors[0].getX() + " " + vectors[0].getY() + " " + vectors[0].getDepthBufferValue());
            if (checkTriangle(vectors[0], vectors[1], vectors[2])) {
                for (int i2 = 0; i2 < vectors.length; i2++) {

                    vectors[i2].mulXComponent(centerX);
                    vectors[i2].sumToXComponent(centerX);
                    vectors[i2].mulYComponent(-(centerY));
                    vectors[i2].sumToYComponent(centerY);
                }
                rasterizeTriangle(vectors[0], vectors[1], vectors[2], light.getLightBuffer());
            }
        }
    }

    private void renderModel(Model3D model, Camera3D camera, Color color, Texture texture, Light light) {
        Vector3f[] transformedVertices = model.transformVerticesToViewport(fovX, fovY, nearZ, farZ, camera);
        Vector4f[] transformedNormalVectors = model.getTransformedNormalVectors();
        Vector4f[] unTransformedVertices = model.getVertices();

        int[] vertexIndices = model.getVertexIndices();
        int[] uvIndices = model.getUVIndices();
        int[] normalIndices = model.getNormalVectorIndices();

        Vector2f[] uvCoordinates = model.getUVCoordinates();

        Vector2f[] vectors = new Vector2f[3];
        Vector4f[] shadowMapVectors = new Vector4f[3];
        Vector3f[] tShadowMapVect = new Vector3f[3];

        Matrix4x4f modelToWorldTransf = model.getModelToWorldTransformation().getMatrix();
        Vector4f subtractVector;

        for (int i = 0; 3 <= vertexIndices.length - i; i += 3) {
            shadowMapVectors[0] = unTransformedVertices[vertexIndices[i] - 1];
            shadowMapVectors[1] = unTransformedVertices[vertexIndices[i + 1] - 1];
            shadowMapVectors[2] = unTransformedVertices[vertexIndices[i + 2] - 1];

            if (light != null) {
                for (int index = 0; index < shadowMapVectors.length; index++) {
                    subtractVector = MathProvider.vectorDiffrence(MathProvider.matrixVectorProduct(modelToWorldTransf, shadowMapVectors[index]), light.getPositionVector());
                    shadowMapVectors[index] = MathProvider.matrixVectorProduct(light.getTransformation().getMatrix(), subtractVector);
                    tShadowMapVect[index] = shadowMapVectors[index].perspectiveDivision();
                    tShadowMapVect[index].mulXComponent(centerX);
                    tShadowMapVect[index].sumToXComponent(centerX);
                    tShadowMapVect[index].mulYComponent(-(centerY));
                    tShadowMapVect[index].sumToYComponent(centerY);
                }
            }

            vectors[0] = transformedVertices[vertexIndices[i] - 1].ignoreZComponent();
            vectors[1] = transformedVertices[vertexIndices[i + 1] - 1].ignoreZComponent();
            vectors[2] = transformedVertices[vertexIndices[i + 2] - 1].ignoreZComponent();

            if (light != null) {
                vectors[0].setLightVector(tShadowMapVect[0]);
                vectors[1].setLightVector(tShadowMapVect[1]);
                vectors[2].setLightVector(tShadowMapVect[2]);
            }
            
            vectors[0].setDepthBufferValue(transformedVertices[vertexIndices[i] - 1].getZ());
            vectors[1].setDepthBufferValue(transformedVertices[vertexIndices[i + 1] - 1].getZ());
            vectors[2].setDepthBufferValue(transformedVertices[vertexIndices[i + 2] - 1].getZ());

            vectors[0].setNormalVector(transformedNormalVectors[normalIndices[i] - 1]);
            vectors[1].setNormalVector(transformedNormalVectors[normalIndices[i + 1] - 1]);
            vectors[2].setNormalVector(transformedNormalVectors[normalIndices[i + 2] - 1]);

            if (uvIndices != null) {
                vectors[0].setUVcoordinates(uvCoordinates[uvIndices[i] - 1].getX(), uvCoordinates[uvIndices[i] - 1].getY());
                vectors[1].setUVcoordinates(uvCoordinates[uvIndices[i + 1] - 1].getX(), uvCoordinates[uvIndices[i + 1] - 1].getY());
                vectors[2].setUVcoordinates(uvCoordinates[uvIndices[i + 2] - 1].getX(), uvCoordinates[uvIndices[i + 2] - 1].getY());
            }
            
            if (checkTriangle(vectors[0], vectors[1], vectors[2])) {
                for (int i2 = 0; i2 < vectors.length; i2++) {
                    vectors[i2].mulXComponent(centerX);
                    vectors[i2].sumToXComponent(centerX);
                    vectors[i2].mulYComponent(-(centerY));
                    vectors[i2].sumToYComponent(centerY);
                }
                rasterizeTriangle(vectors[0], vectors[1], vectors[2], color, texture, light);
            }
        }
    }

    // Methods for simple culling, checks whether a triangle is within screen
    private boolean checkTriangle(Vector2f v1, Vector2f v2, Vector2f v3) {
        if (!isVectorIn(v1) && !isVectorIn(v2) && !isVectorIn(v3)) {
            return false;
        }

        return true;
    }

    private boolean isVectorIn(Vector2f vec2d) {
        return (vec2d.getX() >= -1 && vec2d.getX() <= 1
                && vec2d.getY() >= -1 && vec2d.getY() <= 1
                && vec2d.getDepthBufferValue() >= -1
                && vec2d.getDepthBufferValue() <= 1);
    }

    public void clear(Color color) {
        bitmap.clearDepthBuffer();
        for (int y = 0; y < height; y++) {
            bitmap.drawScanLine(y, 0, width - 1, color);
        }
    }

    public synchronized void updateBuffer() {
        bitmap.copyToByteArray(displayComponents);
    }

    public void show(Graphics2D g2d) {
        g2d.drawImage(frameBuffer, 0, 0, null);
    }

}
