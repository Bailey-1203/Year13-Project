package iso;

import GameEditor.ImportWindow;
import Observers.EventSystem;
import Observers.Events.Event;
import Observers.Observer;
import PhysicsEngine.Physics;
import Scenes.InitalScene;
import Scenes.LevelEditor;
import Scenes.Scene;
import Scenes.SceneConstructor;
import late.Buffer;
import late.Drawer;
import late.Renderer;
import late.Shader;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;
import utility.AssetPool;
import utility.Constant;
import utility.FileExplorer;

import javax.swing.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {

    private static Window window = null;
    private static Scene CurrentScene;
    private final String windowName;
    private final Vector2f WindowSize = Constant.getScreenSize();
    private int width, height;
    private long glfwWindow;
    private imgui_layer imguilayer;
    private long audioContext;
    private long audioDevice;
    private Buffer framebuffer;
    private boolean InEditor = true;
    private String RootFolder;

    private Window() {
        this.width = 2560;
        this.height = 1440;
        this.windowName = "Found";
        EventSystem.addObserver(this);
    }

    public static void ChangeScene(SceneConstructor sceneConstructor, String filename) {
        if (CurrentScene != null) {
            CurrentScene.Distroy();
        }

        getImGuilayer().getPropertiesWindow().setActive(null);
        if (sceneConstructor.inEditor()) {
            CurrentScene = new Scene(sceneConstructor);
            CurrentScene.load(filename);
            CurrentScene.init();
            CurrentScene.start();
        } else {
            CurrentScene = new Scene(sceneConstructor);
            CurrentScene.init();
            CurrentScene.start();
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene getScene() {
        get();
        return CurrentScene;
    }

    public static int getWidth() {
        return get().width;
    }

    public static void setWidth(int nw) {
        get().width = nw;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setHeight(int nh) {
        get().height = nh;
    }

    public static Buffer getFrameBuffer() {
        return get().framebuffer;
    }

    public static float getAspectRatio() {
        return Constant.getScreenAspectRatio();
    }

    public static imgui_layer getImGuilayer() {
        return get().imguilayer;
    }

    public static String getRootFolder() {
        return get().RootFolder;
    }

    public static void setRootFolder(String FilePath) {
        get().RootFolder = FilePath;
    }

    public static Physics getPhysics() {
        return CurrentScene.getPhysics();
    }

    public void run() {

        init();
        loop();

        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("cannot init glfw.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.windowName, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Error creating window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseDetection::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseDetection::MouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseDetection::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyDetection::KeyCallBack);
        glfwSetWindowSizeCallback(glfwWindow, (w, nw, nh) -> {
            setWidth(nw);
            setHeight(nh);
        });

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attrib = {0};
        audioContext = alcCreateContext(audioDevice, attrib);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        assert alCapabilities.OpenAL10 : "Audio Library Unsupported";

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imguilayer = new imgui_layer(glfwWindow);
        this.imguilayer.initImGui();

        this.framebuffer = new Buffer((int) WindowSize.x, (int) WindowSize.y);
        glViewport(0, 0, (int) WindowSize.x, (int) WindowSize.y);

        ImportWindow.init();

        Window.ChangeScene(new InitalScene(), get().RootFolder);
    }

    public void loop() {
        float Start_Time = (float) glfwGetTime();
        float End_Time;
        float delta_time = -1.0f;

        Shader DShader = AssetPool.getShader("assets/shaders/default_new.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            Drawer.start();

            this.framebuffer.Bind();

            glClearColor(1,1,1,1);
            glClear(GL_COLOR_BUFFER_BIT);

            if (delta_time >= 0) {
                if (Window.getImGuilayer().getEditorControl().getviewgrid()) {
                    Drawer.render();
                }
                Renderer.BindShader(DShader);

                if (InEditor) {
                    CurrentScene.EditorUpdate(delta_time);
                } else {
                    CurrentScene.update(delta_time);
                }
                CurrentScene.render();
            }

            if (MouseDetection.mousepressed(GLFW_MOUSE_BUTTON_LEFT)) {
                CurrentScene.getSceneConstructor().getClicked();
            }

            this.framebuffer.UnBind();

            this.imguilayer.update(delta_time, CurrentScene);

            glfwSwapBuffers(glfwWindow);

            MouseDetection.endFrame();

            KeyDetection.EndFrame();

            End_Time = (float) glfwGetTime();
            delta_time = End_Time - Start_Time;
            Start_Time = End_Time;
        }
    }

    @Override
    public void onNotify(GameObject gameObject, Event event) {
        switch (event.event) {
            case PauseSim:
                this.InEditor = true;
                CurrentScene.save("temp.txt");
                Window.ChangeScene(new LevelEditor(), RootFolder);
                break;
            case BeginSim:
                this.InEditor = false;
                CurrentScene.save("");
                Window.ChangeScene(new LevelEditor(), RootFolder);
                break;
            case StopSim:
                this.InEditor = true;
                Window.ChangeScene(new LevelEditor(), RootFolder);
                break;
            case SaveLevel:
                CurrentScene.save(RootFolder);
                break;
            case LoadLevel:
                String DirPath;
                DirPath = FileExplorer.fileExplorer(JFileChooser.DIRECTORIES_ONLY);
                if (DirPath != null) {
                    RootFolder = DirPath;
                    Window.ChangeScene(new LevelEditor(RootFolder), get().RootFolder);
                } else {
                    System.out.println("Not loaded");
                }
                break;
            case SaveAsLevel:
                String DirPath2;
                DirPath2 = FileExplorer.fileExplorer(JFileChooser.DIRECTORIES_ONLY);
                if (DirPath2 != null) {
                    RootFolder = DirPath2;
                    System.out.println(RootFolder);
                    System.out.println("cunt");
                    CurrentScene.save(RootFolder);
                } else {
                    System.out.println("Not Saved");
                }
                break;
        }

    }

}
