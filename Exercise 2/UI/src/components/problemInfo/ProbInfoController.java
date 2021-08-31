package components.problemInfo;

import components.Resources;
import components.problemInfo.accordionItem.AccordionItemController;
import components.problemInfo.configItem.ConfigController;
import components.problemInfo.ruleAccordionItem.RuleAccordionItemController;
import engine.base.configurable.Configurable;
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
    @FXML private Label labelSysInfoElitism;
    @FXML private Label labelSysInfoDays;
    @FXML private Label labelSysInfoHours;
    @FXML private Label LabelHardRuleWright;

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
        labelSysInfoDays.textProperty().bind(problemModule.daysProperty().asString());
        labelSysInfoHours.textProperty().bind(problemModule.hoursProperty().asString());
        labelSysInfoPop.textProperty().bind(problemModule.populationProperty().asString());
        LabelHardRuleWright.textProperty().bind(problemModule.hardRuleWeightProperty().asString());
        labelSysInfoElitism.setText("0");

        teachersCountLbl.textProperty().bind(problemModule.teachersProperty().sizeProperty().asString());
        classesCountLbl.textProperty().bind(problemModule.classesProperty().sizeProperty().asString());
        coursesCountLbl.textProperty().bind(problemModule.coursesProperty().sizeProperty().asString());
    }

    // Actually updates crossover / selection / mutations
    public void updateEvoSettings(evoEngineSettingsWrapper evoEngineSettings) {
        if (evoEngineSettings.getProblem() == null) {
            return;
        }

        // Clear the accordions
        this.accordionCrossover.getPanes().clear();
        this.accordionSelection.getPanes().clear();
        this.accordionMutations.getPanes().clear();

        // Fill the accordions
        addToAccordion(evoEngineSettings.getCrossover(), this.accordionCrossover);
        addToAccordion(evoEngineSettings.getSelection(), this.accordionSelection);
        evoEngineSettings.getMutations().forEach((mutation -> addToAccordion(mutation, this.accordionMutations)));
        labelSysInfoElitism.setText(Integer.toString(evoEngineSettings.getElitism()));
    }

    public void setProblem(evoEngineSettingsWrapper evoEngineSettings) {
        if (evoEngineSettings.getProblem() == null) {
            return;
        }

        problemModule.setTheProblem(evoEngineSettings.getProblem(), evoEngineSettings);

        fillSystemInfoTab();
        fillRulesTab();

        updateEvoSettings(evoEngineSettings);
    }

    private <T extends HasName>void addToAccordion(T object, Accordion accordion){
        if(object instanceof Configurable){
            fillConfigurable((Configurable) object, accordion, object.getName());
        }else{
            configGenerator(object.getName(), accordion, null);
        }
    }

    private void fillConfigurable(Configurable config, Accordion accordion, String name){
        List<String> listOfConfigs = new ArrayList<>();

        config.getConfiguration().getParameters().forEach((key, value)-> {
            listOfConfigs.add(String.format("%s : %s", key, value));
        }) ;

        configGenerator(name, accordion, listOfConfigs);
    }

    private void configGenerator(String name, Accordion accordion, List<String> configs){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Resources.CONFIG_ITEM_FXML_RESOURCE);

        try{
            TitledPane node = loader.load();
            ConfigController controller = loader.getController();
            controller.setConfigName(name);
            if(configs != null){
                controller.setListView(FXCollections.observableArrayList(configs));
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

        fillAccordion(accordionCourses,
                problemModule.coursesProperty(),
                "",
                null);

    }

    private void fillRulesTab() {
        accordionRules.getPanes().clear();

        problemModule.rulesProperty().forEach((rule) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Resources.RULE_ACCORDION_ITEM_FXML_RESOURCE);

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
            loader.setLocation(Resources.ACCORDION_ITEM_FXML_RESOURCE);

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
