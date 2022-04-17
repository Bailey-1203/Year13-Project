package GameEditor;

import PhysicsEngine.Components.PillBoxCollider;
import PhysicsEngine.Components.RBody;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImInt;
import iso.GameObject;
import iso.Prefabs;
import iso.Window;
import late.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import utility.AssetPool;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImportWindow {

    private static final String[] triggersVals = new String[12];
    private static final boolean[] enums = new boolean[12];
    public static Boolean Active = false;
    public static int SPRITE_CREATION = 1;
    public static int TEXTURE_CREATION = 0;
    public static int SOUND_CREATION = 2;
    private static String filePath;
    private static boolean loops = false;
    private static ImageSaveComp ims;
    private static boolean playerController, enemyController;
    private static GameObject gameObject;
    private static SpriteState spriteState;
    private static SpriteLoop spriteLoop;
    private static boolean RigidBody, boxcollider, circleCollider;
    private static Vector2i size = new Vector2i();
    private static int count = 0;
    private static int sep = 0;
    private static int screen = 1;
    private static Sprite sprite;
    private static boolean errorPannel = false;
    private static Triggers trigger;
    private static int EnumIndex = -1;
    private static Triggers defaultstate;
    private static File file;
    private static SpriteSheet sprites;
    private static ThumbNailTextureStore thumbNailTextureStore;
    private static float Frametime = 0.23f;
    private static int type = -1;

    public static void init() {
        int j = 0;
        for (Triggers trig : Triggers.values()) {
            triggersVals[j] = trig.name();
            j++;
        }

    }

    public static void imgui() {
        if (type == 0 && screen == 1) {
            imguiTexture();
        } else if (type == 0 && screen == 2) {
            imguiComponentAdder();
        } else if (type == 1 && screen == 1) {
            imguiTexture();
        } else if (type == 1 && screen == 2) {
            imguiSprite();
        } else if (type == 1 && screen == 3) {
            imguiFinishSpritePannel();
        } else if (type == 2) {
            imguiSound();
        } else if (type == -1) {
            System.out.println("Import Creation Type unspecified");
        }
    }

    private static void imguiSound() {
        ImGui.begin("Sound Creation");
        if (ImGui.checkbox("Sound Loops :", loops)) {
            loops = !loops;
        }
        if (ImGui.button("Create")) {
            try {
                AssetPool.addSound(filePath, loops);
                SoundSaver soundSaver = new SoundSaver();
                soundSaver.init(filePath, loops);
                Window.getScene().getSceneConstructor().getSoundMetaSaver().addComponent(soundSaver);
                reset();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ImGui.end();
    }

    private static void imguiTexture() {
        ImGui.begin("Texture Creation");
        imguiImage(sprite);
        MImGui.drawVec2iControl("Texture Width: ", size, 0, 200, 1, 1,8192);
        count = MImGui.drawIntControl("Texture Count: ", count, 1, 8192);
        sep = MImGui.drawIntControl("Texture Seperation: ", sep);
        if (ImGui.button("Create")) {
            try {
                AssetPool.addSpriteSheet(filePath,
                        new SpriteSheet(AssetPool.getTexture(filePath),
                                size.x, size.y, sep, count));
                ims.init(size, count, sep, file.getName());
                if (type == 1) {
                    sprites = AssetPool.getSpriteSheet(filePath);
                    screen = 2;
                } else if (type == 0) {
                    Window.getScene().getSceneConstructor().addSprites(filePath);
                    screen = 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ImGui.end();

        if (size.x<1)size.x=1;
        if (size.y<1)size.y=1;
        if (count<1)count=1;
    }

    private static void imguiSprite() {
        ImGui.begin("Sprite Creation");
        imguiImagePanel(sprites, 1);
        Frametime = MImGui.drawFLoatControl("Frame Time: ", Frametime, 0, 1000, 0.001f, 0.23f);
        if (ImGui.checkbox("Sprite Loops :", loops)) {
            loops = !loops;
        }
        String[] Enums = getEnums();
        String Type = trigger.name();
        ImInt index = new ImInt(IndexOf(Type, Enums));
        if (ImGui.combo("Trigger :", index, Enums, Enums.length)) {
            if (!enums[IndexOf(Triggers.class.getEnumConstants()[index.get()].name(), triggersVals)]) {
                trigger = Triggers.class.getEnumConstants()[index.get()];
                EnumIndex = index.get();
            }
        }

        if (errorPannel) {
            imguiErrorWindow();
        }

        ImGui.text("Current Sprite Animation");
        imguiImagePanel(spriteLoop.getSprites());

        boolean AlrExists = false;
        if (ImGui.button("Create")) {
            for (SpriteLoop spriteloop : spriteState.getLoops()) {
                if (Objects.equals(spriteloop.getTag(), trigger.name())) {
                    AlrExists = true;
                    break;
                }
            }
            if (!trigger.name().equals("none") && !AlrExists) {
                errorPannel = false;
                enums[EnumIndex] = !enums[EnumIndex];
                spriteLoop.tag = trigger.name();
                spriteLoop.setLoops(loops);
                spriteState.addLoop(spriteLoop);
                spriteLoop = new SpriteLoop();
                int j = 0;
                for (boolean boo : enums) {
                    if (!boo) {
                        trigger = Triggers.class.getEnumConstants()[j];
                        break;
                    }
                    j++;
                }
            }
        }
        Enums = getEnums();
        Type = defaultstate.name();
        index = new ImInt(IndexOf(Type, Enums));
        if (ImGui.combo("Default State :", index, Enums, Enums.length)) {
            defaultstate = Triggers.class.getEnumConstants()[index.get()];
            spriteState.setDefaultTag(Triggers.class.getEnumConstants()[index.get()].name());
        }
        if (ImGui.button("Next")) {
            screen++;
        }

        ImGui.end();
    }

    private static void imguiComponentAdder() {
        ImGui.begin("Component Adder");
        if (ImGui.checkbox("RigidBody :", RigidBody)) {
            RigidBody = !RigidBody;
        }
        if (ImGui.checkbox("BoxCollider :", boxcollider) && RigidBody) {
            boxcollider = !boxcollider;
            if (circleCollider) {
                circleCollider = false;
            }
        }
        if (ImGui.checkbox("CircleCollider :", circleCollider) && RigidBody) {
            circleCollider = !circleCollider;
            if (boxcollider) {
                boxcollider = false;
            }
        }
        if (ImGui.button("Finish")) {
            ims.ComponentAdder(RigidBody, boxcollider, circleCollider);
            Window.getScene().getSceneConstructor().getImageMetaSaver().addComponent(ims);
            ims = new ImageSaveComp();
            reset();
        }

        ImGui.end();

    }

    private static void imguiErrorWindow() {
        ImGui.begin("Error");
        ImGui.text("Trigger Already used");
        if (ImGui.button("Ok")) {
            errorPannel = false;
        }
        ImGui.end();
    }

    private static void imguiFinishSpritePannel() {
        imguiImagePanel(sprites, 2);
        ImGui.text("Current Image :");
        if (ImGui.checkbox("Player Controller :", playerController)) {
            playerController = !playerController;
            if (enemyController) {
                enemyController = false;
            }
        }
        if (ImGui.checkbox("CircleCollider :", enemyController)) {
            enemyController = !enemyController;
            if (playerController) {
                playerController = false;
            }
        }
        if (ImGui.button("Finish")) {

            gameObject = Prefabs.genObjectClicked(thumbNailTextureStore.getSprite(), 0.25f, 0.25f, Window.getImGuilayer().getEditorControl().getVindex());
            if (playerController) {
                gameObject.addComponent(new RBody());
                gameObject.addComponent(new PillBoxCollider());
                gameObject.addComponent(new PlayerController());
                gameObject.getComponent(RBody.class).setDisplayImGui(true);
                gameObject.getComponent(PillBoxCollider.class).setDisplayImGui(true);
                gameObject.getComponent(PlayerController.class).setDisplayImGui(true);
            } else if (enemyController) {
                gameObject.addComponent(new RBody());
                gameObject.addComponent(new PillBoxCollider());
                //gameObject.addComponent(new EnemyController());
            }

            gameObject.addComponent(spriteState);
            gameObject.addComponent(thumbNailTextureStore);
            gameObject.getComponent(SpriteState.class).setDisplayImGui(true);

            Window.getScene().addGOTI(gameObject);
            reset();
        }
    }

    private static void imguiImagePanel(SpriteSheet spriteSheet, int part) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;

        for (int i = 0; i < spriteSheet.size(); i++) {
            Sprite sprite = spriteSheet.getsprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexID();
            Vector2f[] texCoords = sprite.getTex_ver();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                if (part == 1) {
                    spriteLoop.addSprite(sprite, Frametime);
                } else if (part == 2) {
                    thumbNailTextureStore.setSprite(sprite);
                }
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
    }

    private static void imguiImage(Sprite sprite) {
        Vector2f[] texCoords = sprite.getTex_ver();
        ImGui.image(sprite.getTexID(), sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y);
    }

    private static void imguiImagePanel(List<Frame> spriteSheet) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;
        int i = 0;

        for (Frame frame : spriteSheet) {
            Sprite sprite = frame.sprite;
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexID();
            Vector2f[] texCoords = sprite.getTex_ver();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                System.out.println(spriteSheet.size());
                spriteSheet.remove(frame);
                System.out.println(spriteSheet.size());
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
            i++;
        }
    }

    public static void setActive(String FilePath, int i) {
        Active = true;
        type = i;
        ims = new ImageSaveComp();
        filePath = FilePath;
        file = new File(FilePath);
        String Path = file.getAbsolutePath();
        if (type == 1 || type == 0) {
            sprite = new Sprite();
            Texture texture = new Texture();
            texture.init(Path);
            sprite.set_tex(texture);
            spriteState = new SpriteState();
            Arrays.fill(enums, false);
            trigger = Triggers.none;
            defaultstate = Triggers.none;
            EnumIndex = 0;
            thumbNailTextureStore = new ThumbNailTextureStore();
            playerController = false;
            enemyController = false;
        }

        if (type == 1) {
            spriteLoop = new SpriteLoop();
        }
    }

    private static void reset() {
        Active = false;
        type = -1;
        size = new Vector2i();
        count = 0;
        sep = 0;
        Active = false;
        filePath = "";
        loops = false;
        screen = 1;
        Frametime = 0.23f;
        gameObject = null;
        spriteLoop = null;
        spriteState = null;
        Arrays.fill(enums, false);
        EnumIndex = -1;

    }

    private static <T extends Enum<T>> String[] getEnums() {
        String[] Enums = new String[((Class<T>) Triggers.class).getEnumConstants().length];
        int i = 0;
        for (T enumval : ((Class<T>) Triggers.class).getEnumConstants()) {
            Enums[i] = enumval.name();
            i++;
        }
        return Enums;
    }

    private static int IndexOf(String string, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (string.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public enum Triggers {
        none,
        Jump,
        Idle,
        PowerUp,
        PowerDown,
        Die,
        Run,
        ChangeDirection,
        Fire,
        Crouch,
        StopRun,
        BeginRun
    }
}
