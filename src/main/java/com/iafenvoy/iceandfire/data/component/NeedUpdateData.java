package com.iafenvoy.iceandfire.data.component;

public class NeedUpdateData {
    private boolean triggerClientUpdate;

    public void triggerUpdate() {
        this.triggerClientUpdate = true;
    }

    public boolean doesClientNeedUpdate() {
        if (!this.triggerClientUpdate) return false;
        this.triggerClientUpdate = false;
        return true;
    }
}
