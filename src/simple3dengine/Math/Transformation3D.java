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
public class Transformation3D {
    
    private Matrix4x4f transMatrix;
    
    public Transformation3D() {
        transMatrix = new Matrix4x4f();
        transMatrix.initIdentity();
    }
    
    public void addRotationAroundX(float angle) {
        Matrix4x4f matrix = new Matrix4x4f();
        matrix.initIdentity();
        
        float sin = (float)Math.sin(Math.toRadians(angle));
        float cos = (float)Math.cos(Math.toRadians(angle));
        
        matrix.set(cos, 1, 1);
        matrix.set(-sin, 2, 1);
        matrix.set(sin, 1, 2);
        matrix.set(cos, 2, 2);
        
        transMatrix = MathProvider.matrixProduct(matrix, transMatrix);
    }
    
    public void addRotationAroundY(float angle) {
        Matrix4x4f matrix = new Matrix4x4f();
        matrix.initIdentity();
        
        float sin = (float)Math.sin(Math.toRadians(angle));
        float cos = (float)Math.cos(Math.toRadians(angle));
        
        matrix.set(cos, 0, 0);
        matrix.set(sin, 2, 0);
        matrix.set(-sin, 0, 2);
        matrix.set(cos, 2, 2);
        
        transMatrix = MathProvider.matrixProduct(matrix, transMatrix);
    }
    
    public void addRotationAroundZ(float angle) {
        Matrix4x4f matrix = new Matrix4x4f();
        matrix.initIdentity();
        
        float sin = (float)Math.sin(Math.toRadians(angle));
        float cos = (float)Math.cos(Math.toRadians(angle));
        
        matrix.set(cos, 0, 0);
        matrix.set(-sin, 1, 0);
        matrix.set(sin, 0, 1);
        matrix.set(cos, 1, 1);
        
        transMatrix = MathProvider.matrixProduct(matrix, transMatrix);
    }
    
    public void addTranslation(int x, int y, int z) {
        Matrix4x4f matrix = new Matrix4x4f();
        matrix.initIdentity();
        
        matrix.set(x, 0, 3);
        matrix.set(y, 1, 3);
        matrix.set(z, 2, 3);
        
        transMatrix = MathProvider.matrixProduct(matrix, transMatrix);      
    }
    
    public void addScale(float x, float y, float z) {
        Matrix4x4f matrix = new Matrix4x4f();
        matrix.initIdentity();
        
        matrix.set(x, 0, 0);
        matrix.set(y, 1, 1);
        matrix.set(z, 2, 2);
        
        transMatrix = MathProvider.matrixProduct(matrix, transMatrix); 
    }
    
    public void addPerspectiveProjection(float fovX, float fovY, float zNear, float zFar) {
        Matrix4x4f matrix = new Matrix4x4f();
        float fX = (float)(1.0/Math.tan(Math.toRadians(fovX)/2.0));
        float fY = (float)(1.0/Math.tan(Math.toRadians(fovY)/2.0));

        float m = (2*zFar*zNear)/(zNear-zFar);
        float b = (m/zNear+1); 
        
        matrix.set(fX, 0, 0);
        matrix.set(fY, 1, 1);
        matrix.set(-b, 2, 2);
        matrix.set(m, 2, 3);
        matrix.set(1, 3, 2);

        transMatrix = MathProvider.matrixProduct(matrix, transMatrix);
    }
    
    public void resetToIdentity() {
        transMatrix.initIdentity();
    }
    
    public Matrix4x4f getMatrix() {
        return transMatrix;
    }

    @Override
    public String toString() {
        return "Transformation matrix: " + transMatrix.toString();
    } 
}
