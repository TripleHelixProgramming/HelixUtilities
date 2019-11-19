package com.team2363.commands;

import com.team2363.controller.PIDController;
import com.team319.trajectory.Path;

/**
 * PurePersuitHelixFollower
 */
public abstract class HelixPurePersuitFollower extends HelixFollower {

    /**
     * This will import the path class based on the name of the path provided
     * 
     * @param path the name of the path to run
     */
    public HelixPurePersuitFollower(Path path) {
        super(path);
    }

    /**
     * A decorator to flip the left and right direction of the path
     * 
     * @return the current PathFollower instance
     */
    public HelixPurePersuitFollower mirror() {
        super.mirror();
        return this;
    }

    /**
     * A decorator to run the path with the robot facing backwards
     * 
     * @return the current PathFollower instance
     */
    public HelixPurePersuitFollower reverse() {
        super.reverse();
        return this;
    }

    @Override
    public abstract void resetDistance();

    @Override
    public abstract PIDController getHeadingController();

    @Override
    public abstract PIDController getDistanceController();

    @Override
    public abstract double getCurrentDistance();

    @Override
    public abstract double getCurrentHeading();

    @Override
    public abstract void useOutputs(double left, double right);

}