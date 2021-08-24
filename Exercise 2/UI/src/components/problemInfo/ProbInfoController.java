package components.problemInfo;

import components.application.ProblemModule;
import components.problemInfo.accordionItem.AccordionItemController;
import components.problemInfo.configItem.ConfigController;
import components.problemInfo.ruleAccordionItem.RuleAccordionItemController;
import logic.evoAlgorithm.configurable.Configurable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import logic.evoEngineSettingsWrapper;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.HasId;
import engine.base.HasName;
import logic.timeTable.Teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ProbInfoController {
    @FXML private Label labelSysInfoPop;
    @FXML private Label labelSysInfoDays;
    @FXML private Label labelSysInfoHours;

    @FXML private Label teachersCountLbl;
    @FXML private Label classesCountLbl;
    @FXML private Label coursesCountLbl;

    @FXML private Accordion accordionTeachers;
    @FXML private Accordion accordionClasses;
    @FXML private Accordion accordionCourses;
    
    @FXML private Accordion accordionMutations;
    @FXML private Accordion accordionCrossover;
    @FXML private Accordion accordionSelection;
    @FXML private Accordion accordionRules;
    


    private final ProblemModule problemModule;

    public ProbInfoController() {
        problemModule = new ProblemModule();
    }

    @FXML
    private void initialize() {
        labelSysInfoDays.textProperty().bind(Bindings.format("%d", problemModule.daysProperty()));
        labelSysInfoHours.textProperty().bind(Bindings.format("%d", problemModule.hoursProperty()));
        labelSysInfoPop.textProperty().bind(Bindings.format("%d", problemModule.populationProperty()));

        teachersCountLbl.textProperty().bind(Bindings.format("%d", problemModule.teachersProperty().sizeProperty()));
        classesCountLbl.textProperty().bind(Bindings.format("%d", problemModule.classesProperty().sizeProperty()));
        coursesCountLbl.textProperty().bind(Bindings.format("%d", problemModule.coursesProperty().sizeProperty()));
    }

    public void setProblem(evoEngineSettingsWrapper evoEngineSettings) {
        problemModule.setTheProblem(evoEngineSettings.getProblem(), evoEngineSettings);

        fillSystemInfoTab();
        // TODO: Be able to know if the rule have properties to modifies
        fillRulesTab();

        ifConfigurableThenFill(evoEngineSettings.getCrossover(), this.accordionCrossover);


        ifConfigurableThenFill(evoEngineSettings.getSelection(), this.accordionSelection);


        evoEngineSettings.getMutations().forEach((mutation -> {
            ifConfigurableThenFill(mutation, this.accordionMutations);
        }));
    }

    private <T extends HasName>void ifConfigurableThenFill(T object, Accordion accordion){
        if(object instanceof Configurable){
            fillConfigurable((Configurable) object, accordion, object.getName());
        }else{
            configGenerator(object.getName(), accordion, null, false);
        }
    }

    private void fillConfigurable(Configurable config, Accordion accordion, String name){
        List<String> string2stringConfig = new ArrayList<>();

        config.getConfiguration().getParameters().forEach((key, value)-> {
            string2stringConfig.add(String.format("%s : %s", key, value));
        }) ;

        configGenerator(name, accordion, string2stringConfig, true);
    }

    private void configGenerator(String name, Accordion accordion, List<String> lst, boolean isConfigurable){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/components/problemInfo/configItem/configItem.fxml"));

        try{
            TitledPane node = loader.load();
            ConfigController controller = loader.getController();
            controller.setConfigName(name);
            if(isConfigurable){
                controller.setListView(FXCollections.observableArrayList(lst));
                controller.setListViewVisible(true);
            }
            else{
                controller.setListViewVisible(false);
            }
            accordion.getPanes().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillSystemInfoTab(){
        fillAccordion(accordionTeachers,
                problemModule.teachersProperty(),
                "Teaches:",
                this::getTeacherTeachesList);

        fillAccordion(accordionClasses,
                problemModule.classesProperty(),
                "Studies:",
                this::getClassStudiesList);

        // TODO: Create custom course titledPane (accordion item)
        fillAccordion(accordionCourses,
                problemModule.coursesProperty(),
                "",
                null);

    }

    private void fillRulesTab() {
        accordionRules.getPanes().clear();

        problemModule.rulesProperty().forEach((rule) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/components/problemInfo/ruleAccordionItem/RuleAccordionItem.fxml"));

            try {
                TitledPane node = loader.load();

                RuleAccordionItemController controller = loader.getController();
                controller.setName(rule.getId());
                controller.setType(rule.getType().name());

                accordionRules.getPanes().add(node);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private <T extends HasId & HasName>void fillAccordion(Accordion accordion,
                                                          ObservableList<T> list,
                                                          String listDesc,
                                                          Function<T, ObservableList<String>> createSubList) {
        accordion.getPanes().clear();

        for (T item : list) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/components/problemInfo/accordionItem/AccordionItem.fxml"));

            try {
                TitledPane node = loader.load();

                AccordionItemController controller = loader.getController();
                controller.setName(item.getName());
                controller.setId(item.getId());
                controller.setListDesc(listDesc);

                if (createSubList != null) {
                    controller.setItemsList(createSubList.apply(item));
                }

                accordion.getPanes().add(node);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ObservableList<String> getTeacherTeachesList(Teacher teacher) {
        List<String> coursesAndIDs = new ArrayList<>();

        teacher.getTeachesCoursesIDs().forEach((courseID) -> {
            Course course = problemModule.coursesProperty().stream()
                    .filter(c -> c.getId().equals(courseID))
                    .findFirst()
                    .get(); // always present. no need to check
            coursesAndIDs.add(String.format("%s - %s", course.getId(), course.getName()));
        });

        return FXCollections.observableArrayList(coursesAndIDs);
    }

    private ObservableList<String> getClassStudiesList(Class aclass) {
        List<String> coursesAndIDs = new ArrayList<>();

        aclass.getCourseID2Hours().forEach((key, val) -> {
            Course course = problemModule.coursesProperty().stream()
                    .filter(c -> c.getId().equals(key))
                    .findFirst()
                    .get(); // always present. no need to check
            coursesAndIDs.add(String.format("%s - %s: %d hours", course.getId(), course.getName(), val));
        });

        return FXCollections.observableArrayList(coursesAndIDs);
    }
}
