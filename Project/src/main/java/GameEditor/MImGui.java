package GameEditor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class MImGui {

    private static final float WidthConstant = 250.0f;

    public static void drawVec2Control(String tag, Vector2f data, float reset, float width, float sens) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, width);
        ImGui.text(tag);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float height = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f button = new Vector2f(height + 3.0f, height);
        float Width = (ImGui.calcItemWidth() - button.x * 2.0f) / 2.0f;
        ImGui.pushItemWidth(Width);
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 1.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 1.0f, 1.0f, 1.0f);
        if (ImGui.button("x", button.x, button.y)) {
            data.x = reset;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] datax = {data.x};
        ImGui.dragFloat("##x", datax, sens);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(Width);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 0.0f, 1.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 1.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 1.0f, 1.0f, 1.0f);
        if (ImGui.button("y", button.x, button.y)) {
            data.y = reset;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] datay = {data.y};
        ImGui.dragFloat("##y", datay, sens);
        ImGui.popItemWidth();
        ImGui.sameLine();

        data.x = datax[0];
        data.y = datay[0];

        ImGui.nextColumn();

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();
    }

    public static void drawVec2Control(String tag, Vector2f data, float reset, float width) {
        drawVec2Control(tag, data, reset, width, 0.1f);
    }

    public static void drawVec2Control(String tag, Vector2f data, float reset) {
        drawVec2Control(tag, data, reset, WidthConstant);
    }

    public static void drawVec2Control(String tag, Vector2f data) {
        drawVec2Control(tag, data, 0.0f, WidthConstant);
    }

    public static float drawFLoatControl(String tag, float value) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        float[] val = {value};
        ImGui.dragFloat("##dragf", val, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static float drawFLoatControl(String tag, float value, float min, float max) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        float[] val = {value};
        ImGui.dragFloat("##dragf", val, 0.1f, min, max);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static float drawFLoatControl(String tag, float value, float min, float max, float sens) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        float[] val = {value};
        ImGui.dragFloat("##dragf", val, sens, min, max);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static float drawFLoatControl(String tag, float value, float min, float max, float sens, float reset) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float height = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f button = new Vector2f(height + 3.0f, height);
        float Width = (ImGui.calcItemWidth() - button.x * 2.0f) / 2.0f;
        ImGui.pushItemWidth(Width);
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 1.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 1.0f, 1.0f, 1.0f);
        if (ImGui.button("r", button.x, button.y)) {
            value = reset;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();

        float[] val = {value};
        ImGui.dragFloat("##dragf", val, sens, min, max);

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();

        return val[0];
    }

    public static int drawIntControl(String tag, int value) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        int[] val = {value};
        ImGui.dragInt("##dragf", val, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static int drawIntControl(String tag, int value, int min, int max) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        int[] val = {value};
        ImGui.dragInt("##dragf", val, 0.1f, min, max);

        ImGui.columns(1);
        ImGui.popID();

        return val[0];
    }

    public static boolean drawColourPickerV4f(String tag, Vector4f Colour) {
        boolean out = false;
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        float[] imColour = {Colour.x, Colour.y, Colour.z, Colour.w};
        if (ImGui.colorPicker4("##ColPic", imColour)) {
            Colour.set(imColour[0], imColour[1], imColour[2], imColour[3]);
            out = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return out;
    }

    public static boolean drawCheckBox(String tag, Boolean bool) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        if (ImGui.checkbox("##check", bool)) {
            bool = !bool;
        }

        ImGui.columns(1);
        ImGui.popID();

        return bool;
    }

    public static boolean drawDropDown(String tag, Boolean bool) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();


        ImGui.columns(1);
        ImGui.popID();

        return bool;
    }

    public static String drawTextInput(String tag, String name) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();

        ImString string = new ImString(name, 256);
        if (ImGui.inputText("##" + tag, string)) {
            ImGui.columns(1);
            ImGui.popID();
            return string.get();
        }


        ImGui.columns(1);
        ImGui.popID();

        return name;
    }

    public static void drawText2Col(String tag, String text) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, WidthConstant);
        ImGui.text(tag);
        ImGui.nextColumn();
        ImGui.text(text);
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2iControl(String tag, Vector2i data, int reset, float width, int sens, int min, int max) {
        ImGui.pushID(tag);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, width);
        ImGui.text(tag);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float height = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f button = new Vector2f(height + 3.0f, height);
        float Width = (ImGui.calcItemWidth() - button.x * 2.0f) / 2.0f;
        ImGui.pushItemWidth(Width);
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 1.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 1.0f, 1.0f, 1.0f);
        if (ImGui.button("x", button.x, button.y)) {
            data.x = reset;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        int[] datax = {data.x};
        ImGui.dragInt("##x", datax, sens,min,max);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(Width);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 0.0f, 1.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 1.0f, 0.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 1.0f, 1.0f, 1.0f);
        if (ImGui.button("y", button.x, button.y)) {
            data.y = reset;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        int[] datay = {data.y};
        ImGui.dragInt("##y", datay, sens,min,max);
        ImGui.popItemWidth();
        ImGui.sameLine();

        data.x = datax[0];
        data.y = datay[0];

        ImGui.nextColumn();

        ImGui.columns(1);
        ImGui.popStyleVar();
        ImGui.popID();
    }
}
