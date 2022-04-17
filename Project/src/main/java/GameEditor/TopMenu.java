package GameEditor;

import Observers.EventSystem;
import Observers.Events.Event;
import Observers.Events.Events;
import imgui.ImGui;

public class TopMenu {
    public void imgui() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save")) {
                EventSystem.notify(null,new Event(Events.SaveLevel));
            }else if (ImGui.menuItem("Save As")) {
                EventSystem.notify(null,new Event(Events.SaveAsLevel));
            }else if (ImGui.menuItem("Load")) {
                EventSystem.notify(null,new Event(Events.LoadLevel));
            }else if (ImGui.menuItem("Help")){
                DisplayHelpPanel();
            }
            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }

    public void DisplayHelpPanel() {
        ImGui.begin("Help Panel");
        MImGui.drawText2Col("Delete :", "Deletes the Current Active Object");
        ImGui.end();
    }
}
