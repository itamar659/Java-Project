package logic.Algorithm.factory;

import logic.timeTable.rules.*;

public class RuleFactory {

    private RuleFactory() {
    }

    public static FactoryResult createRule(String ruleName, String[][] configuration) {
        FactoryResult returnObject = new FactoryResult(true, String.format("Couldn't find the rule named: %s", ruleName));

        if (DayOffClass.class.getSimpleName().equals(ruleName)) {
            returnObject = new FactoryResult(new DayOffClass());

        } else if (DayOffTeacher.class.getSimpleName().equals(ruleName)) {
            returnObject = new FactoryResult(new DayOffTeacher());

        } else if (Knowledgeable.class.getSimpleName().equals(ruleName)) {
            returnObject = new FactoryResult(new Knowledgeable());

        } else if (Sequentiality.class.getSimpleName().equals(ruleName)) {
            returnObject = Factory.setParameterizableParameters(new Sequentiality(), configuration);

        } else if (Singularity.class.getSimpleName().equals(ruleName)) {
            returnObject = new FactoryResult(new Singularity());

        } else if (TeacherIsHuman.class.getSimpleName().equals(ruleName)) {
            returnObject = new FactoryResult(new TeacherIsHuman());

        } else if (WorkingHoursPreference.class.getSimpleName().equals(ruleName)) {
            returnObject = new FactoryResult(new WorkingHoursPreference());
        }

        return returnObject;
    }
}
