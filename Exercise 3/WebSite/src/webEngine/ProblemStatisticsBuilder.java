package webEngine;

import logic.evoAlgorithm.TimeTableProblem;

public class ProblemStatisticsBuilder {

    private int problemID = 1;
    private TimeTableProblem problem;
    private String uploader;

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public void setProblem(TimeTableProblem problem) {
        this.problem = problem;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public ProblemStatistics create() {
        return new ProblemStatistics(uploader, problemID, problem);
    }
}
