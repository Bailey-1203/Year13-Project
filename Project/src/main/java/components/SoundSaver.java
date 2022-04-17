package components;

import java.io.File;

public class SoundSaver extends Component{

    String filePath;
    boolean loops;

    public SoundSaver() {

    }

    public void init(String f, boolean l) {
        this.filePath = f;
        this.loops = l;
    }

    @Override
    public String getName() {
        return new File(filePath).getName();
    }

    public Boolean Loops() {
        return this.loops;
    }
}
