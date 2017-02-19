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
public class MathProvider {

    //subtracts vector vec2 from vec1
    public static Vector3f vectorDiffrence(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(vec1.getX()-vec2.getX(),vec1.getY()-vec2.getY(),vec1.getZ()-vec2.getZ());
    }
    
    public static Vector4f vectorDiffrence(Vector4f vec1, Vector4f vec2) {
        return new Vector4f(vec1.getX()-vec2.getX(),vec1.getY()-vec2.getY(),vec1.getZ()-vec2.getZ(), 1);
    }
    
    //returns dot product of current vector and desired vector
    public static float dotProduct(Vector3f vec1, Vector3f vec2) {
        return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY() + vec1.getZ() * vec2.getZ();
    }

    public static float dotProduct(Vector2f vec1, Vector2f vec2) {
        return vec1.getX() * vec2.getX() + vec1.getY() * vec2.getY();
    }

    //Very unoptimized matrix vector product algorithms
    public static Vector3f matrixVectorProduct(Matrix3x3f matrix, Vector3f vec) {
        Vector3f result = new Vector3f();
        float[][] m = matrix.getMatrixArray();

        result.setX(m[0][0] * vec.getX() + m[0][1] * vec.getY() + m[0][2] * vec.getZ());
        result.setY(m[1][0] * vec.getX() + m[1][1] * vec.getY() + m[1][2] * vec.getZ());
        result.setZ(m[2][0] * vec.getX() + m[2][1] * vec.getY() + m[2][2] * vec.getZ());

        return result;
    }

    public static Vector4f matrixVectorProduct(Matrix4x4f matrix, Vector4f vec) {
        Vector4f result = new Vector4f();
        float[][] m = matrix.getMatrixArray();

        result.setX(m[0][0] * vec.getX() + m[0][1] * vec.getY() + m[0][2] * vec.getZ() + m[0][3] * vec.getW());
        result.setY(m[1][0] * vec.getX() + m[1][1] * vec.getY() + m[1][2] * vec.getZ() + m[1][3] * vec.getW());
        result.setZ(m[2][0] * vec.getX() + m[2][1] * vec.getY() + m[2][2] * vec.getZ() + m[2][3] * vec.getW());
        result.setW(m[3][0] * vec.getX() + m[3][1] * vec.getY() + m[3][2] * vec.getZ() + m[3][3] * vec.getW());

        return result;
    }

    //Matrix products
    public static Matrix3x3f matrixProduct(Matrix3x3f m1, Matrix3x3f m2) {
        return new Matrix3x3f(matrixVectorProduct(m1, m2.getColumn(0)), matrixVectorProduct(m1, m2.getColumn(1)), matrixVectorProduct(m1, m2.getColumn(2)));
    }
    
    public static Matrix4x4f matrixProduct(Matrix4x4f m1, Matrix4x4f m2) {
        return new Matrix4x4f(matrixVectorProduct(m1, m2.getColumn(0)), matrixVectorProduct(m1, m2.getColumn(1)), matrixVectorProduct(m1, m2.getColumn(2)), matrixVectorProduct(m1, m2.getColumn(3)));
    }

}
