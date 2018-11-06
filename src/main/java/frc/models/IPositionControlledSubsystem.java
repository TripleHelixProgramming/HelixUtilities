package frc.models;

public interface IPositionControlledSubsystem {

	public int targetPosition = 0;
	public int onTargetThreshold = 0;

	public boolean setTargetPosition(int targetPosition);

	public int getTargetPosition();

	public int getCurrentPosition();

	public double getCurrentVelocity();

	public void motionMagicControl();

	public boolean isInPosition(int targetPosition);
}
