package api.consoleApp;

import java.io.*;

public class AppIO {

    public static <T>void SerializeToFile(String path, T obj) throws IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(path))) {
            out.writeObject(obj);
            out.close();
        }
    }

    public static <T>T DeserializeFromFile(String path) throws ClassNotFoundException, IOException {
        try (ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(path))) {
            T rtnObj = (T) in.readObject();
            return rtnObj;
        }
    }
}
