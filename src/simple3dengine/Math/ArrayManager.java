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
public abstract class ArrayManager {
    
    private float matrixArray[][];
    private float vectorArray[];
    
    protected final void setVectorArrayElement(float element, int index) {
        vectorArray[index] = element;
    }
    
    protected final void setVectorArray(float floatArray[]) {
        this.vectorArray = floatArray;
    }
    
    protected final float[] getVectorArray() {
        return vectorArray;
    }
    
    protected final void setMatrixArrayElement(float element, int rowIndex, int columnIndex) {
        matrixArray[rowIndex][columnIndex] = element;
    }
    
    protected final void setMatrixArray(float matrixArray[][]) {
        this.matrixArray = matrixArray;
    }
    
    protected final float[][] getMatrixArray() {
        return matrixArray;
    }

}
