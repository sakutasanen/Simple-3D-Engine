/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3dengine.Rendering.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import simple3dengine.Math.Vector2f;
import simple3dengine.Math.Vector4f;

/**
 *
 * @author Saku
 */
public class OBJLoader {

    private Scanner fileReader;
    private List<String> stringArray;

    private List<Vector4f> vertices;
    private List<Integer> vertexIndexList;

    private List<Vector2f> uvCoordinatesList;
    private List<Integer> uvIndexList;

    private List<Vector4f> normalVectorList;
    private List<Integer> normalVectorIndexList;
    
    public OBJLoader(String filePath) {
        stringArray = new ArrayList<String>();

        vertices = new ArrayList<Vector4f>();
        vertexIndexList = new ArrayList<Integer>();

        uvCoordinatesList = new ArrayList<Vector2f>();
        uvIndexList = new ArrayList<Integer>();
        
        normalVectorList = new ArrayList<Vector4f>();
        normalVectorIndexList = new ArrayList<Integer>();
        try {
            fileReader = new Scanner(new File(filePath));
        } catch (FileNotFoundException ex) {
        }
        createStringArray();
    }

    public void loadVertices() {
        float x;
        float y;
        float z;
        float w = 1;
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).equals("v")) {
                x = Float.parseFloat(stringArray.get(i + 1));
                y = Float.parseFloat(stringArray.get(i + 2));
                z = Float.parseFloat(stringArray.get(i + 3));
                vertices.add(new Vector4f(x, y, z, w));
            }
        }
        loadVertexIndices();
    }

    public void loadUVCoordinates() {
        float u;
        float v;
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).equals("vt")) {
                u = Float.parseFloat(stringArray.get(i + 1));
                v = Float.parseFloat(stringArray.get(i + 2));
                uvCoordinatesList.add(new Vector2f(u, v));
            }
        }
        loadTextureIndices();
    }
    
    public void loadNormalVectors() {
        float x;
        float y;
        float z;
        float w = 1;
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).equals("vn")) {
                x = Float.parseFloat(stringArray.get(i + 1));
                y = Float.parseFloat(stringArray.get(i + 2));
                z = Float.parseFloat(stringArray.get(i + 3));
                normalVectorList.add(new Vector4f(x, y, z, w));
            }
        }
        loadNormalVectorIndices();
    }

    public Vector4f[] getVertices() {
        Vector4f[] result = new Vector4f[vertices.size()];
        int i = 0;
        for (Vector4f v : vertices) {
            result[i] = v;
            i++;
        }
        return result;
    }
    
    public Vector4f[] getNormalVectors() {
        Vector4f[] result = new Vector4f[normalVectorList.size()];
        int i = 0;
        for (Vector4f v : normalVectorList) {
            result[i] = v;
            i++;
        }
        return result;
    }

    public Vector2f[] getUVCoordinates() {
        Vector2f[] result = new Vector2f[uvCoordinatesList.size()];
        int i = 0;
        for (Vector2f v : uvCoordinatesList) {
            result[i] = v;
            i++;
        }
        return result;
    }

    public int[] getVertexIndices() {
        int[] indices = new int[vertexIndexList.size()];
        int i = 0;
        for (int index : vertexIndexList) {
            indices[i] = index;
            i++;
        }
        return indices;
    }

    public int[] getUVIndices() {
        int[] indices = new int[uvIndexList.size()];
        int i = 0;
        for (int index : uvIndexList) {
            indices[i] = index;
            i++;
        }
        return indices;
    }
    
    public int[] getNormalVectorIndices() {
        int[] indices = new int[normalVectorIndexList.size()];
        int i = 0;
        for (int index : normalVectorIndexList) {
            indices[i] = index;
            i++;
        }
        return indices;
    }

    private void loadVertexIndices() {
        String[] result1;
        String[] result2;
        String[] result3;
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).equals("f")) {
                result1 = stringArray.get(i + 1).split("/");
                result2 = stringArray.get(i + 2).split("/");
                result3 = stringArray.get(i + 3).split("/");
                if (!result1[0].isEmpty()) {
                    vertexIndexList.add(Integer.parseInt(result1[0]));
                }
                if (!result2[0].isEmpty()) {
                    vertexIndexList.add(Integer.parseInt(result2[0]));
                }
                if (!result3[0].isEmpty()) {
                    vertexIndexList.add(Integer.parseInt(result3[0]));
                }
            }
        }
    }

    private void loadTextureIndices() {
        String[] result1;
        String[] result2;
        String[] result3;
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).equals("f")) {
                result1 = stringArray.get(i + 1).split("/");
                result2 = stringArray.get(i + 2).split("/");
                result3 = stringArray.get(i + 3).split("/");
                if (!result1[1].isEmpty()) {
                    uvIndexList.add(Integer.parseInt(result1[1]));
                }
                if (!result2[1].isEmpty()) {
                    uvIndexList.add(Integer.parseInt(result2[1]));
                }
                if (!result3[1].isEmpty()) {
                    uvIndexList.add(Integer.parseInt(result3[1]));
                }
            }
        }
    }
    
    private void loadNormalVectorIndices() {
        String[] result1;
        String[] result2;
        String[] result3;
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).equals("f")) {
                result1 = stringArray.get(i + 1).split("/");
                result2 = stringArray.get(i + 2).split("/");
                result3 = stringArray.get(i + 3).split("/");
                if (!result1[1].isEmpty()) {
                    normalVectorIndexList.add(Integer.parseInt(result1[2]));
                }
                if (!result2[1].isEmpty()) {
                    normalVectorIndexList.add(Integer.parseInt(result2[2]));
                }
                if (!result3[1].isEmpty()) {
                    normalVectorIndexList.add(Integer.parseInt(result3[2]));
                }
            }
        }
    }

    private void createStringArray() {
        String line;
        String[] array;
        while (fileReader.hasNext()) {
            line = fileReader.nextLine();
            if (line != null && line.length() > 0) {
                array = line.split(" ");
                for (String s : array) {
                    stringArray.add(s);
                }
            }
        }
        fileReader.close();
        removeEmptyStrings();
    }

    private void removeEmptyStrings() {
        for (int i = 0; i < stringArray.size(); i++) {
            if (stringArray.get(i).isEmpty()) {
                stringArray.remove(i);
            }
        }
    }

}
