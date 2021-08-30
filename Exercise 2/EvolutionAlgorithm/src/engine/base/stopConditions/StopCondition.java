package engine.base.stopConditions;

public interface StopCondition {

    boolean shouldStop();

    float getProgress();
}
