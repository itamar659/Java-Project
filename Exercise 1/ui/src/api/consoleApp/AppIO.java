package api.consoleApp;

import java.io.*;

public class AppIO {

    public static <T>void SerializeToFile(String path, T obj) throws IOException {
        ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(path));
        out.writeObject(obj);
        out.close();
    }

    public static <T>T DeserializeFromFile(String path) throws ClassNotFoundException, IOException {
        ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(path));
        T rtnObj = (T) in.readObject();
        in.close();
        return rtnObj;
    }
}
