/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Math;

/**
 *
 * @author Saku
 */
public class Vector2f extends ArrayManager {
    
    private float u;
    private float v;
    private float worldZ; 
    private float depthBufferValue;
    private Vector4f normalVector;
    private Vector3f lightVector;
    
        //Initializes zero vector
    public Vector2f() {
        setVectorArray(new float[2]);
    }
    
    public Vector2f(float x, float y) {
        float[] array = {x,y};
        setVectorArray(array);
    }
    
    public void setUVcoordinates(float u, float v) {
        this.u = u;
        this.v = v;
    }
    
    public float getU() {
        return u;
    }
    
    public float getV() {
        return v;
    }
    
    public float norm() { 
        return (float)Math.sqrt(Math.pow(getX(), 2)+Math.pow(getY(), 2));
    }

    public float getDepthBufferValue() {
        return depthBufferValue;
    }

    public void setDepthBufferValue(float depthBufferValue) {
        this.depthBufferValue = depthBufferValue;
    }

    public Vector4f getNormalVector() {
        return normalVector;
    }

    public void setNormalVector(Vector4f normalVector) {
        this.normalVector = normalVector;
    }
    
    public void roundComponents() {
        setVectorArrayElement((float)Math.ceil(getVectorArray()[0]), 0);
        setVectorArrayElement((float)Math.ceil(getVectorArray()[1]), 1);
    }
    
    public void sumToXComponent(float d) {
        setVectorArrayElement(getVectorArray()[0]+d, 0);
    }
    
    public void sumToYComponent(float d) {
        setVectorArrayElement(getVectorArray()[1]+d, 1);
    }
    
    public void mulXComponent(float d) {
        setVectorArrayElement(getVectorArray()[0]*d, 0);
    }
    
    public void mulYComponent(float d) {
        setVectorArrayElement(getVectorArray()[1]*d, 1);
    }
    
    ///Bunch of getters and setters
    public float getX() {
        return getVectorArray()[0];
    }
    
    public float getY() {
        return getVectorArray()[1];
    }
    
    public void setX(float x) {
        setVectorArrayElement(x, 0);
    }
    
    public void setY(float y) {
        setVectorArrayElement(y, 1);
    }

    public float getWorldZ() {
        return worldZ;
    }

    public void setWorldZ(float worldZ) {
        this.worldZ = worldZ;
    }

    public Vector3f getLightVector() {
        return lightVector;
    }

    public void setLightVector(Vector3f lightVector) {
        this.lightVector = lightVector;
    }
    
    @Override
    public String toString() {
        return "Vector2d{" + getX() + "," + getY() + '}';
    }
}
