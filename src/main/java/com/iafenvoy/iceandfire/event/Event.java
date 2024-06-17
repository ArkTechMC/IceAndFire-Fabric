package com.iafenvoy.iceandfire.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class Event {
    private boolean canceled = false;
    private Result result = Result.PASS;

    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public enum Result {
        ALLOW, DENY, PASS
    }

    @Target(ElementType.TYPE)
    public @interface HasResult {

    }
}
