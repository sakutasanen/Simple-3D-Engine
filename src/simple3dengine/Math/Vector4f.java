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
public class Vector4f extends ArrayManager {
    
        //Initializes zero vector
    public Vector4f() {
        setVectorArray(new float[4]);
    }
    
    public Vector4f(float x, float y, float z, float w) {
        float[] array = {x,y,z,w};
        setVectorArray(array);
    }
    
    public float norm() { 
        return (float)Math.sqrt(Math.pow(getX(), 2)+Math.pow(getY(), 2)+Math.pow(getZ(), 2)+Math.pow(getW(), 2));
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
    
    public float getW() {
        return getVectorArray()[3];
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
    
    public void setW(float w) {
        setVectorArrayElement(w, 3);
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
    
    public Vector3f perspectiveDivision() {
        Vector3f vec3 = new Vector3f(getX()/getW(), getY()/getW(), getZ()/getW());
        vec3.setWorldZ(getW());
        return vec3;
    }
    
    @Override
    public String toString() {
        return "Vector4d{" + getX() + "," + getY() + "," + getZ() + "," + getW() + '}';
    }
    
}
