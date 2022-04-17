package late;

import iso.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utility.AssetPool;
import utility.Cal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Drawer {

    private static final int Max = 1000;

    private static final List<DrawLines> lines = new ArrayList<>();

    private static final float[] Varray = new float[Max*6*2];
    private static final Shader shader = AssetPool.getShader("assets/shaders/LinesDefault.glsl");

    private static int VaoID;
    private static int VboID;

    private static boolean init = false;

    public static void init() {
        VaoID = glGenVertexArrays();
        glBindVertexArray(VaoID);

        VboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,VboID);
        glBufferData(GL_ARRAY_BUFFER,Varray.length*Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0,3, GL_FLOAT,false, 6*Float.BYTES,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,3,GL_FLOAT,false, 6*Float.BYTES,3*Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void start() {
        if (!init) {
            init();
            init = true;
        }

        for (int i=0; i<lines.size(); i++) {
            if (lines.get(i).start() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void render() {
        if (lines.size() <= 0) {
            return;
        }

        int index = 0;

        for (DrawLines line : lines) {
            for (int i=0; i<2; i++) {
                Vector2f loc = i==0? line.getPos1() : line.getPos2();
                Vector3f col = line.getCol();

                Varray[index]   = loc.x;
                Varray[index+1] = loc.y;
                Varray[index+2] = -10.0f;

                Varray[index+3] = col.x;
                Varray[index+4] = col.y;
                Varray[index+5] = col.z;
                index += 6;
            }
        }
        glBindBuffer(GL_ARRAY_BUFFER, VboID);
        glBufferSubData(GL_ARRAY_BUFFER,0, Arrays.copyOfRange(Varray,0,lines.size()*2*6));

        shader.use();
        shader.uploadMat4f("uProj", Window.getScene().camera().getproj_mat());
        shader.uploadMat4f("uView", Window.getScene().camera().getview_mat());

        glBindVertexArray(VaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES,0,lines.size()*6*2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        shader.detach();
    }

    public static void addline(Vector2f pos1, Vector2f pos2) {
        addline(pos1,pos2, new Vector3f(0,1,0),1);
    }

    public static void addline(Vector2f pos1, Vector2f pos2, Vector3f col) {
        addline(pos1,pos2, col,1);
    }

    public static void addline(Vector2f pos1, Vector2f pos2, Vector3f col, int lft) {
        if (lines.size() >= Max) {
            return;
        }

        Drawer.lines.add(new DrawLines(pos1,pos2,col,lft));
    }

    public static void addBox(Vector2f origin,Vector2f die) {
        addBox(origin,die,0,new Vector3f(0,0,0),1);
    }

    public static void addBox(Vector2f origin,Vector2f die,float rotate) {
        addBox(origin,die,rotate,new Vector3f(0,0,0),1);
    }

    public static void addBox(Vector2f origin,Vector2f die,float rotate,Vector3f colour) {
        addBox(origin,die,rotate, colour,1);
    }

    public static void addBox(Vector2f origin,Vector2f die,float rotate,Vector3f colour, int lt) {
        Vector2f bot = new Vector2f(origin).sub(new Vector2f(die).mul(0.5f));
        Vector2f top = new Vector2f(origin).add(new Vector2f(die).mul(0.5f));

        Vector2f[] points = {
            new Vector2f(bot.x,bot.y),
            new Vector2f(bot.x,top.y),
            new Vector2f(top.x,top.y),
            new Vector2f(top.x,bot.y)
        };

        if (rotate != 0) {
            for (Vector2f p:points) {
                Cal.rotate(p,rotate,origin);
            }
        }

        Drawer.addline(points[0],points[1],colour,lt);
        Drawer.addline(points[1],points[2],colour,lt);
        Drawer.addline(points[2],points[3],colour,lt);
        Drawer.addline(points[3],points[0],colour,lt);


    }

    public static void addCircle(Vector2f origin, float radius) {
        addCircle(origin,radius,new Vector3f(0,0,0), 1);
    }

    public static void addCircle(Vector2f origin, float radius, Vector3f colour) {
        addCircle(origin,radius,colour,1);
    }

    public static void addCircle(Vector2f origin, float radius, Vector3f colour, int lt) {
        Vector2f[] pts = new Vector2f[30];
        int change = 360/pts.length;
        int curAng = 0;

        for (int i=0; i < pts.length; i++) {
            Vector2f temp = new Vector2f(radius,0);
            Cal.rotate(temp,curAng, new Vector2f());
            pts[i] = new Vector2f(temp).add(origin);

            if (i>0) {
                addline(pts[i-1], pts[i],colour,lt);
            }
            curAng += change;
        }
        addline(pts[pts.length-1],pts[0],colour,lt);
    }
}
