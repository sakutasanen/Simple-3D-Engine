/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine;

import simple3dengine.Math.MathProvider;
import simple3dengine.Math.Transformation3D;
import simple3dengine.Math.Vector3f;
import simple3dengine.Math.Vector4f;

/**
 *
 * @author Saku
 */
public class Camera3D {

    private Vector3f cameraPosVector; 
    private Vector4f normalVector;

    private float angleHorizontal;
    private float angleVertical;
    
    private Transformation3D worldToCameraTransformation;
    
    public Camera3D(float xPos, float yPos, float zPos, float angleHorizontal, float angleVertical, float zAngle) {
        cameraPosVector = new Vector3f(xPos,yPos,zPos);
        normalVector = new Vector4f(0, 0, 0.2f, 1);

        this.angleHorizontal = angleHorizontal;
        this.angleVertical = angleVertical;
        
        worldToCameraTransformation = new Transformation3D();
        worldToCameraTransformation.addRotationAroundZ(-zAngle);
        worldToCameraTransformation.addRotationAroundY(-angleHorizontal);
        worldToCameraTransformation.addRotationAroundX(-angleVertical);
        
        normalVector = MathProvider.matrixVectorProduct(worldToCameraTransformation.getMatrix(), normalVector);
    }

    public void update(float xPos, float yPos, float zPos, float angleHorizontal, float angleVertical, float zAngle) {
        cameraPosVector.setX(xPos);
        cameraPosVector.setY(yPos);
        cameraPosVector.setZ(zPos);

        this.angleHorizontal = angleHorizontal;
        this.angleVertical = angleVertical;

        worldToCameraTransformation.resetToIdentity();
        worldToCameraTransformation.addRotationAroundZ(-zAngle);
        worldToCameraTransformation.addRotationAroundY(-angleHorizontal);
        worldToCameraTransformation.addRotationAroundX(-angleVertical);
        
        normalVector = new Vector4f(0, 0, 0.2f, 1);
        normalVector = MathProvider.matrixVectorProduct(worldToCameraTransformation.getMatrix(), normalVector);
    }
    
    public Transformation3D getWorldToCameraTransformation() {
        return worldToCameraTransformation;
    }

    public Vector3f getCameraPosVector() {
        return cameraPosVector;
    }
    
    public Vector4f getNormalVector() {
        return normalVector;
    }
    
}
