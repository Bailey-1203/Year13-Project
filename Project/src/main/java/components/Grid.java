package components;

import iso.Camera;
import iso.Window;
import late.Drawer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utility.Constant;

public class Grid extends Component {


    @Override
    public void EditorUpdate(float delta_time) {
        Camera camera = Window.getScene().camera();
        Vector2f CamPos = camera.getPos_vec();
        Vector2f ProSiz = camera.getProj_Siz();

        Vector2f gridcount = new Vector2f(Constant.getGridCount());
        //float zoom = camera.getZoom();
        Vector2f PosStart = new Vector2f((Math.round(CamPos.x / gridcount.x) * gridcount.x),
                (Math.round(CamPos.y / gridcount.y) * gridcount.y)
        );

        Vector2f LineCount = new Vector2f((int) (ProSiz.x / gridcount.x), (int) (ProSiz.y / gridcount.y));

        Vector2f Dimentions = new Vector2f(ProSiz.x + gridcount.x, ProSiz.y + gridcount.y);

        float MaxL = Math.max(LineCount.x, LineCount.y);

        for (int i = 0; i < MaxL; i++) {
            float x = (PosStart.x + (gridcount.x * i) + 0.125f);
            float y = (PosStart.y + (gridcount.y * i) + 0.125f);

            if (i < LineCount.x) {
                Drawer.addline(new Vector2f(x, PosStart.y), new Vector2f(x, PosStart.y + Dimentions.y), new Vector3f(0, 0, 0));
            }
            if (i < LineCount.y) {
                Drawer.addline(new Vector2f(PosStart.x, y), new Vector2f(PosStart.x + Dimentions.x, y), new Vector3f(0, 0, 0));
            }
        }
    }

    /*public void update(float delta_time){
        Camera camera = Window.getScene().camera();
        Vector2f CamPos = camera.getPos_vec();
        Vector2f ProSiz = camera.getProj_Siz();

        Vector2i gridcount = new  Vector2i(Constant.getGridCount());
        //float zoom = camera.getZoom();
        Vector2i PosStart = new Vector2i((Math.round(CamPos.x/gridcount.x)*gridcount.x),
                (Math.round(CamPos.y/gridcount.y)*gridcount.y)
        );

        Vector2i LineCount = new Vector2i((int)(ProSiz.x/gridcount.x),(int)(ProSiz.y/gridcount.y));

        Vector2i Dimentions = new Vector2i((int)(ProSiz.x)+gridcount.x,(int)(ProSiz.y)+gridcount.y);

        int MaxL = Math.max(LineCount.x,LineCount.y);

        for (int i=0; i<MaxL; i++) {
            int x = (PosStart.x+(gridcount.x*i));
            int y = (PosStart.y+(gridcount.y*i));

            if (i<LineCount.x){
                Drawer.addline(new Vector2f(x,PosStart.y), new Vector2f(x,PosStart.y+Dimentions.y),new Vector3f(0,0,0));
            }
            if (i<LineCount.y) {
                Drawer.addline(new Vector2f(PosStart.x,y), new Vector2f(PosStart.x+Dimentions.x,y),new Vector3f(0,0,0));
            }
        }
    }*/
}
