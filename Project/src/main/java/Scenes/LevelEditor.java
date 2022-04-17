package Scenes;

import GameEditor.ImportWindow;
import PhysicsEngine.Components.BoxCollider;
import PhysicsEngine.Components.CircleCollider;
import PhysicsEngine.Components.RBody;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import iso.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utility.AssetPool;
import utility.FileExplorer;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;



public class LevelEditor extends SceneConstructor {

    private final ArrayList<SpriteSheet> spriteSheets = new ArrayList<>();
    private SpriteSheet sprites;
    private GameObject cont, imageMetaSaver, SoundMetaSaver;
    private Scene scene;

    public LevelEditor() {

    }

    public LevelEditor(String filename) {

    }

    @Override
    public void init(Scene scene) {
        this.scene = scene;
        cont = scene.CreateObj("Editor");
        cont.setNotSerialise();
        SpriteSheet controllerTexture = AssetPool.getSpriteSheet("assets/textures/arrow.png");

        cont.addComponent(new controller());
        cont.addComponent(new Grid());
        cont.addComponent(new EditorCam(scene.camera()));
        cont.addComponent(new ControllerSystem(controllerTexture));
        cont.addComponent(new MoveController(controllerTexture.getsprite(1), Window.getImGuilayer().getPropertiesWindow()));
        cont.addComponent(new ScaleController(controllerTexture.getsprite(2), Window.getImGuilayer().getPropertiesWindow()));
        cont.getComponent(EditorCam.class).setDisplayImGui(true);
        scene.addGOTS(cont);

        boolean foundImg = false;
        boolean foundSnd = false;
        int a = 0;
        for (GameObject gameObject : scene.getObjects()) {
            if (Objects.equals(gameObject.getObjectName(), "imageMetaSaver")) {
                foundImg = true;
                imageMetaSaver = gameObject;
                a++;
            } else if (Objects.equals(gameObject.getObjectName(), "SoundMetaSaver")) {
                foundSnd = true;
                SoundMetaSaver = gameObject;
                a++;
            }

            if (a > 1) {
                break;
            }
        }

        if (!foundImg) {
            System.out.println("run");
            imageMetaSaver = scene.CreateObj("imageMetaSaver");
            scene.addGOTS(imageMetaSaver);
        } else {
            File folder = new File(Window.getRootFolder() + "\\assets");
            File[] files = folder.listFiles();
            assert files != null : "Folder '"+Window.getRootFolder() + "\\assets' does not exist";
            for (File file : files) {
                if (file.isFile()) {
                    for (Component c : imageMetaSaver.getComponents()) {
                        if (c.getClass() == ImageSaveComp.class && Objects.equals(c.getName(), file.getName())) {
                            AssetPool.addSpriteSheet(file.getAbsolutePath(),
                                    new SpriteSheet(AssetPool.getTexture(file.getAbsolutePath()),
                                            c.getdata().x, c.getdata().y, c.getdata().w, c.getdata().z));
                            sprites = AssetPool.getSpriteSheet(file.getAbsolutePath());
                            spriteSheets.add(sprites);
                        }
                    }
                }
            }
        }

        if (!foundSnd) {
            SoundMetaSaver = scene.CreateObj("SoundMetaSaver");
            scene.addGOTS(SoundMetaSaver);
            System.out.println("non-existant");
        } else {
            File folder = new File(Window.getRootFolder() + "\\sounds");
            File[] files = folder.listFiles();
            assert files != null: "Folder '"+Window.getRootFolder() + "\\sounds' does not exist";
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                    for (Component c : SoundMetaSaver.getComponents()) {
                        if (c.getClass() == SoundSaver.class && Objects.equals(c.getName(), file.getName())) {
                            AssetPool.addSound(file.getAbsolutePath(), ((SoundSaver) c).Loops());
                        }
                    }
                }
            }
        }

    }

    @Override
    public void LoadResources(Scene scene) {

        AssetPool.getShader("assets/shaders/default_new.glsl");

        AssetPool.addSpriteSheet("assets/textures/arrow.png",
                new SpriteSheet(AssetPool.getTexture("assets/textures/arrow.png"),
                        24, 48, 0, 3));

        AssetPool.addSpriteSheet("assets/textures/spr.png",
                new SpriteSheet(AssetPool.getTexture("assets/textures/spr.png"),
                        60, 60, 0, 32));

        for (GameObject obj : scene.getObjects()) {
            if (obj.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
                if (sprite.getTexture() != null) {
                    sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilePath()));
                }
            }

            if (obj.getComponent(SpriteState.class) != null) {
                SpriteState state = obj.getComponent(SpriteState.class);
                state.refreshTextures();
            }
        }
    }

    @Override
    public void imgui() {

        ImGui.begin("Import");

        if (ImGui.beginPopupContextWindow("CompMap")) {
            if (ImGui.menuItem("Import Sprites")) {
                String FilePath = FileExplorer.fileExplorer(JFileChooser.FILES_ONLY);
                if (FilePath != null) {
                    FileExplorer.DuplicateImageFile(FilePath, Window.getRootFolder() + "\\assets\\");
                    File file = new File(FilePath);
                    ImportWindow.setActive(Window.getRootFolder() + "\\assets\\" + file.getName(), ImportWindow.SPRITE_CREATION);
                } else {
                    System.out.println("Image Not loaded");
                }
            } else if (ImGui.menuItem("Import Texture")) {
                String FilePath;
                FilePath = FileExplorer.fileExplorer(JFileChooser.FILES_ONLY);
                System.out.println(FilePath);
                if (FilePath != null) {
                    FileExplorer.DuplicateImageFile(FilePath, Window.getRootFolder() + "\\assets\\");
                    File file = new File(FilePath);
                    ImportWindow.setActive(Window.getRootFolder() + "\\assets\\" + file.getName(), ImportWindow.TEXTURE_CREATION);
                } else {
                    System.out.println("Image Not loaded");
                }
            } else if (ImGui.menuItem("Import Sound")) {
                String FilePath;
                FilePath = FileExplorer.fileExplorer(JFileChooser.FILES_ONLY);
                System.out.println(FilePath);
                if (FilePath != null) {
                    FileExplorer.DuplicateImageFile(FilePath, Window.getRootFolder() + "\\Sounds\\");
                    File file = new File(FilePath);
                    ImportWindow.setActive(Window.getRootFolder() + "\\Sounds\\" + file.getName(), ImportWindow.SOUND_CREATION);
                } else {
                    System.out.println("Sound Not loaded");
                }
            }

            ImGui.endPopup();
        }

        ImGui.beginTabBar("TabBar");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        int j = 1;
        //e = 5
        if (ImGui.beginTabItem("Textures")) {
            for (SpriteSheet spriteSheet : spriteSheets) {
                boolean Rb = false;
                boolean Bc = false;
                boolean Cc = false;
                for (Component c : imageMetaSaver.getComponents()) {
                    if (c.getClass() == ImageSaveComp.class) {
                        if (new File(spriteSheet.getsprite(0).getTexture().getFilePath()).getName().equals(c.getName())) {
                            Rb = ((ImageSaveComp) c).isRb();
                            Bc = ((ImageSaveComp) c).isBc();
                            Cc = ((ImageSaveComp) c).isCc();
                        }
                    }
                }
                if (ImGui.collapsingHeader("SpriteSheet " + j)) {
                    for (int i = 0; i < spriteSheet.size(); i++) {
                        Sprite sprite = spriteSheet.getsprite(i);
                        float spriteWidth = sprite.getWidth() * 2;
                        float spriteHeight = sprite.getHeight() * 2;
                        int id = sprite.getTexID();
                        Vector2f[] texCoords = sprite.getTex_ver();

                        ImGui.pushID(i);
                        if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                            GameObject object = Prefabs.genObjectClicked(sprite, 0.25f, 0.25f, Window.getImGuilayer().getEditorControl().getVindex());
                            if (Rb) {
                                object.addComponent(new RBody());
                                object.getComponent(RBody.class).setDisplayImGui(true);
                                if (Bc) {
                                    object.addComponent(new BoxCollider());
                                    object.getComponent(BoxCollider.class).setDisplayImGui(true);
                                } else if (Cc) {
                                    object.addComponent(new CircleCollider());
                                    object.getComponent(CircleCollider.class).setDisplayImGui(true);
                                }
                            }
                            cont.getComponent(controller.class).objPicked(object);
                        }
                        ImGui.popID();

                        ImVec2 lastButtonPos = new ImVec2();
                        ImGui.getItemRectMax(lastButtonPos);
                        float lastButtonX2 = lastButtonPos.x;
                        float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                        if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                            ImGui.sameLine();
                        }
                    }
                    ImGui.newLine();
                }
                j++;
            }
            ImGui.endTabItem();
        }
        if (ImGui.beginTabItem("Prefabs")) {
            List<GameObject> gameObjects = Window.getScene().getImGameObjects();
            int i = 0;
            for (GameObject gameObject : gameObjects) {
                Sprite sprite = gameObject.getComponent(ThumbNailTextureStore.class).getSprite();
                float spriteWidth = sprite.getWidth() * 2;
                float spriteHeight = sprite.getHeight() * 2;
                int id = sprite.getTexID();
                Vector2f[] texCoords = sprite.getTex_ver();

                ImGui.pushID(i);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = gameObject.Duplicate();
                    cont.getComponent(controller.class).objPicked(object);
                }
                ImGui.popID();
                i++;

                ImVec2 lastButtonPos = new ImVec2();
                ImGui.getItemRectMax(lastButtonPos);
                float lastButtonX2 = lastButtonPos.x;
                float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                if (i + 1 < gameObjects.size() && nextButtonX2 < windowX2) {
                    ImGui.sameLine();
                }
            }
            ImGui.endTabItem();
        }
        if (ImGui.beginTabItem("Sounds")) {
            Collection<Sound> sounds = AssetPool.getAllSounds();
            for (Sound sound : sounds) {
                File temp = new File(sound.getFilepath());
                if (ImGui.button(temp.getName())) {
                    if (!sound.isPlaying()) {
                        sound.play();
                    } else {
                        sound.Stop();
                    }
                }

                if (ImGui.getContentRegionAvailX() > 100) {
                    ImGui.sameLine();
                }
            }

            ImGui.endTabItem();
        }
        ImGui.endTabBar();

        ImGui.end();
    }

    @Override
    public void updateGameobjCol(int Vindex, float opacity) {
        for (GameObject obj : this.scene.getObjects()) {
            if (!Objects.equals(obj.getObjectName(), "Editor")) {
                SpriteRenderer spr = obj.getComponent(SpriteRenderer.class);
                Vector4f colour = spr.getOriginColour();
                if (obj.transform.Zindex != Vindex && obj.transform.Zindex != 21) {
                    spr.setColour(new Vector4f(colour.x * opacity, colour.y * opacity, colour.z * opacity, opacity));
                } else {
                    spr.setColour(new Vector4f(colour.x, colour.y, colour.z, colour.w));
                }
            }
        }
    }

    @Override
    public void getClicked() {
        Vector2f mouseloc = new Vector2f(MouseDetection.calWorldPos());
        boolean clicked = false;
        MoveController controllerMove = cont.getComponent(MoveController.class);
        ScaleController controllerScale = cont.getComponent(ScaleController.class);
        boolean contuseMove = controllerMove.gethover();
        boolean contuseScale = controllerScale.gethover();
        if (!contuseMove && !contuseScale) {
            for (GameObject obj : this.scene.getObjects()) {
                if (obj.transform.Zindex == Window.getImGuilayer().getEditorControl().getVindex()) {
                    float objx = obj.transform.Pos.x;
                    float objy = obj.transform.Pos.y;
                    if (mouseloc.x > objx - obj.transform.Sca.x / 2 && mouseloc.x < objx + obj.transform.Sca.x / 2 &&
                            mouseloc.y > objy - obj.transform.Sca.y / 2 && mouseloc.y < objy + obj.transform.Sca.y / 2) {
                        if (scene.getActive() != null) {
                            scene.getActive().getComponent(SpriteRenderer.class).setColour(scene.getActive().getComponent(SpriteRenderer.class).getOriginColour());
                        }
                        scene.setActive(obj);
                        Vector4f colour = obj.getComponent(SpriteRenderer.class).getColour();
                        obj.getComponent(SpriteRenderer.class).setColour(new Vector4f(colour.x, colour.y, colour.z * 0.5f, colour.w));
                        clicked = true;
                        break;
                    }
                }
            }
        }

        if (!clicked && !contuseMove && !contuseScale) {
            if (scene.getActive() != null) {
                scene.getActive().getComponent(SpriteRenderer.class).setColour(scene.getActive().getComponent(SpriteRenderer.class).getOriginColour());
            }
            scene.setActive(null);
        }
    }

    public void addSprites(String Filepath) {
        sprites = AssetPool.getSpriteSheet(Filepath);
        spriteSheets.add(sprites);
    }

    @Override
    public boolean inEditor() {
        return true;
    }

    @Override
    public GameObject getImageMetaSaver() {
        return imageMetaSaver;
    }

    @Override
    public GameObject getSoundMetaSaver() {
        return SoundMetaSaver;
    }
}
