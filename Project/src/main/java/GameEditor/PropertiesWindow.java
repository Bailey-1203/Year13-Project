package GameEditor;

import PhysicsEngine.Components.BoxCollider;
import PhysicsEngine.Components.CircleCollider;
import PhysicsEngine.Components.RBody;
import imgui.ImGui;
import iso.GameObject;

public class PropertiesWindow {

    private GameObject active = null;
    private String tag = "";

    public PropertiesWindow() {

    }

    public void setName(String name) {
        tag = name;
    }

    public void update(float dt, GameObject active){
        this.active = active;
    }

    public void imgui(){
        if (active != null) {
            ImGui.begin(tag);
            if(ImGui.beginPopupContextWindow("CompMap")) {
                if (ImGui.menuItem("Add RBody")) {
                    if (active.getComponent(RBody.class) == null) {
                        RBody rBody = new RBody();
                        rBody.setDisplayImGui(true);
                        active.addComponent(rBody);
                    }
                }else if (ImGui.menuItem("Add Box Collider")) {
                    if (active.getComponent(BoxCollider.class) ==  null && active.getComponent(CircleCollider.class) == null) {
                        BoxCollider boxCollider = new BoxCollider();
                        boxCollider.setDisplayImGui(true);
                        active.addComponent(boxCollider);
                    }
                }else if (ImGui.menuItem("Add Circle Collider")) {
                    if (active.getComponent(BoxCollider.class) == null && active.getComponent(BoxCollider.class) == null) {
                        CircleCollider circleCollider = new CircleCollider();
                        circleCollider.setDisplayImGui(true);
                        active.addComponent(circleCollider);
                    }
                }

                ImGui.endPopup();
            }

            active.imgui();
            ImGui.end();
        }
    }

    public void setActive(GameObject active) {
        this.active = active;
    }
}
