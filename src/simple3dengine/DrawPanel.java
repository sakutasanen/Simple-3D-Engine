/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine;

import simple3dengine.Rendering.Model.OBJLoader;
import simple3dengine.Rendering.Model.Model3D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import simple3dengine.Math.Vector2f;
import simple3dengine.Rendering.FrameBuffer;
import simple3dengine.Rendering.Light;
import simple3dengine.Rendering.Texture;

/**
 *
 * @author Saku
 */
public class DrawPanel extends JPanel implements Runnable, KeyListener {
    
    private Thread thread;
    private Model3D model;
    private Model3D model2;
    private Camera3D camera;
    private Light light;
    private FrameBuffer frameBuffer;
    
    private float angleX = 0;
    private float angleY = 0;
    private float angleZ = 0;
    
    //private float cameraX = 0;
    private float cameraX = -40;
    private float cameraY = 0;
    private float cameraZ = 0;
    
    private float cameraAngle1 = -29;
    private float cameraAngle2 = -33;
    private float cameraAngle3 = 0;
    //private float cameraAngle1 = 0;
    //private float cameraAngle2 = -29;
    //private float cameraAngle3 = 0;
    
    private boolean upPressed = false;
    private boolean downPressed = false;
    
    public DrawPanel(float fovX, float fovY) {
        // Both buffers must be same size !!
        frameBuffer = new FrameBuffer(700,600, fovX, fovY, 40f, 140f);
        Light.initializeLight(700, 600, fovX, fovY, 40f, 140f);
        
        camera = new Camera3D(cameraX, cameraY, cameraZ, cameraAngle1, cameraAngle2, cameraAngle3);
        camera.update(cameraX, cameraY, cameraZ, cameraAngle1, cameraAngle2, cameraAngle3);
        
        light = new Light(0f, 0f, 0f, 0f, -29f, 0f);
        //light = null;
        
        OBJLoader objModel = new OBJLoader("resources/farmhouse.obj");
        OBJLoader objModel2 = new OBJLoader("resources/textureplane.obj");
        
        // if model doesn't have texture coordinates, don't use this constructor
        model = new Model3D(0, -50, 60, 1.0f, objModel, new Texture("resources/farmhouseTexture1.jpg"));
        model2 = new Model3D(0, -50, 60, 60, objModel2, new Texture("resources/grassTexture.jpg"));
        
        //OBJLoader objModel = new OBJLoader("Rabbit.obj");
        //model = new Model3D(0, 0, 50, 30, objModel, new Texture("rabbitTexture.png"));
        
        model.setXAngle(0);
        model.setYAngle(0);
        model.setZAngle(0);  
        
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        
        /*
        light.clearLightBuffer();
        frameBuffer.renderToShadowMap(model2, light);
        frameBuffer.renderToShadowMap(model, light);
        */
        //light.drawShadowMap(g2d);
        
        frameBuffer.show(g2d);
    }
    
    @Override
    public void run() {
        angleY = 0;
        angleZ = 100;
        model.setXAngle(0);
        model.setYAngle(-90);
        model.setZAngle(0);
        
        model2.setXAngle(-90);
        
        while(true) {
            angleX++;
            angleY += 1.0;
            angleZ++;
            
            //System.out.println(cameraAngle1);
            //System.out.println(cameraAngle2);
            //model.setWorldPosX(model.getWorldPosX()-0.1);
            
            // uncomment these to get it spin again
            //model.setYAngle(angleY);
            //model2.setYAngle(angleY);
            
            //model.setZAngle(angleY);
            //model.setZAngle(angleY);
            if (angleX >= 360) {
                angleX = 0;
            }
            if (angleY <= -360) {
                angleY = 0;
            }
            if (angleZ >= 140) {
                angleZ = 0;
            }
            
            if (upPressed) {
                cameraX -= camera.getNormalVector().getX();
                cameraZ += camera.getNormalVector().getZ();
                cameraY -= camera.getNormalVector().getY();
            }
            
            if (downPressed) {
                cameraX += camera.getNormalVector().getX();
                cameraZ -= camera.getNormalVector().getZ();
                cameraY += camera.getNormalVector().getY();
            }
            
            // Selvitä miksi kamera pitää päivittää ennen renderöintiä!
            camera.update(cameraX, cameraY, cameraZ, cameraAngle1, cameraAngle2, cameraAngle3);
            frameBuffer.clear(Color.black);

            //light.clearLightBuffer();
            //frameBuffer.renderToShadowMap(model2, light);
            //frameBuffer.renderToShadowMap(model, light);

            frameBuffer.renderTextureModel(model2, camera, light);
            
            camera.update(cameraX, cameraY, cameraZ, cameraAngle1, cameraAngle2, cameraAngle3);
            
            frameBuffer.renderTextureModel(model, camera, light);
            
            frameBuffer.updateBuffer();
            
            repaint();
            /*try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }*/
        }
    }      

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        if (KeyEvent.VK_UP == e.getKeyCode()) {
            upPressed = true;
        }
        
        if (KeyEvent.VK_DOWN == e.getKeyCode()) {
            downPressed = true;
        }
        
        if (KeyEvent.VK_A == e.getKeyCode()) {
            cameraAngle1 += 0.5;
        }
        
        if (KeyEvent.VK_D == e.getKeyCode()) {
            cameraAngle1 -= 0.5;
        }
        
        if (KeyEvent.VK_W == e.getKeyCode()) {
            cameraAngle2 += 0.5;
        }
        
        if (KeyEvent.VK_S == e.getKeyCode()) {
            cameraAngle2 -= 0.5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
         if (KeyEvent.VK_UP == e.getKeyCode()) {
             upPressed = false;
         }
         if (KeyEvent.VK_DOWN == e.getKeyCode()) {
             downPressed = false;
         }
    }
}
