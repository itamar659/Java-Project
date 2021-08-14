package components;

import components.application.ApplicationController;
import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.timeTablePanel.TimeTablePanelController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Lesson;
import logic.timeTable.Teacher;

import java.net.URL;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/components/application/Application.fxml");
        loader.setLocation(url);
        Parent root = loader.load(url.openStream());

        ApplicationController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static ObservableList<Lesson> createList() {
        Course c = new Course();
        c.setName("ABC");
        c.setId("1");
        Teacher t = new Teacher();
        t.setName("F");
        t.setId("1");
        Class cc = new Class();
        cc.setId("5");
        cc.setName("asdffdsa");
        Lesson l = new Lesson();
        l.setCourse(c);
        l.setaClass(cc);
        l.setTeacher(t);
        l.setDay(0);
        l.setHour(0);

        Lesson l2 = new Lesson();
        Teacher t2 = new Teacher();
        t2.setName("HHH");
        t2.setId("7");
        l2.setCourse(c);
        l2.setaClass(cc);
        l2.setTeacher(t2);
        l2.setDay(1);
        l2.setHour(3);


        ObservableList<Lesson> lessons = FXCollections.observableArrayList();

        lessons.add(l);
        lessons.add(l);
        lessons.add(l);
        lessons.add(l2);

        return lessons;
    }
}