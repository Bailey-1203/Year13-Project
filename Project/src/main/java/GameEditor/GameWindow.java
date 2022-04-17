package GameEditor;

import Observers.EventSystem;
import Observers.Events.Event;
import Observers.Events.Events;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import iso.Window;
import iso.MouseDetection;
import org.joml.Vector2f;

public class GameWindow {

    private float lx,rx,ty,by;
    private boolean Running = false;

    public void imgui() {
        ImGui.begin("Game", ImGuiWindowFlags.NoScrollbar|ImGuiWindowFlags.NoScrollWithMouse|ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Run", "", Running, !Running)) {
            Running = true;
            EventSystem.notify(null, new Event(Events.BeginSim));
        }
        if (ImGui.menuItem("Stop","", !Running, Running)) {
            Running = false;
            EventSystem.notify(null,new Event(Events.StopSim));
        }
        if (ImGui.menuItem("Pause","", !Running, Running)) {
            Running = false;
            EventSystem.notify(null,new Event(Events.PauseSim));
        }
        ImGui.endMenuBar();

        ImVec2 WinSize = GetWindowSize();
        ImVec2 WinPos = GetWindowPosition(WinSize);

        ImGui.setCursorPos(WinPos.x,WinPos.y);
        ImVec2 TLCorner = new ImVec2();
        ImGui.getCursorScreenPos(TLCorner);
        TLCorner.x -= ImGui.getScrollX();
        TLCorner.y -= ImGui.getScrollY();
        lx = TLCorner.x;
        by = TLCorner.y;
        rx = TLCorner.x+WinSize.x;
        ty = TLCorner.y+WinSize.y;
        int TexID = Window.getFrameBuffer().getTexID();
        ImGui.image(TexID, WinSize.x, WinSize.y,0,1,1,0);
        MouseDetection.setViewPos(new Vector2f(TLCorner.x,TLCorner.y));
        MouseDetection.setViewSize(new Vector2f(WinSize.x,WinSize.y));

        ImGui.end();
    }

    private ImVec2 GetWindowSize(){
        ImVec2 winsize = new ImVec2();
        ImGui.getContentRegionAvail(winsize);
        winsize.x -= ImGui.getScrollX();
        winsize.y -= ImGui.getScrollY();

        float AWidth = winsize.x;
        float AHeight = AWidth/Window.getAspectRatio();
        if (AHeight > winsize.y) {
            AHeight = winsize.y;
            AWidth = AHeight*Window.getAspectRatio();
        }

        return new ImVec2(AWidth,AHeight);
    }

    private ImVec2 GetWindowPosition(ImVec2 size) {
        ImVec2 winsize = new ImVec2();
        ImGui.getContentRegionAvail(winsize);
        winsize.x -= ImGui.getScrollX();
        winsize.y -= ImGui.getScrollY();

        float ViewX = (winsize.x/2.0f)-(size.x/2.0f);
        float ViewY = (winsize.y/2.0f)-(size.y/2.0f);

        return new ImVec2(ViewX+ImGui.getCursorPosX(),ViewY+ImGui.getCursorPosY());
    }

    public boolean WantMouseClicked() {
        return MouseDetection.get_x() >= lx && MouseDetection.get_x() <= rx &&
                MouseDetection.get_y() >= by && MouseDetection.get_y() <= ty;
    }
}

//720.0lx, 2544.0rx, 331.5by, 1357.5ty
