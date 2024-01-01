package mancala;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Saver {

    final private String directory;

    public Saver() {
        this.directory = "assets"; 
    }

    public void saveObject(final Serializable toSave, final String filename) throws IOException {
        final File dir = new File(directory);
        if (!dir.exists()){
            dir.mkdir();
        }
        try (ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(directory + "/" + filename))) {
            fileOut.writeObject(toSave);
        }
    }    

    public Serializable loadObject(final String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(directory + "/" + filename))) {
            return (Serializable) fileIn.readObject();
        }
    }
}