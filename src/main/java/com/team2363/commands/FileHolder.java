package com.team2363.commands;

import java.io.IOException;

import com.team319.trajectory.Path.SegmentValue;

import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;

public class FileHolder implements TrajectoryHolder {

    private Trajectory leftTrajectory;
    private Trajectory centerTrajectory;
    private Trajectory rightTrajectory;

    FileHolder(String path) {
        importPath(path);
    }

    @Override
    public double getValue(int index, SegmentValue value) {
        switch (value) {
            case X:
                return centerTrajectory.get(0).x;
            case Y:
                return centerTrajectory.get(0).y;
            case LEFT_POSITION:
                return leftTrajectory.get(0).position;
            case LEFT_VELOCITY:
                return leftTrajectory.get(0).velocity;
            case LEFT_ACCELERATION:
                return leftTrajectory.get(0).acceleration;
            case LEFT_JERK:
                return leftTrajectory.get(0).jerk;
            case RIGHT_POSITION:
                return rightTrajectory.get(0).position;
            case RIGHT_VELOCITY:
                return rightTrajectory.get(0).velocity;
            case RIGHT_ACCELERATION:
                return rightTrajectory.get(0).acceleration;
            case RIGHT_JERK:
                return rightTrajectory.get(0).jerk;
            case CENTER_POSITION:
                return centerTrajectory.get(0).position;
            case CENTER_VELOCITY:
                return centerTrajectory.get(0).velocity;
            case CENTER_ACCELERATION:
                return centerTrajectory.get(0).acceleration;
            case CENTER_JERK:
                return centerTrajectory.get(0).jerk;
            case HEADING:
                return centerTrajectory.get(0).heading;
            case TIME_STAMP:
                return centerTrajectory.get(0).dt;
        }
		return 0;
    }
    
    
  private void importPath(String pathName) {
    try {
        leftTrajectory = PathfinderFRC.getTrajectory(pathName + ".right");
        centerTrajectory = PathfinderFRC.getTrajectory(pathName);
        rightTrajectory = PathfinderFRC.getTrajectory(pathName + ".left");
    } catch (IOException e) {
		  e.printStackTrace();
	  }
  }

    @Override
    public int getSegmentCount() {
        return centerTrajectory.length();
    }
}
