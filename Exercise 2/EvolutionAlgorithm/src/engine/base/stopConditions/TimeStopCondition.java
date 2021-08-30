package engine.base.stopConditions;

import java.time.Instant;

public class TimeStopCondition implements StopCondition {

    private long periodOfTime;
    private long totalTime;
    private Instant lastCheck;

    public long getPeriodOfTime() {
        return periodOfTime;
    }

    public void setPeriodOfTime(long periodOfTime) {
        this.periodOfTime = periodOfTime;
    }

    public void pause() {
        lastCheck = null;
    }

    @Override
    public boolean shouldStop() {
        if (lastCheck != null) {
            long diffInSeconds = Instant.now().minusSeconds(lastCheck.getEpochSecond()).getEpochSecond();
            totalTime += diffInSeconds;
        }

        lastCheck = Instant.now();
        return totalTime >= periodOfTime;
    }

    @Override
    public float getProgress() {
        if (periodOfTime > 0) {
            return (float) totalTime / periodOfTime;
        }
        return 0;
    }

    public void reset() {
        totalTime = 0;
    }
}
