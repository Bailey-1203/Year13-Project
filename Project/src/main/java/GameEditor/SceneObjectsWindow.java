package GameEditor;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;
import iso.GameObject;
import iso.Window;

import java.util.List;
import java.util.Objects;

public class SceneObjectsWindow {

    private static final String tag = "ObjectList";

    public void imgui() {
        ImGui.begin("Object List");

        List<GameObject> gameObjects = Window.getScene().getObjects();
        int i = 0;
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.getSerialise()) {
                continue;
            } else if (Objects.equals(gameObject.getObjectName(), "imageMetaSaver")) {
                continue;
            }
            boolean tree = doTreenode(gameObject, i);
            if (tree) {
                ImGui.treePop();
            }
            i++;
        }

        ImGui.end();
    }

    private boolean doTreenode(GameObject gameObject, int Index) {
        ImGui.pushID(Index);
        boolean bool = ImGui.treeNodeEx(
                gameObject.getObjectName(),
                ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding |
                        ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth,
                gameObject.getObjectName());
        ImGui.popID();

        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayloadObject(tag, gameObject);
            ImGui.text(gameObject.getObjectName());
            ImGui.endDragDropSource();
        }

        if (ImGui.beginDragDropTarget()) {
            Object object = ImGui.acceptDragDropPayload(tag);
            if (object != null) {
                object.getClass().isAssignableFrom(GameObject.class);
            }
            ImGui.endDragDropTarget();
        }
        return bool;
    }
}
