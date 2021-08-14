package components.problemInfo;

import components.application.ProblemModule;
import components.problemInfo.accordionItem.AccordionItemController;
import components.problemInfo.ruleAccordionItem.RuleAccordionItemController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.*;
import logic.timeTable.Class;
import logic.timeTable.rules.base.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ProblemInfoController {

    private final ProblemModule problemModule;

    public ProblemInfoController() {
        problemModule = new ProblemModule();
    }

    public void setProblem(TimeTableProblem problem) {
        problemModule.setTheProblem(problem);

        fillAccordion(teachersAccordion,
                problemModule.teachersProperty(),
                "Teaches:",
                this::getTeacherTeachesList);

        fillAccordion(classesAccordion,
                problemModule.classesProperty(),
                "Studies:",
                this::getClassStudiesList);

        // TODO: Create custom course titledPane (accordion item)
        fillAccordion(coursesAccordion,
                problemModule.coursesProperty(),
                "",
                null);

        // TODO: Be able to know if the rule have properties to modifies
        fillRuleAccordion();
    }

    private void fillRuleAccordion() {
        rulesAccordion.getPanes().clear();

        problemModule.rulesProperty().forEach((rule) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/components/problemInfo/ruleAccordionItem/RuleAccordionItem.fxml"));

            try {
                TitledPane node = loader.load();

                RuleAccordionItemController controller = loader.getController();
                controller.setName(rule.getId());
                controller.setType(rule.getType().name());

                rulesAccordion.getPanes().add(node);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private Label daysLbl;

    @FXML
    private Label hoursLbl;

    @FXML
    private Label hardRuleWeightLbl;

    @FXML
    private Label classesCountLbl;

    @FXML
    private Label teachersCountLbl;

    @FXML
    private Label coursesCountLbl;

    @FXML
    private Label rulesCountLbl;

    @FXML
    private Accordion teachersAccordion;

    @FXML
    private Accordion classesAccordion;

    @FXML
    private Accordion coursesAccordion;

    @FXML
    private Accordion rulesAccordion;


    @FXML
    private void initialize() {
        daysLbl.textProperty().bind(Bindings.format("%d", problemModule.daysProperty()));
        hoursLbl.textProperty().bind(Bindings.format("%d", problemModule.hoursProperty()));
        teachersCountLbl.textProperty().bind(Bindings.format("%d", problemModule.teachersProperty().sizeProperty()));
        classesCountLbl.textProperty().bind(Bindings.format("%d", problemModule.classesProperty().sizeProperty()));
        coursesCountLbl.textProperty().bind(Bindings.format("%d", problemModule.coursesProperty().sizeProperty()));
        rulesCountLbl.textProperty().bind(Bindings.format("%d", problemModule.rulesProperty().sizeProperty()));
        hardRuleWeightLbl.textProperty().bind(Bindings.format("%d", problemModule.hardRuleWeightProperty()));
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
