package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by leonelbadi on 11/3/16.
 */
public class ObjectPlainWriter {

    /**
     * Example method for using the ObjectOutputStream class
     */
    public void writeObject(String filename, PlainWritable object ) {

        PrintWriter writer = null;

        try {

            //Construct the LineNumberReader object
            writer = new PrintWriter(filename);
            writer.write(object.writeObject());

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the FileWriter
            if (writer != null) {
                writer.close();
            }
        }
    }

    public PlainWritable readObject(String fileName, PlainWritable objectToWrite){

        FileInputStream reader = null;

        try {
            String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            return objectToWrite.readObject(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
