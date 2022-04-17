package Scenes;

import imgui.ImGui;
import iso.GameObject;
import iso.Window;
import utility.FileExplorer;

import javax.swing.*;

public class InitalScene extends SceneConstructor{
    public InitalScene() {

    }

    @Override
    public void init(Scene scene) {

    }

    @Override
    public void LoadResources(Scene scene) {

    }

    @Override
    public void imgui() {
        ImGui.begin("Start");
        if (ImGui.button("Load existing Game")){
            String Path;
            Path = FileExplorer.fileExplorer(JFileChooser.DIRECTORIES_ONLY);
            if (Path != null) {
                FileExplorer.InitalCreateGame(Path);
                Window.setRootFolder(Path);
                Window.ChangeScene(new LevelEditor(Path), Path);
            }else {
                System.out.println("loading exited");
            }
        }else if (ImGui.button("Create new Game")) {
            String Path;
            Path = FileExplorer.fileExplorer(JFileChooser.DIRECTORIES_ONLY);
            if (Path != null) {
                FileExplorer.InitalCreateGame(Path);
                Window.setRootFolder(Path);
                Window.ChangeScene(new LevelEditor(Path), Path);
            }else {
                System.out.println("creation exited");
            }
        }

        ImGui.end();
    }

    @Override
    public void updateGameobjCol(int Vindex, float opacity) {

    }

    @Override
    public void addSprites(String Filepath) {

    }

    @Override
    public boolean inEditor() {
        return false;
    }

    @Override
    public GameObject getImageMetaSaver() {
        return null;
    }

    @Override
    public GameObject getSoundMetaSaver() {
        return null;
    }
}
