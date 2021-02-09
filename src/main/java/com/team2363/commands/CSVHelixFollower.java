package com.team2363.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.team2363.controller.PIDController;
import com.team2363.logger.HelixEvents;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class CSVHelixFollower extends Command {
  private Notifier pathNotifier = new Notifier(this::moveToNextSegment);
  private Notifier pidNotifier = new Notifier(this::calculateOutputs);

  private String filename;
  private String line = null;
  private boolean mirror, reverse, isFinished;
  private double mCurrentDistance, mTargetDistance;
  private double mCurrentHeading, mTargetHeading; 
  private double mLeftVelocity, mRightVelocity;
  private BufferedReader pathReader;
  private String[] timestamp;

  public enum INDEX {

    DT(0),
    LEFT_VELOCITY(4),
    RIGHT_VELOCITY(12),
    CENTER_POSITION(7),
    HEADING(15);
  
    private int index;

    private INDEX(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
  }

  public CSVHelixFollower(String filename) {
    try {
      this.filename = filename + ".csv";
      pathReader = new BufferedReader(new FileReader(this.filename));
      pathReader.readLine();
    } catch (IOException e) {
      isFinished = true;
    }
  }

  public CSVHelixFollower mirror() {
    mirror = true;
    return this;
  }

  public CSVHelixFollower reverse() {
    reverse = true;
    return this;
  }

  public abstract void resetDistance();
  public abstract PIDController getHeadingController();
  public abstract PIDController getDistanceController();
  public abstract double getCurrentDistance();
  public abstract double getCurrentHeading();
  public abstract void useOutputs(double left, double right);

  @Override
  protected void initialize() {
    resetDistance();
    getDistanceController().reset();
    getHeadingController().reset();
    isFinished = false;

    pathNotifier.startPeriodic(getIndex(INDEX.DT));
    pidNotifier.startPeriodic(getDistanceController().getPeriod());

    HelixEvents.getInstance().addEvent("HelixFollower", "Starting path: " + filename);
  }

  @Override
  protected void execute() {
    SmartDashboard.putNumber("Distance Path Error", getDistanceController().getError());
    SmartDashboard.putNumber("Heading Path Error", getHeadingController().getError());
  }

  @Override
  protected boolean isFinished() {
    return isFinished;
  }

  @Override
  protected void end() {
    try {
      pathReader.close();
    } catch (IOException e) {

    }
    pathNotifier.stop();
    pidNotifier.stop();
    HelixEvents.getInstance().addEvent("HelixFollower", "Finished path: " + filename);
  }

  @Override
  protected void interrupted() {
    end();
  }


  private void moveToNextSegment() {
    try {
      line = pathReader.readLine();
      if (line == null) {
        isFinished = true;
      } else {
        timestamp = line.split(",");
      }
    } catch (IOException e) {

    }
  }

  private void calculateOutputs() {

    if (line == null) return;
    
    mCurrentDistance = getCurrentDistance();
    mTargetDistance = reverse ? -getIndex(INDEX.CENTER_POSITION) : getIndex(INDEX.CENTER_POSITION);
    mCurrentHeading = getCurrentHeading();
    mTargetHeading = mirror ? -getIndex(INDEX.HEADING) : getIndex(INDEX.HEADING);
    mLeftVelocity = getIndex(mirror ^ reverse ? INDEX.RIGHT_VELOCITY : INDEX.LEFT_VELOCITY);
    mRightVelocity = getIndex(mirror ^ reverse ? INDEX.LEFT_VELOCITY : INDEX.RIGHT_VELOCITY);

    if (reverse) {
      mLeftVelocity *= -1;
      mRightVelocity *= -1;
    }

    getDistanceController().setReference(mTargetDistance);
    getHeadingController().setReference(mTargetHeading);

    double correctedLeftVelocity = mLeftVelocity + getDistanceController().calculate(mCurrentDistance) - getHeadingController().calculate(mCurrentHeading);
    double correctedRightVelocity = mRightVelocity + getDistanceController().calculate(mCurrentDistance) + getHeadingController().calculate(mCurrentHeading);

    useOutputs(correctedLeftVelocity, correctedRightVelocity);
  }

  public double getIndex(INDEX index) {
    return Double.parseDouble(timestamp[(index).getIndex()].replaceAll("\\s", ""));
  }
}
