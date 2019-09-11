package com.team2363.commands;

import java.io.IOException;

import com.team2363.controller.PIDController;
import com.team2363.logger.HelixEvents;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;

public abstract class PathFollower extends Command {
  private Notifier pathNotifier = new Notifier(this::moveToNextSegment);
  private Notifier pidNotifier = new Notifier(this::calculateOutputs);

  // The trajectories to follow for each side
  private Trajectory leftTrajectory;
  private Trajectory rightTrajectory;
  private boolean flip;

  private int currentSegment;
  private boolean isFinished;

  /**
   * This will import the path files based on the name of the path provided
   * 
   * @param pathName the name of the path to run
   */
  public PathFollower(String pathName) {
    this(pathName, false);
  }

  public PathFollower(String pathName, boolean flip) {
    requires(getRequiredSubsystem());
    this.flip = !flip;
    importPath(pathName, !flip);
  }

  public abstract Subsystem getRequiredSubsystem();
  public abstract void resetDistance();
  public abstract PIDController getHeadingController();
  public abstract PIDController getDistanceController();
  public abstract double getCurrentDistance();
  public abstract double getCurrentHeading();
  public abstract void useOutputs(double left, double right);

  @Override
  protected void initialize() {
    resetDistance();
    //Make sure we're starting at the beginning of the path
    getDistanceController().reset();
    getHeadingController().reset();
    currentSegment = 0;
    isFinished = false;

    // Start running the path
    pathNotifier.startPeriodic(leftTrajectory.get(0).dt);
    pidNotifier.startPeriodic(getDistanceController().getPeriod());

    // If there  was an issue with importing the paths then we should just finish this command instantly
    if (leftTrajectory == null || rightTrajectory == null) {
      HelixEvents.getInstance().addEvent("DRIVETRAIN", "There was an issue importing the path, ending the command instantly.");
      isFinished = true;
    }
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
    pathNotifier.stop();
    pidNotifier.stop();
  }

  @Override
  protected void interrupted() {
    end();
  }

  private void importPath(String pathName, boolean flip) {
    try {
      if (flip) {
        leftTrajectory = PathfinderFRC.getTrajectory(pathName + ".right");
        rightTrajectory = PathfinderFRC.getTrajectory(pathName + ".left");
      } else {
        leftTrajectory = PathfinderFRC.getTrajectory(pathName + ".left");
        rightTrajectory = PathfinderFRC.getTrajectory(pathName + ".right");
      }

    } catch (IOException e) {
		  e.printStackTrace();
	  }
  }

  private void moveToNextSegment() {
    // Move to the next segment in the path
    currentSegment++;

    // Was that the last segment in our path?
    if (currentSegment >= leftTrajectory.length()) {
      isFinished = true;
    }
  }

  private void calculateOutputs() {
    // We need to get the current segment right away so it doesn't change in the middle
    // of the calculations
    int segment = currentSegment;
    // If we're finished there are no more segments to read from and we should return
    if (segment >= leftTrajectory.length()) {
      return;
    }

    // Get our expected velocities based on the paths
    double leftVelocity = leftTrajectory.get(segment).velocity;
    double rightVelocity = rightTrajectory.get(segment).velocity;

    // Set our expected position to be the setpoint of our distance controller
    // The position will be an average of both the left and right to give us the overall distance
    double expectedPosition = (leftTrajectory.get(segment).position + rightTrajectory.get(segment).position) / 2.0;
    getDistanceController().setReference(expectedPosition);
    double currentPosition = getCurrentDistance();

    // Set our expected heading to be the setpoint of our direction controller
    double expectedHeading = Math.toDegrees(leftTrajectory.get(segment).heading);
    // If the path is flipped, invert the sign of the heading
    getHeadingController().setReference(flip ? -expectedHeading : expectedHeading);
    double currentHeading = getCurrentHeading();

    // The final velocity is going to be a combination of our expected velocity corrected by our distance error and our heading error
    // velocity = expected + distanceError +/- headingError
    double correctedLeftVelocity = leftVelocity + getDistanceController().calculate(currentPosition) - getHeadingController().calculate(currentHeading);
    double correctedRightVelocity = rightVelocity + getDistanceController().calculate(currentPosition) + getHeadingController().calculate(currentHeading);

    useOutputs(correctedLeftVelocity, correctedRightVelocity);
  }
}
