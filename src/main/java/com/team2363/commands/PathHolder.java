package com.team2363.commands;

import com.team319.trajectory.Path;
import com.team319.trajectory.Path.SegmentValue;

class PathHolder implements TrajectoryHolder {
    
    private Path path;

    PathHolder(Path path) {
        this.path = path;
    }

    @Override
    public double getValue(int index, SegmentValue value) {
		return path.getValue(index, value);
	}

    @Override
    public int getSegmentCount() {
        return path.getSegmentCount();
    }
}
