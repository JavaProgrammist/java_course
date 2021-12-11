package ru.otus.model;

import java.time.LocalDateTime;

public class InfoAboutExceptionInAction {

    private LocalDateTime actionStartTime;
    private boolean isExceptionThrown;

    public InfoAboutExceptionInAction(LocalDateTime actionStartTime, boolean isExceptionThrown) {
        this.actionStartTime = actionStartTime;
        this.isExceptionThrown = isExceptionThrown;
    }

    public LocalDateTime getActionStartTime() {
        return actionStartTime;
    }

    public boolean isExceptionThrown() {
        return isExceptionThrown;
    }

    public boolean isActionStartSecondEven() {
        return actionStartTime.getSecond() % 2 == 0;
    }
}
