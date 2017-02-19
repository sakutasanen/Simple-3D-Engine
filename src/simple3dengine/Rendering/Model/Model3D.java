/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Rendering.Model;

import simple3dengine.Camera3D;
import simple3dengine.Math.MathProvider;
import simple3dengine.Math.Matrix4x4f;
import simple3dengine.Math.Transformation3D;
import simple3dengine.Math.Vector2f;
import simple3dengine.Math.Vector3f;
import simple3dengine.Math.Vector4f;
import simple3dengine.Rendering.Light;
import simple3dengine.Rendering.Texture;

/**
 *
 * @author Saku
 */
public class Model3D {

    private Texture texture;
    
    private final Vector4f[] vertices;
    private final Vector3f[] transformedVertices;
    private final Vector3f[] transformedVertices2;
    private final Vector3f posVector;

    private Vector2f[] uvCoordinates; 
    private Vector4f[] normalVectors;
    private Vector4f[] transformedNormalVectors;
    
    private final int[] vertexIndices;
    private int[] uvIndices;
    private int[] normalVectorIndices;
    
    private float xAngle;
    private float yAngle;
    private float zAngle;
    
    private float scale;
    
    private final OBJLoader loader;

    public Model3D(float worldPosX, float worldPosY, float worldPosZ, float scale, OBJLoader objModel) {
        posVector = new Vector3f(worldPosX, worldPosY, worldPosZ);

        loader = objModel;
        objModel.loadVertices();
        objModel.loadNormalVectors();
        
        vertices = objModel.getVertices();
        vertexIndices = objModel.getVertexIndices();
        normalVectorIndices = objModel.getNormalVectorIndices();
        normalVectors = objModel.getNormalVectors();
        
        transformedVertices = new Vector3f[vertices.length];
        transformedVertices2 = new Vector3f[vertices.length];
        transformedNormalVectors = new Vector4f[normalVectors.length];
        
        uvCoordinates = null;
        uvIndices = null;
        
        xAngle = 0;
        yAngle = 0;
        zAngle = 0;
        
        this.scale = scale;
    }
    
    public Model3D(float worldPosX, float worldPosY, float worldPosZ, float scale, OBJLoader objModel, Texture texture) {
        this(worldPosX, worldPosY, worldPosZ, scale, objModel);
        this.texture = texture;
        objModel.loadUVCoordinates();
        uvCoordinates = objModel.getUVCoordinates();
        uvIndices = objModel.getUVIndices();
    }

    //first we apply transformations then we project to the screen
    public Vector3f[] transformVerticesToViewport(float fovX, float fovY, float nearZ, float farZ, Camera3D camera) {
        Transformation3D worldToViewportTransf;
        Matrix4x4f modelToWorldTransf = getModelToWorldTransformation().getMatrix();
        
        ////Camera transformation
        worldToViewportTransf = camera.getWorldToCameraTransformation();
        worldToViewportTransf.addPerspectiveProjection(fovX, fovY, nearZ, farZ);

        int i = 0;
        Vector3f subtractVector;
        for (Vector4f v : vertices) {
            subtractVector = MathProvider.vectorDiffrence(MathProvider.matrixVectorProduct(modelToWorldTransf, v).perspectiveDivision(), camera.getCameraPosVector());
            transformedVertices[i] = MathProvider.matrixVectorProduct(worldToViewportTransf.getMatrix(), subtractVector.getHomogeneousVector()).perspectiveDivision();         
            i++;
        }
   
        int i2 = 0;
        for (Vector4f normal : normalVectors) {
            transformedNormalVectors[i2] = MathProvider.matrixVectorProduct(modelToWorldTransf, normal);
            i2++;
        }
        
        return transformedVertices;
    }
    
    public Vector3f[] transformVerticesToShadowMap(Light light) {
        Matrix4x4f modelToWorldTransf = getModelToWorldTransformation().getMatrix();
        
        Vector4f subtractVector;
        int i = 0;
        for (Vector4f v : vertices) {
            subtractVector = MathProvider.vectorDiffrence(MathProvider.matrixVectorProduct(modelToWorldTransf, v), light.getPositionVector());
            transformedVertices2[i] = MathProvider.matrixVectorProduct(light.getTransformation().getMatrix(), subtractVector).perspectiveDivision();
            i++;
        }
        
        return transformedVertices2;
    }
    
    public Transformation3D getModelToWorldTransformation() {
        Transformation3D modelToWorldTransf = new Transformation3D();
        
        modelToWorldTransf.addRotationAroundX(xAngle);
        modelToWorldTransf.addRotationAroundY(yAngle);
        modelToWorldTransf.addRotationAroundZ(zAngle);
        modelToWorldTransf.addScale(scale, scale, scale);
        modelToWorldTransf.addTranslation((int) getWorldPosX(), (int) getWorldPosY(), (int) getWorldPosZ());
        return modelToWorldTransf;
    }
    
    public Vector4f[] getTransformedNormalVectors() {
       return transformedNormalVectors;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector4f[] getNormalVectors() {
        return normalVectors;
    }
    
    public Vector4f[] getVertices() {
        return vertices;
    }

    public int[] getNormalVectorIndices() {
       return normalVectorIndices;
    }  
    
    public Vector2f[] getUVCoordinates() {
        return uvCoordinates;
    }
    
    public int[] getUVIndices() {
        return uvIndices;
    }
    
    public int[] getVertexIndices() {
        return vertexIndices;
    }
    
    public Vector3f getPositionVector() {
        return posVector;
    }

    public float getWorldPosX() {
        return posVector.getX();
    }

    public void setWorldPosX(float worldPosX) {
        posVector.setX(worldPosX);
    }

    public float getWorldPosY() {
        return posVector.getY();
    }

    public void setWorldPosY(float worldPosY) {
        posVector.setY(worldPosY);
    }

    public float getWorldPosZ() {
        return posVector.getZ();
    }

    public void setWorldPosZ(float worldPosZ) {
        posVector.setZ(worldPosZ);
    }

    public float getXAngle() {
        return xAngle;
    }

    public float getYAngle() {
        return yAngle;
    }

    public float getZAngle() {
        return zAngle;
    }

    public void setXAngle(float xAngle) {
        this.xAngle = xAngle;
    }

    public void setYAngle(float yAngle) {
        this.yAngle = yAngle;
    }

    public void setZAngle(float zAngle) {
        this.zAngle = zAngle;
    }

}
