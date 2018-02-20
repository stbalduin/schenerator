package de.schenerator.presenter;

import de.schenerator.controller.Commander;

/**
 * A presenter in the context of the schedule generator should derive from this
 * Presenter to get access to the commander, which can be used to manipulate the
 * schedules.
 * 
 * @author sBalduin
 *
 */
public abstract class Presenter {
    protected Commander commander;

    public void setCommander(Commander commander) {
        this.commander = commander;
    }
}
