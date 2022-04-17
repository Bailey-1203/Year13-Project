package components;

import org.joml.Vector2i;
import org.joml.Vector4i;

public class ImageSaveComp extends Component {

    Vector2i size;
    int count, separation;
    String Imagename;
    boolean Rb, Bc, Cc;

    public ImageSaveComp() {

    }

    public boolean isBc() {
        return Bc;
    }

    public boolean isCc() {
        return Cc;
    }

    public boolean isRb() {
        return Rb;
    }

    public void init(Vector2i s, int c, int sep, String n) {
        size = s;
        count = c;
        separation = sep;
        this.Imagename = n;
        Rb = false;
        Bc = false;
        Cc = false;
    }

    @Override
    public String getName() {
        return this.Imagename;
    }

    @Override
    public Vector4i getdata() {
        return new Vector4i(size, count, separation);
    }

    public void ComponentAdder(boolean a, boolean b, boolean c) {
        Rb = a;
        Bc = b;
        Cc = c;
    }

}
