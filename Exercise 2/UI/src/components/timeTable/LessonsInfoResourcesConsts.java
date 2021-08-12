package components.timeTable;

import components.MainApplication;

import java.net.URL;

public class LessonsInfoResourcesConsts {
    private static final String BASE_PACKAGE = "/components/timeTable";
    private static final String SINGLE_LESSON_DATA_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/lessonInfo/LessonInfo.fxml";
    private static final String MULTI_LESSONS_DATA_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/lessonsInfo/LessonsInfo.fxml";
    private static final String GRID_TABLE_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/timeTableComponent/TimeTableComponent.fxml";

    public static final URL MULTI_LESSONS_TT_FXML_RESOURCE = MainApplication.class.getResource(MULTI_LESSONS_DATA_FXML_RESOURCE_IDENTIFIER);
    public static final URL SINGLE_LESSON_TT_FXML_RESOURCE = MainApplication.class.getResource(SINGLE_LESSON_DATA_FXML_RESOURCE_IDENTIFIER);
    public static final URL GRID_TABLE_FXML_RESOURCE = MainApplication.class.getResource(GRID_TABLE_FXML_RESOURCE_IDENTIFIER);

    public static final String TOP_FXML_RESOURCE_IDENTIFIER = BASE_PACKAGE + "/timeTablePanel/TimeTablePanel.fxml";
}
