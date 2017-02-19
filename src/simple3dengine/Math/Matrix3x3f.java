/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Math;

import java.util.Arrays;

/**
 *
 * @author Saku
 */
public class Matrix3x3f extends ArrayManager {
    
    public Matrix3x3f() {
        setMatrixArray(new float[3][3]);
    }
    
    public Matrix3x3f(float[][] matrixArray) {
        setMatrixArray(matrixArray);
    }
    
    public Matrix3x3f(Vector3f v, Vector3f u, Vector3f w) {
        float[][] matrixArray = new float[3][3];
        
        matrixArray[0][0] = v.getVectorArray()[0];
        matrixArray[1][0] = v.getVectorArray()[1];
        matrixArray[2][0] = v.getVectorArray()[2];
        
        matrixArray[0][1] = u.getVectorArray()[0];
        matrixArray[1][1] = u.getVectorArray()[1];
        matrixArray[2][1] = u.getVectorArray()[2];
        
        matrixArray[0][2] = w.getVectorArray()[0];
        matrixArray[1][2] = w.getVectorArray()[1];
        matrixArray[2][2] = w.getVectorArray()[2];
        
        setMatrixArray(matrixArray);
    }
    
    public void initIdentity() {
        setMatrixArray(new float[3][3]);
        
        set(1,0,0);
        set(1,1,1);
        set(1,2,2);
    }
    
    public float get(int rowIndex, int columnIndex) {
        return getMatrixArray()[rowIndex][columnIndex];
    }
    
    public Vector3f getColumn(int index) {
        return new Vector3f(getMatrixArray()[0][index],getMatrixArray()[1][index],getMatrixArray()[2][index]);
    }
    
    public void set(float element, int rowIndex, int columnIndex) {
        setMatrixArrayElement(element, rowIndex, columnIndex);
    }

    @Override
    public String toString() {
        return "Matrix3x3d{" + Arrays.toString(getMatrixArray()[0]) + ", " + Arrays.toString(getMatrixArray()[1]) + ", " + Arrays.toString(getMatrixArray()[2]) +'}';
    }
    
    
    
}
