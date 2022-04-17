package late;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DrawLines {

    private final Vector2f pos1;
    private final Vector2f pos2;
    private final Vector3f col;
    private int lt;

    public DrawLines(Vector2f pos1, Vector2f pos2, Vector3f colour, int lt) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.col  = colour;
        this.lt   = lt;
    }

    public int start() {
        this.lt--;
        return this.lt;
    }

    public Vector2f getPos1() {
        return pos1;
    }

    public Vector2f getPos2() {
        return pos2;
    }

    public Vector3f getCol() {
        return col;
    }


}
