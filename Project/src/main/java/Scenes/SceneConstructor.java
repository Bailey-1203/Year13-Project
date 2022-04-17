package Scenes;

import iso.GameObject;

public abstract class SceneConstructor {
    public abstract void init(Scene scene);
    public abstract void LoadResources(Scene scene);
    public abstract void imgui();

    public void getClicked() {
    }

    public abstract void updateGameobjCol(int Vindex, float opacity);

    public abstract void addSprites(String Filepath);

    public abstract boolean inEditor();

    public abstract GameObject getImageMetaSaver();

    public abstract GameObject getSoundMetaSaver();

}
