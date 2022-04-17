package Scenes;

import PhysicsEngine.Physics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.Converter;
import iso.Camera;
import iso.GODeserializer;
import iso.GameObject;
import iso.Transform;
import late.Renderer;
import org.joml.Vector2f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scene {
    private final Physics physics;
    private final Renderer renderer;
    private final List<GameObject> gameObjects;
    private final List<GameObject> ImGameObjects;
    private final SceneConstructor sceneConstructor;
    private boolean run;
    private Camera camera;
    private GameObject active = null;

    public Scene(SceneConstructor sceneConstructor) {
        this.sceneConstructor = sceneConstructor;
        this.physics = new Physics();
        this.gameObjects = new ArrayList<>();
        this.ImGameObjects = new ArrayList<>();
        this.renderer = new Renderer();
        this.run = false;

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }

        run = true;
    }

    public void addGOTS(GameObject go) {
        if (!run) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }
    }

    public void addGOTI(GameObject gameObject) {
        ImGameObjects.add(gameObject);
    }

    public void init() {
        this.camera = new Camera(new Vector2f(-250f, 0f));
        this.sceneConstructor.LoadResources(this);
        this.sceneConstructor.init(this);
    }

    public Camera camera() {
        return this.camera;
    }

    public void update(float delta_time) {
        this.camera.adjust_proj();
        this.physics.update(delta_time);

        for (GameObject go : this.gameObjects) {
            go.update(delta_time);
            if (go.isDead()) {
                gameObjects.remove(go);
                this.renderer.removeObject(go);
                this.physics.removeObject(go);
            }
        }
    }

    public void render() {
        this.renderer.render();
    }

    public void save(String filename) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new Converter())
                .registerTypeAdapter(GameObject.class, new GODeserializer())
                .enableComplexMapKeySerialization()
                .create();
        try {
            FileWriter w = new FileWriter(new File(filename, "LevelDat.txt"));
            ArrayList<GameObject> GameObjectsToSerialise = new ArrayList<>();
            for (GameObject obj : gameObjects) {
                if (obj.getSerialise()) {
                    GameObjectsToSerialise.add(obj);
                }
            }
            w.write(gson.toJson(GameObjectsToSerialise));
            w.close();

            FileWriter w2 = new FileWriter(new File(filename, "ImGameObjects.txt"));
            ArrayList<GameObject> ImGameObjectsToSerialise = new ArrayList<>();
            for (GameObject obj : ImGameObjects) {
                if (obj.getSerialise()) {
                    ImGameObjectsToSerialise.add(obj);
                }
            }
            w2.write(gson.toJson(ImGameObjectsToSerialise));
            w2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameObject CreateObj(String name) {
        GameObject obj = new GameObject(name);
        obj.addComponent(new Transform());
        obj.transform = obj.getComponent(Transform.class);
        return obj;
    }

    public void load(String filename) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new Converter())
                .registerTypeAdapter(GameObject.class, new GODeserializer())
                .enableComplexMapKeySerialization()
                .create();

        String Filepath = null;
        try {
            Filepath = new String(Files.readAllBytes(Paths.get(filename, "LevelDat.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!Objects.equals(Filepath, "")) {
                int MaxGmoID = -1;
                int MaxComID = -1;
                GameObject[] objects = gson.fromJson(Filepath, GameObject[].class);
                for (GameObject object : objects) {
                    addGOTS(object);

                    for (Component c : object.getComponents()) {
                        if (c.getUnivID() > MaxComID) {
                            MaxComID = c.getUnivID();
                        }
                    }

                    if (object.getUnivID() > MaxGmoID) {
                        MaxGmoID = object.getUnivID();
                    }
                }

                MaxGmoID++;
                MaxComID++;
                Component.init(MaxComID);
                GameObject.init(MaxGmoID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Filepath = null;
        try {
            Filepath = new String(Files.readAllBytes(Paths.get(filename, "ImGameObjects.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!Objects.equals(Filepath, "")) {
                int MaxGmoID = -1;
                int MaxComID = -1;
                GameObject[] Imobjects = gson.fromJson(Filepath, GameObject[].class);
                for (GameObject imobject : Imobjects) {
                    addGOTI(imobject);

                    for (Component c : imobject.getComponents()) {
                        if (c.getUnivID() > MaxComID) {
                            MaxComID = c.getUnivID();
                        }
                    }

                    if (imobject.getUnivID() > MaxGmoID) {
                        MaxGmoID = imobject.getUnivID();
                    }
                }

                MaxGmoID++;
                MaxComID++;
                Component.init(MaxComID);
                GameObject.init(MaxGmoID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imgui() {
        this.sceneConstructor.imgui();
    }

    public GameObject getActive() {
        return this.active;
    }

    public void setActive(GameObject active) {
        this.active = active;
    }

    public GameObject getEditor() {
        for (GameObject gameObject : gameObjects) {
            if (Objects.equals(gameObject.getObjectName(), "Editor")) {
                return gameObject;
            }
        }
        return null;
    }

    public List<GameObject> getObjects() {
        return this.gameObjects;
    }

    public void EditorUpdate(float dt) {
        this.camera.adjust_proj();
        //int c = 0;
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.EditorUpdate(dt);
            if (gameObject.isDead()) {
                gameObjects.remove(gameObject);
                this.renderer.removeObject(gameObject);
                this.physics.removeObject(gameObject);
                i--;
            }
            // c += 1;
        }

        //System.out.println(c);
    }

    public SceneConstructor getSceneConstructor() {
        return this.sceneConstructor;
    }

    public void Distroy() {
        for (GameObject gameObject : gameObjects) {
            gameObject.destroy();
        }
    }

    public Physics getPhysics() {
        return this.physics;
    }

    public List<GameObject> getImGameObjects() {
        return ImGameObjects;
    }
}
