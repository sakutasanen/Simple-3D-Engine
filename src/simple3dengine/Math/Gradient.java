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
public class Gradient {
    
    // c = interpolant
    
    private float dx; /// dc/dx
    private float dy; /// dc/dy
    
    public Gradient(Vector2f top, Vector2f middle, Vector2f bottom, float c0, float c1, float c2) {
        
        float x0 = top.getX();
        float x1 = middle.getX();
        float x2 = bottom.getX();
        
        float y0 = top.getY();
        float y1 = middle.getY();
        float y2 = bottom.getY();
        
        dx = ((c1-c2)*(y0-y2)-(c0-c2)*(y1-y2))/((x1-x2)*(y0-y2)-(x0-x2)*(y1-y2));
        dy = ((c1-c2)*(x0-x2)-(c0-c2)*(x1-x2))/((x0-x2)*(y1-y2)-(x1-x2)*(y0-y2));
    }
    
    public float getDX() {
        return dx;
    }
    
    public float getDY() {
        return dy;
    }
    
}
