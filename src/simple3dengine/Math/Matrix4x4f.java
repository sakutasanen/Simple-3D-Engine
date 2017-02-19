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
public class Matrix4x4f extends ArrayManager {

    public Matrix4x4f() {
        setMatrixArray(new float[4][4]);
    }

    public Matrix4x4f(float[][] matrixArray) {
        setMatrixArray(matrixArray);
    }

    public Matrix4x4f(Vector4f v, Vector4f u, Vector4f w, Vector4f x) {
        float[][] matrixArray = new float[4][4];

        matrixArray[0][0] = v.getVectorArray()[0];
        matrixArray[1][0] = v.getVectorArray()[1];
        matrixArray[2][0] = v.getVectorArray()[2];
        matrixArray[3][0] = v.getVectorArray()[3];

        matrixArray[0][1] = u.getVectorArray()[0];
        matrixArray[1][1] = u.getVectorArray()[1];
        matrixArray[2][1] = u.getVectorArray()[2];
        matrixArray[3][1] = u.getVectorArray()[3];

        matrixArray[0][2] = w.getVectorArray()[0];
        matrixArray[1][2] = w.getVectorArray()[1];
        matrixArray[2][2] = w.getVectorArray()[2];
        matrixArray[3][2] = w.getVectorArray()[3];
        
        matrixArray[0][3] = x.getVectorArray()[0];
        matrixArray[1][3] = x.getVectorArray()[1];
        matrixArray[2][3] = x.getVectorArray()[2];
        matrixArray[3][3] = x.getVectorArray()[3];

        setMatrixArray(matrixArray);
    }

    public void initIdentity() {
        setMatrixArray(new float[4][4]);

        set(1, 0, 0);
        set(1, 1, 1);
        set(1, 2, 2);
        set(1, 3, 3);
    }

    public float get(int rowIndex, int columnIndex) {
        return getMatrixArray()[rowIndex][columnIndex];
    }
    
    public Vector4f getColumn(int index) {
        return new Vector4f(getMatrixArray()[0][index],getMatrixArray()[1][index],getMatrixArray()[2][index],getMatrixArray()[3][index]);
    }

    public void set(float element, int rowIndex, int columnIndex) {
        setMatrixArrayElement(element, rowIndex, columnIndex);
    }

    @Override
    public String toString() {
        return "Matrix4x4d{" + Arrays.toString(getMatrixArray()[0]) + ", " + Arrays.toString(getMatrixArray()[1]) + ", " + Arrays.toString(getMatrixArray()[2]) + Arrays.toString(getMatrixArray()[3]) + '}';
    }

}
