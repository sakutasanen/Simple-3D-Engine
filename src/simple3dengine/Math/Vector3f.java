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
public class Vector3f extends ArrayManager {
    
    private float worldZ;
    
    //Initializes zero vector
    public Vector3f() {
        setVectorArray(new float[3]);
    }
    
    public Vector3f(float x, float y, float z) {
        float[] array = {x,y,z};
        setVectorArray(array);
    }
    
    public float norm() { 
        return (float)Math.sqrt(Math.pow(getX(), 2)+Math.pow(getY(), 2)+Math.pow(getZ(), 2));
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
    
    public float getZ() {
        return getVectorArray()[2];
    }
    
    public void setX(float x) {
        setVectorArrayElement(x, 0);
    }
    
    public void setY(float y) {
        setVectorArrayElement(y, 1);
    }
    
    public void setZ(float z) {
        setVectorArrayElement(z, 2);
    }
    
    public Vector4f getHomogeneousVector() {
        return new Vector4f(getX(),getY(),getZ(),1);
    }
    
    public Vector2f ignoreZComponent() {
        Vector2f vec2d = new Vector2f(getX(),getY());
        vec2d.setWorldZ(worldZ);
        return vec2d;
    }

    public float getWorldZ() {
        return worldZ;
    }

    public void setWorldZ(float worldZ) {
        this.worldZ = worldZ;
    }
    
    @Override
    public String toString() {
        return "Vector3d{" + getX() + "," + getY() + "," + getZ() + '}';
    }
    
}
