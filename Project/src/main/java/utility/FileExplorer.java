package utility;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileExplorer {
    public static String fileExplorer(int FileType) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(FileType);
        int ButtonClicked = jFileChooser.showOpenDialog(null);
        if (ButtonClicked == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }else{
            return null;
        }
    }

    public static void DuplicateImageFile(String from, String to){
        File filefrom = new File(from);
        File fileto = new File(to + filefrom.getName());
        try {
            Files.copy(filefrom.toPath(), fileto.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void InitalCreateGame(String FilePath) {
        File file = new File(FilePath+"\\assets");
        if(!file.exists()) {
            file.mkdirs();
        }
        file = new File(FilePath+"\\Sounds");
        if(!file.exists()) {
            file.mkdirs();
        }
    }

}
