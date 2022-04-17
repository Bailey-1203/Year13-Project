package components;

import GameEditor.MImGui;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SpriteState extends Component {
    private final List<SpriteLoop> Loops = new ArrayList<>();
    public HashMap<Trigger, String> SpriteLoops = new HashMap<>();
    private String DefaultTag = "";
    private transient SpriteLoop currentActive = null;

    public List<SpriteLoop> getLoops() {
        return Loops;
    }

    public void addLoop(SpriteLoop loop) {
        Loops.add(loop);
    }

    public void refreshTextures() {
        for (SpriteLoop loop : Loops) {
            loop.refreshTextures();
        }
    }

    public void addTrigger(String from, String to, String trigger) {
        this.SpriteLoops.put(new Trigger(from, trigger), to);
    }

    public void trigger(String trigger) {
        for (Trigger trig : SpriteLoops.keySet()) {
            if (trig.trigger.equals(currentActive.tag) && trig.trigger.equals(trigger)) {
                if (SpriteLoops.get(trig) != null) {
                    int newLoopIndex = stateIndexOf(SpriteLoops.get(trig));
                    if (newLoopIndex > -1) {
                        currentActive = Loops.get(newLoopIndex);
                    }
                }
            }
        }

        //System.out.println("unknown trigger "+trigger);
    }

    private int stateIndexOf(String s) {
        int index = 0;
        for (SpriteLoop loop : Loops) {
            if (loop.tag.equals(s)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public void start() {
        for (SpriteLoop loop : Loops) {
            if (loop.tag.equals(DefaultTag)) {
                currentActive = loop;
                break;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (currentActive != null) {
            currentActive.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null) {
                sprite.setSprite(currentActive.getSprite());
            }
        }
    }

    public void setDefaultTag(String tag) {
        for (SpriteLoop loop : Loops) {
            if (Objects.equals(loop.tag, tag)) {
                DefaultTag = tag;
                if (currentActive == null) {
                    currentActive = loop;
                    return;
                }
            }
        }
    }

    @Override
    public void EditorUpdate(float dt) {
        if (currentActive != null) {
            currentActive.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null) {
                sprite.setSprite(currentActive.getSprite());
            }
        }
    }

    @Override
    public void imgui() {
        int i = 0;
        ImString defaultTag = new ImString(DefaultTag);
        ImGui.inputText("default Tag: ", defaultTag);
        DefaultTag = defaultTag.get();
        for (SpriteLoop loop : Loops) {
            ImString tag = new ImString(loop.tag);
            ImGui.inputText("Tag: ", tag);
            loop.tag = tag.get();

            ImBoolean doLoop = new ImBoolean(loop.getLoops());
            ImGui.checkbox("Loop :", doLoop);
            loop.setLoops(doLoop.get());
            for (Frame frame : loop.Sprites) {
                frame.timer = MImGui.drawFLoatControl("frame " + i + ":", frame.timer, 0.0f, 1000f, 0.001f);
                i++;
            }
        }
    }

    public class Trigger {
        public String tag;
        public String trigger;

        public Trigger() {

        }

        public Trigger(String tag, String trigger) {
            this.tag = tag;
            this.trigger = trigger;
        }

        @Override
        public boolean equals(Object object) {
            if (object.getClass() != Trigger.class) {
                return false;
            }
            Trigger tri = (Trigger) object;
            return tri.trigger.equals(this.trigger) && tri.tag.equals((this.tag));
        }

        public int Hash() {
            return Objects.hash(trigger, tag);
        }
    }
}
