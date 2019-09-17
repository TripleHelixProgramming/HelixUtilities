package com.team2363.commands;

import com.team319.trajectory.Path.SegmentValue;

interface TrajectoryHolder {
    double getValue(int index, SegmentValue value);
    int getSegmentCount();
}
