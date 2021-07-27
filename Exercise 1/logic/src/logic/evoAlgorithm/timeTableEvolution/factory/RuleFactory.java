package logic.evoAlgorithm.timeTableEvolution.factory;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.*;
import logic.timeTable.rules.base.Rule;

public class RuleFactory {

    public Rule<TimeTable> create(String ruleName) {
        if (ruleName == null) {
            return null;
        }

        if (DayOffClass.class.getSimpleName().equals(ruleName)) {
            return new DayOffClass();

        } else if (DayOffTeacher.class.getSimpleName().equals(ruleName)) {
            return new DayOffTeacher();

        } else if (Knowledgeable.class.getSimpleName().equals(ruleName)) {
            return new Knowledgeable();

        } else if (Satisfactory.class.getSimpleName().equals(ruleName)){
            return new Satisfactory();

        } else if (Sequentiality.class.getSimpleName().equals(ruleName)) {
            new Sequentiality();

        } else if (Singularity.class.getSimpleName().equals(ruleName)) {
            return new Singularity();

        } else if (TeacherIsHuman.class.getSimpleName().equals(ruleName)) {
            return new TeacherIsHuman();

        } else if (WorkingHoursPreference.class.getSimpleName().equals(ruleName)) {
            return new WorkingHoursPreference();
        }

        return null;
    }
}
