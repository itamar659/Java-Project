package logic.timeTable.rules;

import logic.timeTable.rules.base.Rule;
import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.validation.ValidationResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class WorkingHoursPreference extends Rule {

    public WorkingHoursPreference() {
        this.setRuleName("WorkingHoursPreference");
    }

    @Override
    public float calcFitness(Solution solution) {
        throw new NotImplementedException();
    }

    @Override
    public ValidationResult checkValidation() {
        return new ValidationResult(true);
    }
}
