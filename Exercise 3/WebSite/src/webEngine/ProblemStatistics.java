package webEngine;

import logic.Engine;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.rules.base.Rules;
import webEngine.users.User;

import java.util.HashSet;
import java.util.Set;

// This object will hold information and be converted into json object.
public class ProblemStatistics {

    // General statistics
    private final int problemID;
    private final String uploader;
    private final Set<User> users = new HashSet<>();

    // Evolution algorithm problem
    private int days;
    private int hours;
    private int teachers;
    private int classes;
    private int courses;

    private int softRules;
    private int hardRules;

    // Evolution algorithm engine
    private float bestFitness;

    public void calculateBestFitness() {
        users.forEach(user -> {
            float fitness = user.getEngine(problemID).getBestResult().getFitness();
            if (bestFitness < fitness) {
                bestFitness = fitness;
            }
        });
    }

    // Constructor
    public ProblemStatistics(String uploader, int problemID, TimeTableProblem problem) {
        this.uploader = uploader;
        this.problemID = problemID;
        this.bestFitness = 0;

        extractProblemStatistics(problem);
    }

    public synchronized Set<User> getUsers() {
        return new HashSet<>(users);
    }

    public synchronized void addUser(User user) {
        users.add(user);
        user.addSolvingProblemID(this.problemID);
    }

    public synchronized void removeUser(User user) {
        users.remove(user);
        user.removeSolvingProblemID(this.problemID);
    }

    public void hasUser(User user) {
        users.remove(user);
    }

    public int numberOfUsers() {
        return users.size();
    }

    // Private methods
    private void extractProblemStatistics(TimeTableProblem problem) {
        days = problem.getDays();
        hours = problem.getHours();
        teachers = problem.getTeachers().size();
        classes = problem.getClasses().size();
        courses = problem.getCourses().size();

        softRules = (int) problem.getRules().getListOfRules().stream()
                .filter(rule -> rule.getType().equals(Rules.RULE_TYPE.SOFT))
                .count();
        hardRules = problem.getRules().getListOfRules().size() - softRules;

    }
}