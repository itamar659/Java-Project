package components;

import java.net.URL;

public class Resources {

    // Center panel resources (time table)
    private static final String BASE_CENTER = "/components/centerScreen";

    private static final String CENTER_HOLDER_FXML_RESOURCE_IDENTIFIER = BASE_CENTER + "/centerHolder.fxml";
    private static final String SINGLE_LESSON_DATA_FXML_RESOURCE_IDENTIFIER = BASE_CENTER + "/timeTable/lessonInfo/LessonInfo.fxml";
    private static final String MULTI_LESSONS_DATA_FXML_RESOURCE_IDENTIFIER = BASE_CENTER + "/timeTable/lessonsInfo/LessonsInfo.fxml";
    private static final String GRID_TABLE_FXML_RESOURCE_IDENTIFIER = BASE_CENTER + "/timeTable/timeTableComponent/TimeTableComponent.fxml";
    private static final String TIME_TABLE_PANEL_FXML_RESOURCE_IDENTIFIER = BASE_CENTER + "/timeTable/timeTablePanel/TimeTablePanel.fxml";
    private static final String CROSSOVER_CONFIG_FXML_RESOURCE_IDENTIFIER = BASE_CENTER + "/timeTable/configurations/crossover/CrossoverController.fxml";

    public static final URL CENTER_HOLDER_FXML_RESOURCE = MainApplication.class.getResource(CENTER_HOLDER_FXML_RESOURCE_IDENTIFIER);
    public static final URL SINGLE_LESSON_TT_FXML_RESOURCE = MainApplication.class.getResource(SINGLE_LESSON_DATA_FXML_RESOURCE_IDENTIFIER);
    public static final URL MULTI_LESSONS_TT_FXML_RESOURCE = MainApplication.class.getResource(MULTI_LESSONS_DATA_FXML_RESOURCE_IDENTIFIER);
    public static final URL GRID_TABLE_FXML_RESOURCE = MainApplication.class.getResource(GRID_TABLE_FXML_RESOURCE_IDENTIFIER);
    public static final URL TIME_TABLE_PANEL_FXML_RESOURCE = MainApplication.class.getResource(TIME_TABLE_PANEL_FXML_RESOURCE_IDENTIFIER);
    public static final URL CROSSOVER_CONFIG_FXML_RESOURCE = MainApplication.class.getResource(CROSSOVER_CONFIG_FXML_RESOURCE_IDENTIFIER);


    // Left panel resources (general information)
    private static final String BASE_LEFT = "/components/problemInfo";

    private static final String PROBLEM_INFO_FXML_RESOURCE_IDENTIFIER = BASE_LEFT + "/ProbInfo.fxml";
    private static final String ACCORDION_ITEM_FXML_RESOURCE_IDENTIFIER = BASE_LEFT + "/accordionItem/accordionItem.fxml";
    private static final String RULE_ACCORDION_ITEM_FXML_RESOURCE_IDENTIFIER = BASE_LEFT + "/ruleAccordionItem/ruleAccordionItem.fxml";
    private static final String CONFIG_RULE_ITEM_FXML_RESOURCE_IDENTIFIER = BASE_LEFT + "/configItem/configItem.fxml";

    public static final URL PROBLEM_INFO_FXML_RESOURCE = MainApplication.class.getResource(PROBLEM_INFO_FXML_RESOURCE_IDENTIFIER);
    public static final URL ACCORDION_ITEM_FXML_RESOURCE = MainApplication.class.getResource(ACCORDION_ITEM_FXML_RESOURCE_IDENTIFIER);
    public static final URL RULE_ACCORDION_ITEM_FXML_RESOURCE = MainApplication.class.getResource(RULE_ACCORDION_ITEM_FXML_RESOURCE_IDENTIFIER);
    public static final URL CONFIG_ITEM_FXML_RESOURCE = MainApplication.class.getResource(CONFIG_RULE_ITEM_FXML_RESOURCE_IDENTIFIER);


    // Right panel resources (best solution information)
    private static final String BASE_RIGHT = "/components/rightPanel";

    private static final String RIGHT_PANEL_FXML_RESOURCE_IDENTIFIER = BASE_RIGHT + "/RightPanel.fxml";
    private static final String TOP_RIGHT_PANEL_FXML_RESOURCE_IDENTIFIER = BASE_RIGHT + "/topRightPanel/TopRightPanel.fxml";

    public static final URL RIGHT_PANEL_FXML_RESOURCE = MainApplication.class.getResource(RIGHT_PANEL_FXML_RESOURCE_IDENTIFIER);
    public static final URL TOP_RIGHT_PANEL_FXML_RESOURCE = MainApplication.class.getResource(TOP_RIGHT_PANEL_FXML_RESOURCE_IDENTIFIER);


    // Startup application resource
    private static final String APP_FXML_RESOURCE_IDENTIFIER = "/components/application/App.fxml";
    public static final URL APP_FXML_RESOURCE = MainApplication.class.getResource(APP_FXML_RESOURCE_IDENTIFIER);
}
