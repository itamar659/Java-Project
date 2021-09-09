package webEngine;

import logic.evoAlgorithm.TimeTableProblem;

public class ProblemStatisticsBuilder {

    private int currentID = 1;
    private TimeTableProblem problem;
    private String uploader;

    public void setProblem(TimeTableProblem problem) {
        this.problem = problem;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public ProblemStatistics create() {
        return new ProblemStatistics(uploader, currentID++, problem);
    }
}
