package iso;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private final Matrix4f Proj_Mat;
    private final Matrix4f View_Mat;
    private final Matrix4f Proj_inv_Mat;
    private final Matrix4f View_inv_Mat;
    private final Vector2f Proj_Siz;
    public Vector2f pos_vec;
    private float zoom = 1.0f;

    public Camera(Vector2f pos) {
        this.pos_vec = pos;
        this.Proj_Mat = new Matrix4f();
        this.View_Mat = new Matrix4f();
        this.Proj_inv_Mat = new Matrix4f();
        this.View_inv_Mat = new Matrix4f();
        float pro_Hei = 3;
        float pro_Wid = 6;
        this.Proj_Siz = new Vector2f(pro_Wid, pro_Hei);
        adjust_proj();
    }

    public void adjust_proj() {
        Proj_Mat.identity();
        Proj_Mat.ortho(0.0f, Proj_Siz.x * this.zoom, 0.0f, Proj_Siz.y * this.zoom, 0.0f, 100.0f);
        Proj_Mat.invert(Proj_inv_Mat);
    }

    public Matrix4f getview_mat() {
        Vector3f cam_front = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cam_up = new Vector3f(0.0f, 1.0f, 0.0f);

        this.View_Mat.identity();
        View_Mat.lookAt(new Vector3f(pos_vec.x, pos_vec.y, 20.0f),
                cam_front.add(pos_vec.x, pos_vec.y, 0.0f),
                cam_up);
        this.View_Mat.invert(View_inv_Mat);
        return this.View_Mat;
    }

    public Matrix4f getproj_mat() {
        return this.Proj_Mat;
    }

    public Matrix4f getProj_inv_Mat() {
        return this.Proj_inv_Mat;
    }

    public Matrix4f getView_inv_Mat() {
        return this.View_inv_Mat;
    }

    public Vector2f getProj_Siz() {
        return this.Proj_Siz;
    }

    public Vector2f getPos_vec() {
        return this.pos_vec;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

}
