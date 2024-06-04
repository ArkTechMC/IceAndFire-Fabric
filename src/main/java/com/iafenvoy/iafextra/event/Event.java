package com.iafenvoy.iafextra.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class Event {
    private boolean canceled = false;
    private Result result=Result.PASS;

    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Target(ElementType.TYPE)
    public @interface HasResult {

    }

    public enum Result {
        ALLOW, DENY, PASS
    }
}
