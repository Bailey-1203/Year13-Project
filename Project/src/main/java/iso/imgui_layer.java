package iso;

import GameEditor.*;
import Scenes.Scene;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class imgui_layer {

    private final long glfwWindow;

    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();

    private final GameWindow gameWindow;

    private final PropertiesWindow propertiesWindow;
    private final EditorControl editorControl;
    private final TopMenu topMenu;
    private final SceneObjectsWindow sceneObjectsWindow;

    public imgui_layer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
        gameWindow = new GameWindow();
        propertiesWindow = new PropertiesWindow();
        editorControl = new EditorControl();
        propertiesWindow.setName("inspector");
        this.topMenu = new TopMenu();
        this.sceneObjectsWindow = new SceneObjectsWindow();
    }

    public void initImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini");
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw");

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard()) {
                KeyDetection.KeyCallBack(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }

            if (!io.getWantCaptureMouse() || gameWindow.WantMouseClicked()) {
                MouseDetection.MouseButtonCallback(w, button, action, mods);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            MouseDetection.mouseScrollCallback(w, xOffset, yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/font2.ttf", 32, fontConfig);

        fontConfig.destroy();

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiImplGlfw.init(glfwWindow,false);
        imGuiGl3.init("#version 330 core");
    }

    public void update(float dt, Scene currentScene) {
        startFrame(dt);

        if (currentScene.getSceneConstructor().inEditor()) {
            setupDockspace();
            currentScene.imgui();
            gameWindow.imgui();
            editorControl.imgui();
            propertiesWindow.update(dt, currentScene.getActive());
            propertiesWindow.imgui();
            sceneObjectsWindow.imgui();
            if (ImportWindow.Active) {
                ImportWindow.imgui();
            }
        }else{
            currentScene.imgui();
        }
        endFrame();
    }

    private void startFrame(final float deltaTime) {
        imGuiImplGlfw.newFrame();
        ImGui.newFrame();
    }

    private void endFrame() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0,0,Window.getWidth(),Window.getHeight());
        glClearColor(0,0,0,1);
        glClear(GL_COLOR_BUFFER_BIT);
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        long wind = glfwGetCurrentContext();
        ImGui.updatePlatformWindows();
        ImGui.renderPlatformWindowsDefault();
        glfwMakeContextCurrent(wind);
    }

    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGuiViewport Main = ImGui.getMainViewport();
        ImGui.setNextWindowPos(Main.getWorkPosX(),Main.getWorkPosY());
        ImGui.setNextWindowSize(Main.getWorkSizeX(),Main.getWorkSizeY());
        ImGui.setNextWindowViewport(Main.getID());
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace"));

        topMenu.imgui();

        ImGui.end();
    }

    public PropertiesWindow getPropertiesWindow() {
        return this.propertiesWindow;
    }

    public EditorControl getEditorControl() {
        return this.editorControl;
    }
}
